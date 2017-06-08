package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static jett_apps.grouvie.LandingPage.CINEMA_DATA;
import static jett_apps.grouvie.LandingPage.CINEMA_MESSAGE;
import static jett_apps.grouvie.LandingPage.DAY_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.SHOWTIME_MESSAGE;
import static jett_apps.grouvie.LandingPage.SHOWTIME_DISTANCE_DATA;
import static jett_apps.grouvie.LandingPage.USER_NAME;

public class SelectShowtime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_showtime);

        Intent intent = getIntent();
        final String chosenFilm  = intent.getStringExtra(FILM_MESSAGE);
        final String chosenCinema = intent.getStringExtra(CINEMA_MESSAGE);
        final String chosenDay = intent.getStringExtra(DAY_MESSAGE);
        final String showtimeDistanceData = intent.getStringExtra(SHOWTIME_DISTANCE_DATA);
        ((TextView) findViewById(R.id.chosenFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.chosenCinema)).setText(chosenCinema);


        // Convert the string to JSONArray.
        JSONObject showtime_distance_data = null;
        try {
            Log.v("CINEMA DATA", showtimeDistanceData);
            showtime_distance_data = (new JSONArray(showtimeDistanceData)).getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> showtimes = new ArrayList<>();
        try {
            JSONArray showtimesJSON = showtime_distance_data.getJSONArray("showtimes");
            for (int i = 0; i < showtimesJSON.length(); ++i) {
                showtimes.add(showtimesJSON.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter showtimeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, showtimes);
        ListView showtimeListView = (ListView) findViewById(R.id.timeList);
        showtimeListView.setAdapter(showtimeAdapter);

        showtimeListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String chosenTime = showtimes.get(position);
                    String user_name = getIntent().getStringExtra(USER_NAME);

                    //Sending the current plan to the final planning page
                    Intent intent = new Intent(view.getContext(), LeaderInitialPlan.class);
                    intent.putExtra(FILM_MESSAGE, chosenFilm);
                    intent.putExtra(CINEMA_MESSAGE, chosenCinema);
                    intent.putExtra(DAY_MESSAGE, chosenDay);
                    intent.putExtra(SHOWTIME_MESSAGE, chosenTime);
                    intent.putExtra(USER_NAME, user_name);
//                    intent.putExtra(SHOWTIME_DISTANCE_DATA, totalDistance);
                    startActivity(intent);

                    }
                }
        );

    }
}
