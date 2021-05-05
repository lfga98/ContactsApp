package com.lfgaacademy.contactsapp.exceptions;

public class UniqueConstraintException extends RuntimeException{
    public UniqueConstraintException(String message) {
        super(message);
    }
}
