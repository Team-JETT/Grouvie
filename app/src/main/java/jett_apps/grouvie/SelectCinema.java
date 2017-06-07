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
import java.util.Iterator;

import static jett_apps.grouvie.LandingPage.CINEMA_MESSAGE;
import static jett_apps.grouvie.LandingPage.CINEMA_DATA;
import static jett_apps.grouvie.LandingPage.DAY_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.GROUP_LIST;

public class SelectCinema extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cinema);

        Intent intent = getIntent();
        final String chosenFilm = intent.getStringExtra(FILM_MESSAGE);
        final String chosenDay = intent.getStringExtra(DAY_MESSAGE);
        final String cinemaData = intent.getStringExtra(CINEMA_DATA);
        final String chosenFriends = intent.getStringExtra(GROUP_LIST);
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
//                {"Cineworld - Fulham Road", "Vue - Shepard's Bush", "Odeon"};

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
                    try {
                        JSONObject chosenCinemaData = (JSONObject) finalCinema_data.get(position);
                        Log.v("CINEMA DATA", chosenCinemaData.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Sending the current plan to the final planning page
                    Intent intent = new Intent(view.getContext(), SelectShowtime.class);
                    intent.putExtra(FILM_MESSAGE, chosenFilm);
                    intent.putExtra(DAY_MESSAGE, chosenDay);
                    intent.putExtra(CINEMA_MESSAGE, chosenCinema);
                    intent.putExtra(GROUP_LIST, chosenFriends);
                    startActivity(intent);

                }
            }
        );

    }
}
