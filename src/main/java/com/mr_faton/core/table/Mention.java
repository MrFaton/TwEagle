package com.mr_faton.core.table;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
public class Mention extends Tweet {
    private String recipient;
    private boolean recipientMale;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean isRecipientMale() {
        return recipientMale;
    }

    public void setRecipientMale(boolean recipientMale) {
        this.recipientMale = recipientMale;
    }
}
