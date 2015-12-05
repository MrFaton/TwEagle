package com.mr_faton.core.table;

import com.mr_faton.core.util.TimeWizard;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
public class Tweet {
     int id;
    String owner;
    boolean ownerMale;
    String message;
    Date postedDate;
    boolean synonymized;
    boolean reposted;

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

    public boolean isSynonymized() {
        return synonymized;
    }

    public void setSynonymized(boolean synonymized) {
        this.synonymized = synonymized;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public boolean isReposted() {
        return reposted;
    }

    public void setReposted(boolean reposted) {
        this.reposted = reposted;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerMale='" + ownerMale + '\'' +
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

        Tweet tweet = (Tweet) o;

        if (id != tweet.id) return false;
        if (ownerMale != tweet.ownerMale) return false;
        if (synonymized != tweet.synonymized) return false;
        if (reposted != tweet.reposted) return false;
        if (owner != null ? !owner.equals(tweet.owner) : tweet.owner != null) return false;
        if (message != null ? !message.equals(tweet.message) : tweet.message != null) return false;
        return !(postedDate != null ? !postedDate.equals(tweet.postedDate) : tweet.postedDate != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (ownerMale ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (postedDate != null ? postedDate.hashCode() : 0);
        result = 31 * result + (synonymized ? 1 : 0);
        result = 31 * result + (reposted ? 1 : 0);
        return result;
    }
}
