package com.mr_faton.core.table;

import java.util.Date;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 07.10.2015
 */
public class DonorUser {
    private String name;
    private boolean male;
    private Date takeMessageDate;
    private Date takeFollowingDate;
    private Date takeFollowersDate;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return male;
    }
    public void setMale(boolean male) {
        this.male = male;
    }

    public Date getTakeMessageDate() {
        return takeMessageDate;
    }
    public void setTakeMessageDate(Date takeMessageDate) {
        this.takeMessageDate = takeMessageDate;
    }

    public Date getTakeFollowingDate() {
        return takeFollowingDate;
    }
    public void setTakeFollowingDate(Date takeFollowingDate) {
        this.takeFollowingDate = takeFollowingDate;
    }

    public Date getTakeFollowersDate() {
        return takeFollowersDate;
    }
    public void setTakeFollowersDate(Date takeFollowersDate) {
        this.takeFollowersDate = takeFollowersDate;
    }

    @Override
    public String toString() {
        return name;
    }
}
