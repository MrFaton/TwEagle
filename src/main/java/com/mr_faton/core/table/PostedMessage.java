package com.mr_faton.core.table;


import java.util.Date;

/**
 * Created by Mr_Faton on 19.09.2015.
 */
public class PostedMessage {
    private int id;
    private String message;
    private long messageId;
    private String owner;
    private String recipient;
    private Date postedDate;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageId() {
        return messageId;
    }
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Date getPostedDate() {
        return postedDate;
    }
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        return owner + ": " + message;
    }
}
