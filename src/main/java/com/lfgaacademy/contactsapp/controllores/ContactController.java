package com.lfgaacademy.contactsapp.controllores;


import com.lfgaacademy.contactsapp.entities.Contact;
import com.lfgaacademy.contactsapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Contact> getAllContacts() {
        List<Contact> listContacts = contactService.getAllContacts();
        return listContacts;
    }




}
