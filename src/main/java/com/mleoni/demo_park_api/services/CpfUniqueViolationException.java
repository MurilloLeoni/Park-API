package com.mleoni.demo_park_api.services;

public class CpfUniqueViolationException extends RuntimeException {
    public CpfUniqueViolationException(String message) {
        super(message);
    }
}
