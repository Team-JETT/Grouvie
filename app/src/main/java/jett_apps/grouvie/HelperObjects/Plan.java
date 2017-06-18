package jett_apps.grouvie.HelperObjects;


import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {

    private String suggestedFilm;
    private String suggestedCinema;
    private String suggestedShowTime;
    private String leaderPhoneNum;
    private ArrayList<Friend> eventMembers;
    private String cinemaData;
    private String showtimeDistance;

    private String suggestedDate;
    private int suggestedDay;
    private int suggestedMonth;
    private int suggestedYear;

    private ArrayList<Film> listOfFilms;

    public Plan(String suggestedFilm, String suggestedCinema, String suggestedShowTime,
                String suggestedDate, ArrayList<Friend> eventMembers, String leaderPhoneNum) {
        this.suggestedFilm = suggestedFilm;
        this.suggestedCinema = suggestedCinema;
        this.suggestedShowTime = suggestedShowTime;
        this.suggestedDate = suggestedDate;
        this.eventMembers = eventMembers;
        this.leaderPhoneNum = leaderPhoneNum;
    }

    public Plan() {

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
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        // My 2p - I think this is okay. Btw, have rewritten in a more succinct way.
    }

    @Override
    public int hashCode() {
        // TODO: Someone explain to Erkin why 31?
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
}

