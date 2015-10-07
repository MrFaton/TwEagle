package com.mr_faton.core.exception;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 07.10.2015
 */
public class DataSequenceReachedException extends Exception {
    public DataSequenceReachedException() {
    }

    public DataSequenceReachedException(String message) {
        super(message);
    }

    public DataSequenceReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSequenceReachedException(Throwable cause) {
        super(cause);
    }
}
