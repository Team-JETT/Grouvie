package jett_apps.grouvie.HelperObjects;
import java.io.Serializable;
import java.util.ArrayList;

public class PropagationObject implements Serializable {
    
    private String date;
    private int day;
    private int month;
    private int year;
    private ArrayList<Friend> selectedFriends;
    private String cinemaData;
    private String filmTitle;
    private String showtimeDistance;
    private String chosenTime;
    private ArrayList<Film> listOfFilms;

    public PropagationObject() {
        
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

    public void setSelectedFriends(ArrayList<Friend> selectedFriends) {
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

    public String getCinema() {
        return cinemaData;
    }

    public void setShowtimeDistance(String showtimeDistance) {
        this.showtimeDistance = showtimeDistance;
    }

    public String getShowtimeDistance() {
        return showtimeDistance;
    }

    public ArrayList<Friend> getSelectedFriends() {
        return selectedFriends;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setListOfFilms(ArrayList<Film> listOfFilms) {
        this.listOfFilms = listOfFilms;
    }

    public ArrayList<Film> getListOfFilms() {
        return listOfFilms;
    }
}
