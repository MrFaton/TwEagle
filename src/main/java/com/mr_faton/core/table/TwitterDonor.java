package com.mr_faton.core.table;

import java.util.Date;

public class TwitterDonor {
    private String name;
    private boolean male;

    private boolean takeMessage;
    private boolean takeFollowing;
    private boolean takeFollowers;
    private boolean takeStatus;

    private Date takeMessagesDate;
    private Date takeFollowingDate;
    private Date takeFollowersDate;
    private Date takeStatusDate;


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



    public boolean isTakeMessage() {
        return takeMessage;
    }
    public void setTakeMessage(boolean takeMessage) {
        this.takeMessage = takeMessage;
    }

    public boolean isTakeFollowing() {
        return takeFollowing;
    }
    public void setTakeFollowing(boolean takeFollowing) {
        this.takeFollowing = takeFollowing;
    }

    public boolean isTakeFollowers() {
        return takeFollowers;
    }
    public void setTakeFollowers(boolean takeFollowers) {
        this.takeFollowers = takeFollowers;
    }

    public boolean isTakeStatus() {
        return takeStatus;
    }
    public void setTakeStatus(boolean takeStatus) {
        this.takeStatus = takeStatus;
    }



    public Date getTakeMessagesDate() {
        return takeMessagesDate;
    }
    public void setTakeMessagesDate(Date takeMessagesDate) {
        this.takeMessagesDate = takeMessagesDate;
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

    public Date getTakeStatusDate() {
        return takeStatusDate;
    }
    public void setTakeStatusDate(Date takeStatusDate) {
        this.takeStatusDate = takeStatusDate;
    }
}
