package com.mr_faton.core.table;

import java.util.Date;

/**
 * Created by Mr_Faton on 18.09.2015.
 */
public class Message {
    private int id;
    private String message;

    private String owner;
    private boolean ownerMale;

    private String recipient;
    private Boolean recipientMale;

    private Date postedDate;

    private boolean synonymized;
    private boolean posted;


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

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isOwnerMale() {
        return ownerMale;
    }
    public void setOwnerMale(boolean ownerMale) {
        this.ownerMale = ownerMale;
    }

    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Boolean isRecipientMale() {
        return recipientMale;
    }
    public void setRecipientMale(Boolean recipientMale) {
        this.recipientMale = recipientMale;
    }

    public Date getPostedDate() {
        return postedDate;
    }
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public boolean isSynonymized() {
        return synonymized;
    }
    public void setSynonymized(boolean synonymized) {
        this.synonymized = synonymized;
    }

    public boolean isPosted() {
        return posted;
    }
    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    @Override
    public String toString() {
        return owner + ": " + message;
    }
}
