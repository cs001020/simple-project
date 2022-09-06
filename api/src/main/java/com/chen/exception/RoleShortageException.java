package com.chen.exception;

/**
 * @author CHEN
 */
public class RoleShortageException extends RuntimeException {
    public RoleShortageException(String message) {
        super(message);
    }
}
