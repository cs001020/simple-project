package com.chen.exception;

public class UserPasswordInaccuracyException extends RuntimeException {
    public UserPasswordInaccuracyException(String message) {
        super(message);
    }
}
