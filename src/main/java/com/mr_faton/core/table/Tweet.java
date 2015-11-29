package com.mr_faton.core.table;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.11.2015
 */
public class Tweet {
    private int id;
    private String owner;
    private boolean ownerMale;
    private String message;
    private Date postedDate;
    private boolean synonymized;
    private boolean reposted;

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
}
