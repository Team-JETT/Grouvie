package jett_apps.grouvie.HelperObjects;

import java.io.Serializable;

public class PlanChange implements Serializable {

    private String filmTitle;
    private String time;
    private String date;
    private String cinema;

    public PlanChange() {

    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public String getFilmTitle() {
        return filmTitle;
    }
}
