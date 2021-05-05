package com.lfgaacademy.contactsapp.exceptions;

public class DeletedNotFoundException extends RuntimeException{
    public DeletedNotFoundException(String message) {
        super(message);
    }
}
