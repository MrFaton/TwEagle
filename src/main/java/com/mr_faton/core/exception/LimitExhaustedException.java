package com.mr_faton.core.exception;

import twitter4j.HttpResponse;
import twitter4j.TwitterException;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 28.09.2015
 */
public class LimitExhaustedException extends TwitterException {
    public LimitExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitExhaustedException(String message) {
        super(message);
    }

    public LimitExhaustedException(Exception cause) {
        super(cause);
    }

    public LimitExhaustedException(String message, HttpResponse res) {
        super(message, res);
    }

    public LimitExhaustedException(String message, Exception cause, int statusCode) {
        super(message, cause, statusCode);
    }
}
