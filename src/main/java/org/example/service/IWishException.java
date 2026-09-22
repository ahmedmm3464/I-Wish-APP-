package org.example.service;

public class IWishException extends RuntimeException {
    public IWishException(String message) { super(message); }
    public IWishException(String message, Throwable cause) { super(message, cause); }
}
