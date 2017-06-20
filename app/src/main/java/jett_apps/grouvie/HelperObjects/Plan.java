package jett_apps.grouvie.HelperObjects;


import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable, Comparable<Plan> {

    private String leaderPhoneNum;
    private String creationDateTime;
    private String suggestedFilm;
    private String suggestedCinema;
    private String suggestedShowTime;
    private ArrayList<Friend> eventMembers;
    private String cinemaData;
    private String showtimeDistance;
    private boolean isInitialPlan;

    private String suggestedDate;
    private int suggestedDay;
    private int suggestedMonth;
    private int suggestedYear;

    private ArrayList<Film> listOfFilms;
    private ArrayList<String> cinemaList;
    private JSONArray cinemaDataJson;
    private String moviePoster;

    public Plan() {

    }

    public Plan(String suggestedFilm, String suggestedCinema, String suggestedShowTime,
                String suggestedDate, ArrayList<Friend> eventMembers, String leaderPhoneNum) {
        this.suggestedFilm = suggestedFilm;
        this.suggestedCinema = suggestedCinema;
        this.suggestedShowTime = suggestedShowTime;
        this.suggestedDate = suggestedDate;
        this.eventMembers = eventMembers;
        this.leaderPhoneNum = leaderPhoneNum;
    }

    // TODO: Someone explain to Erkin why this is here???????
    public Plan(Plan oldPlan) {
        this.suggestedFilm = oldPlan.getSuggestedFilm();
        this.suggestedCinema = oldPlan.getSuggestedCinema();
        this.suggestedShowTime = oldPlan.getSuggestedShowTime();
        this.leaderPhoneNum = oldPlan.getLeaderPhoneNum();
        this.eventMembers = oldPlan.getEventMembers();
        this.cinemaData = oldPlan.getCinemaData();
        this.showtimeDistance = oldPlan.getShowtimeDistance();
        this.isInitialPlan = oldPlan.isInitialPlan();
        this.suggestedDate = oldPlan.getSuggestedDate();
        this.suggestedDay = oldPlan.getSuggestedDay();
        this.suggestedMonth = oldPlan.getSuggestedMonth();
        this.suggestedYear = oldPlan.getSuggestedYear();
        this.listOfFilms = oldPlan.getListOfFilms();
    }

    public String getSuggestedDate() {
        return suggestedDate;
    }

    public void setSuggestedDate(String suggestedDate) {
        this.suggestedDate = suggestedDate;
    }

    public String getSuggestedShowTime() {
        return suggestedShowTime;
    }

    public void setSuggestedShowTime(String suggestedShowTime) {
        this.suggestedShowTime = suggestedShowTime;
    }

    public String getSuggestedCinema() {
        return suggestedCinema;
    }

    public void setSuggestedCinema(String suggestedCinema) {
        this.suggestedCinema = suggestedCinema;
    }

    public String getSuggestedFilm() {
        return suggestedFilm;
    }

    public void setSuggestedFilm(String suggestedFilm) {
        this.suggestedFilm = suggestedFilm;
    }

    public ArrayList<Friend> getEventMembers() {
        return eventMembers;
    }

    public void setEventMembers(ArrayList<Friend> eventMembers) {
        this.eventMembers = eventMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        return  (suggestedFilm.equals(plan.suggestedFilm) &&
                 suggestedCinema.equals(plan.suggestedCinema) &&
                 suggestedShowTime.equals(plan.suggestedShowTime) &&
                 suggestedDate.equals(plan.suggestedDate) &&
                 eventMembers.equals(plan.eventMembers));
    }

    @Override
    public int hashCode() {
        int result = suggestedFilm.hashCode();
        result = 31 * result + suggestedCinema.hashCode();
        result = 31 * result + suggestedShowTime.hashCode();
        result = 31 * result + suggestedDate.hashCode();
        result = 31 * result + eventMembers.hashCode();
        return result;
    }

    public String getLeaderPhoneNum() {
        return leaderPhoneNum;
    }

    public void setLeaderPhoneNum(String leaderPhoneNum) {
        this.leaderPhoneNum = leaderPhoneNum;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCinemaData() {
        return cinemaData;
    }

    public void setCinemaData(String cinemaData) {
        this.cinemaData = cinemaData;
    }

    public void setListOfFilms(ArrayList<Film> listOfFilms) {
        this.listOfFilms = listOfFilms;
    }

    public ArrayList<Film> getListOfFilms() {
        return listOfFilms;
    }

    public int getSuggestedDay() {
        return suggestedDay;
    }

    public void setSuggestedDay(int suggestedDay) {
        this.suggestedDay = suggestedDay;
    }

    public int getSuggestedMonth() {
        return suggestedMonth;
    }

    public void setSuggestedMonth(int suggestedMonth) {
        this.suggestedMonth = suggestedMonth;
    }

    public int getSuggestedYear() {
        return suggestedYear;
    }

    public void setSuggestedYear(int suggestedYear) {
        this.suggestedYear = suggestedYear;
    }

    public void setShowtimeDistance(String showtimeDistance) {
        this.showtimeDistance = showtimeDistance;
    }

    public String getShowtimeDistance() {
        return showtimeDistance;
    }

    public boolean isInitialPlan() {
        return isInitialPlan;
    }

    public void setInitialPlan(boolean initialPlan) {
        isInitialPlan = initialPlan;
    }

    public void setCinemaList(ArrayList<String> cinemaList) {
        this.cinemaList = cinemaList;
    }

    public ArrayList<String> getCinemaList() {
        return cinemaList;
    }

    public void setCinemaDataJson(JSONArray cinemaDataJson) {
        this.cinemaDataJson = cinemaDataJson;
    }

    public JSONArray getCinemaDataJson() {
        return cinemaDataJson;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    @Override
    public int compareTo(@NonNull Plan o) {
        int earlierMonth = getSuggestedMonth() - o.getSuggestedMonth();
        int earlierDay = getSuggestedDay() - o.getSuggestedDay();

        return earlierMonth + earlierDay;
    }
}

