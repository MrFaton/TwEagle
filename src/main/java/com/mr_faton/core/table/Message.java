package com.mr_faton.core.table;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 18.09.2015
 * @version 1.0
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
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerMale=" + ownerMale +
                ", recipient='" + recipient + '\'' +
                ", recipientMale=" + recipientMale +
                ", postedDate=" + postedDate +
                ", synonymized=" + synonymized +
                ", posted=" + posted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (id != message1.id) return false;
        if (ownerMale != message1.ownerMale) return false;
        if (synonymized != message1.synonymized) return false;
        if (posted != message1.posted) return false;
        if (!message.equals(message1.message)) return false;
        if (!owner.equals(message1.owner)) return false;
        if (recipient != null ? !recipient.equals(message1.recipient) : message1.recipient != null) return false;
        if (recipientMale != null ? !recipientMale.equals(message1.recipientMale) : message1.recipientMale != null)
            return false;
        return postedDate.equals(message1.postedDate);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + message.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + (ownerMale ? 1 : 0);
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (recipientMale != null ? recipientMale.hashCode() : 0);
        result = 31 * result + postedDate.hashCode();
        result = 31 * result + (synonymized ? 1 : 0);
        result = 31 * result + (posted ? 1 : 0);
        return result;
    }
}
