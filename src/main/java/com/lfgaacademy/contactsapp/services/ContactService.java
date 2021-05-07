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
        if (contactRepository.findContactById(idContact)==null)
            throw new ContactNotFoundException("Contact with id: " + idContact + " doesn't exist.");
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
        if (!idContact.equals(newContact.getId()))
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
            if (newContact.getId()==null)
                throw new UniqueConstraintException("The email: " + newContact.getEmail() + " is already being used.");
            else if (contactRepository.findContactByEmail(newContact.getEmail()).getId()!=newContact.getId())
                throw new UniqueConstraintException("The email: " + newContact.getEmail() + " is already being used.");
        }

        if (!newContact.getPhoneNumber().isEmpty()&&contactRepository.findContactByPhoneNumber(newContact.getPhoneNumber()) != null) {
            if (newContact.getId()==null)
                throw new UniqueConstraintException("The phone number: " + newContact.getPhoneNumber() + " is already being used.");
            if (contactRepository.findContactByPhoneNumber(newContact.getPhoneNumber()).getId()!=newContact.getId())
                throw new UniqueConstraintException("The phone number: " + newContact.getPhoneNumber() + " is already being used.");
        }

        if (containsNonCharacters(newContact.getName())) {
            throw new FormatInvalidException("The name: " + newContact.getName() + " must be only characters.");
        }

        if (containsNonCharacters(newContact.getLastName())) {
            throw new FormatInvalidException("The last name: " + newContact.getLastName() + " must be only characters.");
        }


        if (emailValidate(newContact.getEmail())) {
            throw new FormatInvalidException("The email: " + newContact.getEmail() + " is not a valid email.");
        }
        if (newContact.getPhoneNumber().length()!=0&&!newContact.getPhoneNumber().matches("[0-9]+")){
            throw new FormatInvalidException("The phone number: " + newContact.getPhoneNumber() + " must contain only digits.");
        }


    }

    private boolean emailValidate(String email) {
        Matcher matcher = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE).matcher(email);
        return !matcher.find();
    }

    private boolean containsNonCharacters(String value){
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]\"\'~-]");
        Matcher hasSpecial = special.matcher(value);
        return hasSpecial.find()||value.matches(".*\\d.*");
    }


}
