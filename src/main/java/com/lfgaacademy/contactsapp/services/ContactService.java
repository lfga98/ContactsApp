package com.lfgaacademy.contactsapp.services;


import com.lfgaacademy.contactsapp.entities.Contact;
import com.lfgaacademy.contactsapp.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;


    public List<Contact> getAllContacts(){
        return contactRepository.findAll();
    }
}
