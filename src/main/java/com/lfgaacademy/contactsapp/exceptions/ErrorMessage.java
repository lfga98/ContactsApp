package com.lfgaacademy.contactsapp.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
    private Integer statusCode;
    private String message;

    public ErrorMessage(String message, Integer status) {
        this.message=message;
        this.statusCode=status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
