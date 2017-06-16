package jett_apps.grouvie;

import java.io.Serializable;

public class Film implements Serializable {

    private final String filmName, imageUrl;

    public Film(String filmName, String imageUrl) {
        this.filmName = filmName;
        this.imageUrl = imageUrl;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
