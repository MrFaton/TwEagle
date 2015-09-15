package com.mr_faton.core.exception;

/**
 * Created by Mr_Faton on 15.09.2015.
 */
public class NoSuchEntityException extends Exception {
    public NoSuchEntityException() {
    }

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
