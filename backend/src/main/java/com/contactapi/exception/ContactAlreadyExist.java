package com.contactapi.exception;

public class ContactAlreadyExist extends RuntimeException{
    public ContactAlreadyExist(String message) {
        super(message);
    }
}
