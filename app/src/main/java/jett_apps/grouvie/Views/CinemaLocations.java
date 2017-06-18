package jett_apps.grouvie.Views;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.PlanningActivities.SelectShowtime;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class CinemaLocations extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Plan data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//--------------------------------------------------------------------------------------------------
        data = (Plan) getIntent().getSerializableExtra(DATA);

        final String chosenFilm = data.getSuggestedFilm();
        final String cinemaData = data.getCinemaData();

        ((TextView) findViewById(R.id.chosen_film)).setText(chosenFilm);

        JSONArray cinema_data = null;
        try {
            Log.v("CINEMA DATA", cinemaData);
            cinema_data = new JSONArray(cinemaData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // List of all cinemas
        final ArrayList<String> cinemas = new ArrayList<>();
        // Loop to extract all cinemas from the JSONArray
        for (int i = 0; i < cinema_data.length(); ++i) {
            try {
                JSONObject cinema = cinema_data.getJSONObject(i);
                Iterator<String> iter = cinema.keys();
                while (iter.hasNext()) {
                    cinemas.add(iter.next());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ListAdapter showtimeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cinemas);
        ListView showtimeListView = (ListView) findViewById(R.id.cinemaList);
        showtimeListView.setAdapter(showtimeAdapter);

        final JSONArray finalCinema_data = cinema_data;
        showtimeListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String chosenCinema = cinemas.get(position);
                        Log.v("CHOSEN CINEMA", chosenCinema);
                        JSONArray showtimeDistanceData = null;
                        try {
                            // For our chosen chosenCinema get the showtimes and distance to the chosenCinema.
                            showtimeDistanceData = ((JSONObject) finalCinema_data.get(position)).
                                    getJSONArray(chosenCinema);
                            Log.v("CHOSEN CINEMA DATA", cinemaData.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Sending the current plan to the final planning page
                        Intent intent = new Intent(view.getContext(), SelectShowtime.class);

                        data.setSuggestedCinema(chosenCinema);
                        data.setShowtimeDistance(showtimeDistanceData.toString());

                        intent.putExtra(DATA, data);
                        startActivity(intent);

                    }
                }
        );
//--------------------------------------------------------------------------------------------------
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
