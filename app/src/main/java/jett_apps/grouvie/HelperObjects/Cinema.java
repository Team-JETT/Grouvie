package jett_apps.grouvie.HelperObjects;

import com.google.android.gms.maps.model.LatLng;

public class Cinema {

    private String name;
    private LatLng location;

    public Cinema() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }
}
