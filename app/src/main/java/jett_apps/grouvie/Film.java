package jett_apps.grouvie;

public class Film {

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
