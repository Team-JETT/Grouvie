package jett_apps.grouvie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static jett_apps.grouvie.LandingPage.DAY;
import static jett_apps.grouvie.LandingPage.LATITUDE;
import static jett_apps.grouvie.LandingPage.LONGITUDE;
import static jett_apps.grouvie.LandingPage.MONTH;
import static jett_apps.grouvie.LandingPage.YEAR;
import static jett_apps.grouvie.LandingPage.DAY_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.CINEMA_DATA;
import static jett_apps.grouvie.LandingPage.USER_NAME;


public class SelectFilm extends AppCompatActivity implements LocationListener {

    Location location;
    double latitude = 51.499074;
    double longitude = -0.177070;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_film);

        obtainLocation();

        final JSONObject local_data = getLocalData();

        final ArrayList<String> films = new ArrayList<>();
        Iterator<String> iter = local_data.keys();
        while (iter.hasNext()) {
            films.add(iter.next());
        }


        ListAdapter filmAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, films);
        ListView filmsListView = (ListView) findViewById(R.id.filmList);
        filmsListView.setAdapter(filmAdapter);

        ServerContact.dialog.dismiss();

        filmsListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filmTitle = films.get(position);
                    Log.v("CHOSEN FILM", filmTitle);
                    JSONArray cinema_data = null;
                    try {
                        cinema_data = local_data.getJSONArray(filmTitle);
                        Log.v("CINEMA DATA", cinema_data.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final JSONArray cinemaData = cinema_data;

                    Intent intent = new Intent(view.getContext(), SelectGroup.class);
                    intent.putExtra(LATITUDE, latitude);
                    intent.putExtra(LONGITUDE, longitude);
                    intent.putExtra(FILM_MESSAGE, filmTitle);
                    intent.putExtra(DAY_MESSAGE, getIntent().getStringExtra(DAY_MESSAGE));
                    intent.putExtra(CINEMA_DATA, cinemaData.toString());
                    intent.putExtra(USER_NAME, getIntent().getStringExtra(USER_NAME));
                    startActivity(intent);
                }
            }
        );

    }

    @Nullable
    private JSONObject getLocalData() {
        // Grab the date from the MainActivity
        Intent intent = getIntent();
        final int day = intent.getIntExtra(DAY, 0);
        final int month = intent.getIntExtra(MONTH, 0);
        final int year = intent.getIntExtra(YEAR, 1900);

        JSONObject json = new JSONObject();
        try {
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
            json.accumulate("day", day);
            json.accumulate("month", month);
            json.accumulate("year", year);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            result = new ServerContact().execute("get_local_data", json.toString()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject local_data = null;
        try {
            if (result == null) {
                Log.e("DANK MEMES", "Failed to get anything back from web server.");
            }
            Log.e("DANK MEMES", result);
            local_data = new JSONObject(result);
            Log.v("DANK MEMES:", local_data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return local_data;
    }

    private void obtainLocation() {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 8);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 8);
        }


        location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
        } else {
            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                onLocationChanged(location);
            }

        }

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //Print out location message for debugging purposes
//        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
