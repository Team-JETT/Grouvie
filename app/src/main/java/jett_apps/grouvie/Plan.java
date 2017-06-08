package jett_apps.grouvie;


import java.io.Serializable;
import java.util.Arrays;

public class Plan implements Serializable {

    private String suggestedFilm;
    private String suggestedCinema;
    private String suggestedShowTime;
    private String suggestedDate;
    private String[] eventMembers;

    public Plan(String suggestedFilm, String suggestedCinema, String suggestedShowTime,
                String suggestedDate, String[] eventMembers) {
        this.suggestedFilm = suggestedFilm;
        this.suggestedCinema = suggestedCinema;
        this.suggestedShowTime = suggestedShowTime;
        this.suggestedDate = suggestedDate;
        this.eventMembers = eventMembers;
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

        if (!suggestedFilm.equals(plan.suggestedFilm)) return false;
        if (!suggestedCinema.equals(plan.suggestedCinema)) return false;
        if (!suggestedShowTime.equals(plan.suggestedShowTime)) return false;
        if (!suggestedDate.equals(plan.suggestedDate)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(eventMembers, plan.eventMembers);

    }

    @Override
    public int hashCode() {
        int result = suggestedFilm.hashCode();
        result = 31 * result + suggestedCinema.hashCode();
        result = 31 * result + suggestedShowTime.hashCode();
        result = 31 * result + suggestedDate.hashCode();
        result = 31 * result + Arrays.hashCode(eventMembers);
        return result;
    }
}

