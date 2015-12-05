package com.mr_faton.core.table;

import com.mr_faton.core.util.TimeWizard;

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

    @Override
    public String toString() {
        return "Mention{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerMale='" + ownerMale + '\'' +
                ", recipient='" + recipient + '\'' +
                ", recipientMale='" + recipientMale + '\'' +
                ", message='" + message + '\'' +
                ", postedDate='" + TimeWizard.formatDateWithTime(postedDate.getTime()) + '\'' +
                ", synonymized='" + synonymized + '\'' +
                ", reposted='" + reposted + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Mention mention = (Mention) o;

        if (recipientMale != mention.recipientMale) return false;
        return recipient.equals(mention.recipient);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + recipient.hashCode();
        result = 31 * result + (recipientMale ? 1 : 0);
        return result;
    }
}
