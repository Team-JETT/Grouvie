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

import static jett_apps.grouvie.MainActivity.CINEMA_MESSAGE;
import static jett_apps.grouvie.MainActivity.DAY_MESSAGE;
import static jett_apps.grouvie.MainActivity.FILM_MESSAGE;
import static jett_apps.grouvie.MainActivity.SHOWTIME_MESSAGE;

public class LeaderInitialPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        Intent intent = getIntent();
        final String chosenFilm = intent.getStringExtra(FILM_MESSAGE);
        final String chosenCinema = intent.getStringExtra(CINEMA_MESSAGE);
        final String chosenTime = intent.getStringExtra(SHOWTIME_MESSAGE);
        final String chosenDay = intent.getStringExtra(DAY_MESSAGE);

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

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

