package com.mr_faton.core.exception;

/**
 * Created by Mr_Faton on 21.10.2015.
 */
public class BadUserException extends Exception {
    public BadUserException() {
    }

    public BadUserException(String message) {
        super(message);
    }

    public BadUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadUserException(Throwable cause) {
        super(cause);
    }
}
