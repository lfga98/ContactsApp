package com.lfgaacademy.contactsapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(DeletedNotFoundException.class)
    public ErrorMessage deletedContactNotFound(DeletedNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.NO_CONTENT.value());
        return errorMessage;
    }

    @ExceptionHandler(MismatchIdException.class)
    public ErrorMessage mismatchIdContact(MismatchIdException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }

    @ExceptionHandler(UniqueConstraintException.class)
    public ErrorMessage uniqueConstraints(UniqueConstraintException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }

    @ExceptionHandler(FormatInvalidException.class)
    public ErrorMessage formatInvalidInputs(FormatInvalidException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return errorMessage;
    }

}