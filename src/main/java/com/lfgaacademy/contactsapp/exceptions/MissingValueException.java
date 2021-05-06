package com.lfgaacademy.contactsapp.exceptions;

public class MissingValueException extends RuntimeException{
    public MissingValueException(String message) {
        super(message);
    }
}
