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
import static jett_apps.grouvie.LandingPage.DATE_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.GROUP_LIST;
import static jett_apps.grouvie.LandingPage.SHOWTIME_MESSAGE;
import static jett_apps.grouvie.LandingPage.USER_NAME;

public class LeaderInitialPlan extends AppCompatActivity {

    private double latitude, longitude;
    private String chosenFilm, chosenCinema, chosenTime, chosenDay;
    private String[] chosenGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        Intent intent = getIntent();
//        latitude = intent.getDoubleExtra(LATITUDE, 0);
//        longitude = intent.getDoubleExtra(LONGITUDE, 0);
        chosenFilm = intent.getStringExtra(FILM_MESSAGE);
        chosenCinema = intent.getStringExtra(CINEMA_MESSAGE);
        chosenTime = intent.getStringExtra(SHOWTIME_MESSAGE);
        chosenDay = intent.getStringExtra(DATE_MESSAGE);
        chosenGroup = intent.getStringArrayExtra(GROUP_LIST);

        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDay);

    }

    public void sendToGroup(View view) throws IOException {

        JSONObject json = new JSONObject();
        try {
            json.accumulate("PHONE_NUMBER", "1");
            json.accumulate("LEADER", 0);
            json.accumulate("SHOWTIME", chosenTime);
            json.accumulate("FILM", chosenFilm);
            json.accumulate("CINEMA", chosenCinema);
            json.accumulate("LATITUDE", latitude);
            json.accumulate("LONGITUDE", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TODO: Send initial/draft plan to web server to update the database
        //TODO: Send current plan to rest of the group
        new ServerContact().execute("make_plan", json.toString());


        String leaderPhoneNum = ProfileManager.getPhone(LeaderInitialPlan.this);
        Plan p = new Plan(chosenFilm, chosenCinema, chosenTime, chosenDay, chosenGroup,
                            leaderPhoneNum);
        CurrentPlans.addPlan(p, LeaderInitialPlan.this);

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        String user_name = getIntent().getStringExtra(USER_NAME);
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra(USER_NAME, user_name);
        startActivity(intent);
    }
}

