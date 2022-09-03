package com.chen.exception;

/**
 * @author CHEN
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
