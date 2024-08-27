package com.tecflux.exception;

public class CnpjAlreadyExistsException extends RuntimeException {

    public CnpjAlreadyExistsException(String message) {
        super(message);
    }
}