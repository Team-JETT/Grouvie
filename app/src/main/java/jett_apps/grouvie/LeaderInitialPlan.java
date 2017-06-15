package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static jett_apps.grouvie.LandingPage.CINEMA_MESSAGE;
import static jett_apps.grouvie.LandingPage.DATA;
import static jett_apps.grouvie.LandingPage.DATE_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.GROUP_LIST;
import static jett_apps.grouvie.LandingPage.SHOWTIME_MESSAGE;
import static jett_apps.grouvie.LandingPage.USER_NAME;

public class LeaderInitialPlan extends AppCompatActivity {

    private double latitude, longitude;
    private String chosenFilm, chosenCinema, chosenTime, chosenDay;
    private String[] chosenGroup;

    private PropogationObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        data = (PropogationObject) getIntent().getSerializableExtra(DATA);

//        Intent intent = getIntent();
//        latitude = intent.getDoubleExtra(LATITUDE, 0);
//        longitude = intent.getDoubleExtra(LONGITUDE, 0);
//        chosenFilm = intent.getStringExtra(FILM_MESSAGE);
//        chosenCinema = intent.getStringExtra(CINEMA_MESSAGE);
//        chosenTime = intent.getStringExtra(SHOWTIME_MESSAGE);
//        chosenDay = intent.getStringExtra(DATE_MESSAGE);
//        chosenGroup = intent.getStringArrayExtra(GROUP_LIST);

        chosenFilm = data.getFilmTitle();
        chosenCinema = data.getCinemaData();
        chosenTime = data.getChosenTime();
        chosenDay = data.getDate();
        chosenGroup = data.getSelectedFriends();

        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDay);

    }

    public void sendToGroup(View view) throws IOException {

        JSONObject json = new JSONObject();
        String leaderPhoneNum = ProfileManager.getPhone(LeaderInitialPlan.this);
        try {
            json.accumulate("phone_number", leaderPhoneNum);
            json.accumulate("leader", leaderPhoneNum);
            json.accumulate("showtime", chosenTime);
            json.accumulate("film", chosenFilm);
            json.accumulate("cinema", chosenCinema);
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("MAKE PLAN:", json.toString());
        // Send initial/draft plan to web server to update the database
        new ServerContact().execute("make_plan", json.toString());


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

