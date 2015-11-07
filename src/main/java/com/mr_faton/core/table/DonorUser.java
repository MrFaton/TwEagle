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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DonorUser donorUser = (DonorUser) o;

        if (male != donorUser.male) return false;
        if (!name.equals(donorUser.name)) return false;
        if (takeMessageDate != null ? !takeMessageDate.equals(donorUser.takeMessageDate) : donorUser.takeMessageDate != null)
            return false;
        if (takeFollowingDate != null ? !takeFollowingDate.equals(donorUser.takeFollowingDate) : donorUser.takeFollowingDate != null)
            return false;
        return !(takeFollowersDate != null ? !takeFollowersDate.equals(donorUser.takeFollowersDate) : donorUser.takeFollowersDate != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (male ? 1 : 0);
        result = 31 * result + (takeMessageDate != null ? takeMessageDate.hashCode() : 0);
        result = 31 * result + (takeFollowingDate != null ? takeFollowingDate.hashCode() : 0);
        result = 31 * result + (takeFollowersDate != null ? takeFollowersDate.hashCode() : 0);
        return result;
    }
}
