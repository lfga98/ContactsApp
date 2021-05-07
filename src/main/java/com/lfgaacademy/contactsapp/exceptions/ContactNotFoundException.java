package com.lfgaacademy.contactsapp.exceptions;

public class ContactNotFoundException extends RuntimeException{
    public ContactNotFoundException(String message) {
        super(message);
    }
}
