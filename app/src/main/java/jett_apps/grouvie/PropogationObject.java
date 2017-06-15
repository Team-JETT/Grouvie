package jett_apps.grouvie;

import org.json.JSONArray;

import java.io.Serializable;

class PropogationObject implements Serializable {
    
    String date;
    int day;
    int month;
    int year;
    private String[] selectedFriends;
    private String cinemaData;
    private String filmTitle;
    private String showtimeDistance;
    private String chosenTime;

    public PropogationObject() {
        
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setSelectedFriends(String[] selectedFriends) {
        this.selectedFriends = selectedFriends;
    }

    public void setCinemaData(String cinemaData) {
        this.cinemaData = cinemaData;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getCinemaData() {
        return cinemaData;
    }

    public void setShowtimeDistance(String showtimeDistance) {
        this.showtimeDistance = showtimeDistance;
    }

    public String getShowtimeDistance() {
        return showtimeDistance;
    }

    public String[] getSelectedFriends() {
        return selectedFriends;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public String getChosenTime() {
        return chosenTime;
    }
}
