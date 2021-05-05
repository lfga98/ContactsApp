package com.lfgaacademy.contactsapp.repositories;

import com.lfgaacademy.contactsapp.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
    List<Contact> findAll();
    Contact findContactById(Long contactID);
    void deleteById(Long contactId);
    Contact findContactByEmail(String email);
    Contact findContactByPhoneNumber(String email);


}
