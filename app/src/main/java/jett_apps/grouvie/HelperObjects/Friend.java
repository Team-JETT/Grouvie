package jett_apps.grouvie.HelperObjects;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Friend implements Serializable, Comparable<Friend> {
    private String name;
    private String phoneNum;
    private boolean checked = false;
    private boolean hasAccepted = false;

    private String filmChange;
    private String timeChange;
    private String cinemaChange;
    private String dateChange;


    public Friend(String name, String phoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return name;
    }
    public void toggleChecked() {
        checked = !checked;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        return phoneNum.equals(friend.phoneNum);

    }

    @Override
    public int hashCode() {
        return phoneNum.hashCode();
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public int compareTo(@NonNull Friend o) {
        return getName().compareTo(o.getName());
    }

    public String getFilmChange() {
        return filmChange;
    }

    public String getTimeChange() {
        return timeChange;
    }

    public String getDateChange() {
        return dateChange;
    }

    public String getCinemaChange() {
        return cinemaChange;
    }

    public void setFilmChange(String filmChange) {
        this.filmChange = filmChange;
    }

    public void setTimeChange(String timeChange) {
        this.timeChange = timeChange;
    }

    public void setDateChange(String dateChange) {
        this.dateChange = dateChange;
    }

    public void setCinemaChange(String cinemaChange) {
        this.cinemaChange = cinemaChange;
    }

    public boolean hasAccepted() {
        return hasAccepted;
    }

    public void setHasAccepted(boolean b) {
        this.hasAccepted = b;
    }
}
