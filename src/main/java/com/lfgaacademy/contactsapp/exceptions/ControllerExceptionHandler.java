package com.lfgaacademy.contactsapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(MissingValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage missingValueOrEmpty(MissingValueException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }



    @ExceptionHandler(DeletedNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorMessage deletedContactNotFound(DeletedNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.NO_CONTENT.value());
        return errorMessage;
    }

    @ExceptionHandler(MismatchIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage mismatchIdContact(MismatchIdException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }

    @ExceptionHandler(UniqueConstraintException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage uniqueConstraints(UniqueConstraintException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }

    @ExceptionHandler(FormatInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage formatInvalidInputs(FormatInvalidException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }

    @ExceptionHandler(ContactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage contactNotFound(ContactNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return errorMessage;
    }



}