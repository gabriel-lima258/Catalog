package com.gtech.catalog.services.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
