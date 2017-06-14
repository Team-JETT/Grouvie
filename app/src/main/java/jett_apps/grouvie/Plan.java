package jett_apps.grouvie;


import java.io.Serializable;
import java.util.Arrays;

public class Plan implements Serializable {

    private String suggestedFilm;
    private String suggestedCinema;
    private String suggestedShowTime;
    private String suggestedDate;
    private String leaderPhoneNum;
    private String[] eventMembers;

    public Plan(String suggestedFilm, String suggestedCinema, String suggestedShowTime,
                String suggestedDate, String[] eventMembers, String leaderPhoneNum) {
        this.suggestedFilm = suggestedFilm;
        this.suggestedCinema = suggestedCinema;
        this.suggestedShowTime = suggestedShowTime;
        this.suggestedDate = suggestedDate;
        this.eventMembers = eventMembers;
        this.leaderPhoneNum = leaderPhoneNum;
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

    public String[] getEventMembers() {
        return eventMembers;
    }

    public void setEventMembers(String[] eventMembers) {
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
                 Arrays.equals(eventMembers, plan.eventMembers));
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
        result = 31 * result + Arrays.hashCode(eventMembers);
        return result;
    }

    public String getLeaderPhoneNum() {
        return leaderPhoneNum;
    }

    public void setLeaderPhoneNum(String leaderPhoneNum) {
        this.leaderPhoneNum = leaderPhoneNum;
    }
}

