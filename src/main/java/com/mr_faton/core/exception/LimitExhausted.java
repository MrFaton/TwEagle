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
public class LimitExhausted extends TwitterException {
    public LimitExhausted(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitExhausted(String message) {
        super(message);
    }

    public LimitExhausted(Exception cause) {
        super(cause);
    }

    public LimitExhausted(String message, HttpResponse res) {
        super(message, res);
    }

    public LimitExhausted(String message, Exception cause, int statusCode) {
        super(message, cause, statusCode);
    }
}
