package jett_apps.grouvie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SelectFilm extends AppCompatActivity implements LocationListener {

    public static final String EXTRA_MESSAGE = "FILMTITLE";
    Location location;
    double latitude = 51.499074;
    double longitude = -0.177070;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_film);

        final String[] showingFilmsArray = {"Guardians of the Galaxy Vol 2",
                "The Fate of the Furious",
                "Boss Baby",
                "WonderWoman",
                "Baywatch",
                "Alien: Covenant",
                "Beauty and the Beast",
                "Lion",
                "Pirates of the Caribbean"};

        ListAdapter filmAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, showingFilmsArray);
        ListView filmsListView = (ListView) findViewById(R.id.filmList);
        filmsListView.setAdapter(filmAdapter);

        //TODO: Obtain location (latitude and longitude)

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
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

        filmsListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filmTitle = showingFilmsArray[position];
                    Intent intent = new Intent(view.getContext(), SelectShowtime.class);
                    intent.putExtra(EXTRA_MESSAGE, filmTitle);
                    startActivity(intent);
                }
            }
        );

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
