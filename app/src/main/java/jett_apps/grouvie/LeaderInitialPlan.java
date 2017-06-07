package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import static jett_apps.grouvie.LandingPage.CINEMA_MESSAGE;
import static jett_apps.grouvie.LandingPage.DAY_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.GROUP_LIST;
import static jett_apps.grouvie.LandingPage.SHOWTIME_MESSAGE;

public class LeaderInitialPlan extends AppCompatActivity {

    private String chosenFilm;
    private String chosenCinema;
    private String chosenTime;
    private String chosenDay;
    private String[] chosenFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        Intent intent = getIntent();
        chosenFilm = intent.getStringExtra(FILM_MESSAGE);
        chosenCinema = intent.getStringExtra(CINEMA_MESSAGE);
        chosenTime = intent.getStringExtra(SHOWTIME_MESSAGE);
        chosenDay = intent.getStringExtra(DAY_MESSAGE);
        chosenFriends = intent.getStringArrayExtra(GROUP_LIST);

        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDay);

    }

    public void sendToGroup(View view) throws IOException {
        //TODO: Send initial/draft plan to web server to update the database
        //TODO: Send current plan to rest of the group

        JSONObject json = new JSONObject();
        try {
            json.accumulate("PHONE_NUMBER", "1");
            json.accumulate("GROUP_ID", 0);
            json.accumulate("SHOWTIME", "s");
            json.accumulate("FILM", "GOTG3");
            json.accumulate("PRICE", 32.22);
            json.accumulate("LOCATION_LAT", 52.111100);
            json.accumulate("LOCATION_LONG", 21.211122);
            json.accumulate("IMAGE", "HTTP");
            json.accumulate("IS_LEADER", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ServerContact().execute("insert", json.toString());

        Plan p = new Plan(chosenFilm, chosenCinema, chosenTime, chosenDay, chosenFriends);
        CurrentPlans.addPlan(p, LeaderInitialPlan.this);

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
    }
}

