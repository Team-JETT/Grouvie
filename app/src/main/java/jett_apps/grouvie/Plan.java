package jett_apps.grouvie;


public class Plan {

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
}

