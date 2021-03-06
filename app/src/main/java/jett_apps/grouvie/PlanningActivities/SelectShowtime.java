package jett_apps.grouvie.PlanningActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.LeaderInitialPlan;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class SelectShowtime extends AppCompatActivity {

    private Plan data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_showtime);

        data = (Plan) getIntent().getSerializableExtra(DATA);

        final String chosenFilm = data.getSuggestedFilm();
        final String chosenCinema = data.getSuggestedCinema();
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

        ListAdapter showtimeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, showtimes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };

        ListView showtimeListView = (ListView) findViewById(R.id.timeList);
        showtimeListView.setAdapter(showtimeAdapter);

        showtimeListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String chosenTime = showtimes.get(position);

                    Intent intent;

                    //Decide if this plan is leader's initial plan or a response
                    if (data.isInitialPlan()) {

                        //Sending the current plan to the final planning page
                        intent = new Intent(view.getContext(), LeaderInitialPlan.class);

                    } else {

                        //Go back to suggest change in plan activity
                        intent = new Intent(view.getContext(), SuggestChangeInPlan.class);

                    }

                    data.setSuggestedShowTime(chosenTime);

                    intent.putExtra(DATA, data);
                    startActivity(intent);

                }
            }
        );

    }
}
