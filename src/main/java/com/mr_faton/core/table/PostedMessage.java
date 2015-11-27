package com.mr_faton.core.table;


import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 19.09.2015
 */
public class PostedMessage {
    private int id;

    private int messageId;
    private String message;

    private long twitterId;

    private String owner;
    private Boolean ownerMale;

    private String recipient;
    private Boolean recipientMale;

    private boolean retweeted;

    private Date postedDate;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public long getTwitterId() {
        return twitterId;
    }
    public void setTwitterId(long twitterId) {
        this.twitterId = twitterId;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean isOwnerMale() {
        return ownerMale;
    }
    public void setOwnerMale(Boolean ownerMale) {
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

    public boolean isRetweeted() {
        return retweeted;
    }
    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostedMessage that = (PostedMessage) o;

        if (id != that.id) return false;
        if (messageId != that.messageId) return false;
        if (twitterId != that.twitterId) return false;
        if (!message.equals(that.message)) return false;
        if (!owner.equals(that.owner)) return false;
        if (!ownerMale.equals(that.ownerMale)) return false;
        if (recipient != null ? !recipient.equals(that.recipient) : that.recipient != null) return false;
        if (recipientMale != null ? !recipientMale.equals(that.recipientMale) : that.recipientMale != null)
            return false;
        return postedDate.equals(that.postedDate);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + messageId;
        result = 31 * result + message.hashCode();
        result = 31 * result + (int) (twitterId ^ (twitterId >>> 32));
        result = 31 * result + owner.hashCode();
        result = 31 * result + ownerMale.hashCode();
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (recipientMale != null ? recipientMale.hashCode() : 0);
        result = 31 * result + postedDate.hashCode();
        return result;
    }
}
