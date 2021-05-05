package com.lfgaacademy.contactsapp.services;


import com.lfgaacademy.contactsapp.entities.Contact;
import com.lfgaacademy.contactsapp.exceptions.DeletedNotFoundException;
import com.lfgaacademy.contactsapp.exceptions.FormatInvalidException;
import com.lfgaacademy.contactsapp.exceptions.MissmatchIdException;
import com.lfgaacademy.contactsapp.exceptions.UniqueConstraintException;
import com.lfgaacademy.contactsapp.repositories.ContactRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact findContactById(Long idContact) {
        return contactRepository.findContactById(idContact);
    }

    public Contact deleteContact(Long idContact) {
        Contact contact = contactRepository.findContactById(idContact);

        if (contact == null)
            throw new DeletedNotFoundException("Contact with id: " + idContact + " not found, couldn't be deleted");
        contactRepository.deleteById(idContact);

        return contact;
    }

    public Contact saveContact(Contact newContact) {
        fieldsValidator(newContact);
        return contactRepository.save(newContact);
    }

    public Contact editContact(Long idContact, Contact newContact) {
        if (idContact != newContact.getId())
            throw new MissmatchIdException("ContactId: " + idContact + " doesn't match object's ID: " + newContact.getId());
        fieldsValidator(newContact);

        return contactRepository.save(newContact);
    }


    private void fieldsValidator(Contact newContact) {
        if (contactRepository.findContactByEmail(newContact.getEmail()) != null) {
            throw new UniqueConstraintException("The email: " + newContact.getEmail() + " is already being used.");
        }

        if (contactRepository.findContactByPhoneNumber(newContact.getPhoneNumber()) != null) {
            throw new UniqueConstraintException("The phone number: " + newContact.getPhoneNumber() + " is already being used.");
        }

        if (newContact.getName().matches(".*\\d.*")) {
            throw new FormatInvalidException("The name: " + newContact.getName() + " contains numbers. Name can't contain numbers.");
        }

        if (newContact.getLastName().matches(".*\\d.*")) {
            throw new FormatInvalidException("The last name: " + newContact.getLastName() + " contains numbers. Name can't contain numbers.");
        }

        if (newContact.getPhoneNumber().length() != 10) {
            throw new FormatInvalidException("The phone number: " + newContact.getPhoneNumber() + " must have 10 digits.");
        }

        if (emailValidate(newContact.getEmail())) {
            throw new FormatInvalidException("The email: " + newContact.getEmail() + " is not a valid email.");
        }


    }

    private boolean emailValidate(String email) {
        Matcher matcher = Pattern.compile("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}(.[a-z]{2,3})+$|^$", Pattern.CASE_INSENSITIVE).matcher(email);
        return !matcher.find();
    }


}
