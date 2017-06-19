package jett_apps.grouvie.HelperObjects;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Film implements Serializable {

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
    
}
