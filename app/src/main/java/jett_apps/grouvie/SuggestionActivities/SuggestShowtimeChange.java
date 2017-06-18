package jett_apps.grouvie.SuggestionActivities;

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

import jett_apps.grouvie.HelperObjects.PropagationObject;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.CHANGED_PLAN_MESSAGE;

public class SuggestShowtimeChange extends AppCompatActivity {

    private PropagationObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_showtime_change);

        data = (PropagationObject) getIntent().getSerializableExtra(CHANGED_PLAN_MESSAGE);

        final String chosenFilm = data.getFilmTitle();
        final String chosenCinema = data.getCinema();
        final String showtimeDistanceData = data.getShowtimeDistance();

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

                        //Sending the current plan to the final planning page
                        Intent intent = new Intent(view.getContext(), SuggestChangeInPlan.class);

                        data.setChosenTime(chosenTime);

                        intent.putExtra(CHANGED_PLAN_MESSAGE, data);

                        startActivity(intent);
                    }
                }
        );


    }
}
