package jett_apps.grouvie.HelperObjects;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Film implements Serializable, Comparable<Film> {

    private final String filmName, imageUrl, overview;

    public Film(String filmName, String imageUrl, String overview) {
        this.filmName = filmName;
        this.imageUrl = imageUrl;
        this.overview = overview;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int compareTo(@NonNull Film o) {
        return getFilmName().compareTo(o.getFilmName());
    }
}
