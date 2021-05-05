package com.lfgaacademy.contactsapp.services;


import com.lfgaacademy.contactsapp.entities.Contact;
import com.lfgaacademy.contactsapp.exceptions.DeletedNotFoundException;
import com.lfgaacademy.contactsapp.exceptions.MissmatchIdException;
import com.lfgaacademy.contactsapp.repositories.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return contactRepository.save(newContact);
    }

    public Contact editContact(Long idContact, Contact newContact) {
        if (idContact != newContact.getId()) {
            throw new MissmatchIdException("ContactId: " + idContact + " doesn't match object's ID: " + newContact.getId());
        }
        return contactRepository.save(newContact);
    }

}
