package jett_apps.grouvie.Views;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperObjects.Cinema;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.PlanningActivities.SelectShowtime;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class CinemaLocations extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Plan data;
    private ArrayList<Cinema> cinemaList;
    private ArrayList<String> cinemas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        data = (Plan) getIntent().getSerializableExtra(DATA);
        cinemaList = new ArrayList<>();
        final String cinemaData = data.getCinemaData();

        for (int i =0; i < cinemas.size(); i++) {
            String cinemaName = cinemas.get(i);
            LatLng location = getLatLngFromLocationName(this, cinemaName);
            Cinema cinema = new Cinema();

            cinema.setName(cinemaName);
            cinema.setLocation(location);

            cinemaList.add(cinema);
        }
    }

    public LatLng getLatLngFromLocationName(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng position = null;

        try {
            addressList = geocoder.getFromLocationName(address, 5);
            if(addressList == null) {
                return null;
            }
            Address location = addressList.get(0);
            location.getLongitude();
            location.getLatitude();
            position = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String postcode = ProfileManager.getPostcode(CinemaLocations.this);
        LatLng currentLocation = getLatLngFromLocationName(CinemaLocations.this, postcode);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(currentLocation);

        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(CinemaLocations.this, SelectShowtime.class);
                startActivity(intent);
                return true;
            }
        });

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < cinemaList.size(); i++) {

            Cinema cinema = cinemaList.get(i);
            LatLng position = cinema.getLocation();
            String cinemaName = cinema.getName();

            mMap.addMarker(new MarkerOptions().position(position).title(cinemaName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            builder.include(position);
        }

        LatLngBounds bounds = builder.build();
//
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen
//
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
//
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//        mMap.animateCamera(cu);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You"));
    }
}
