package com.lfgaacademy.contactsapp.services;


import com.lfgaacademy.contactsapp.entities.Contact;
import com.lfgaacademy.contactsapp.exceptions.*;
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
        if (idContact.equals(newContact.getId()))
            throw new MismatchIdException("ContactId: " + idContact + " doesn't match object's ID: " + newContact.getId());

        fieldsValidator(newContact);

        return contactRepository.save(newContact);
    }


    private void fieldsValidator(Contact newContact) {

        if (newContact.getName().length()<=0){
            throw new MissingValueException("Name can't be empty");
        }

        if (newContact.getLastName().length()<=0){
            throw new MissingValueException("Last name can't be empty");
        }

        if (newContact.getEmail().length()<=0){
            throw new MissingValueException("Email can't be empty");
        }

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

        if (newContact.getPhoneNumber().length() != 10&&newContact.getPhoneNumber().length() != 0) {
            throw new FormatInvalidException("The phone number: " + newContact.getPhoneNumber() + " must have 10 digits.");
        }

        if (emailValidate(newContact.getEmail())) {
            throw new FormatInvalidException("The email: " + newContact.getEmail() + " is not a valid email.");
        }
        if (newContact.getPhoneNumber().length()==10&&!newContact.getPhoneNumber().matches("[0-9]+")){
            throw new FormatInvalidException("The phone number: " + newContact.getPhoneNumber() + " must contain only digits.");
        }


    }

    private boolean emailValidate(String email) {
        Matcher matcher = Pattern.compile("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}(.[a-z]{2,3})+$|^$", Pattern.CASE_INSENSITIVE).matcher(email);
        return !matcher.find();
    }


}
