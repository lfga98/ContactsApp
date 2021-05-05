package com.lfgaacademy.contactsapp.controllores;


import com.lfgaacademy.contactsapp.entities.Contact;
import com.lfgaacademy.contactsapp.services.ContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{idContact}")
    public Contact getContactById(@PathVariable(name="idContact") Long idContact){
        return contactService.findContactById(idContact);
    }

    @PostMapping
    public Contact saveNewContact(@RequestBody Contact contact){
        return contactService.saveContact(contact);
    }

    @PutMapping("/{idContact}")
    public Contact editContact(@PathVariable(name="idContact") Long idContact,@RequestBody Contact contact){
        return contactService.editContact(idContact,contact);
    }

    @DeleteMapping("/{idContact}")
    public Contact deleteContact(@PathVariable(name="idContact") Long idContact){
        return contactService.deleteContact(idContact);
    }


}
