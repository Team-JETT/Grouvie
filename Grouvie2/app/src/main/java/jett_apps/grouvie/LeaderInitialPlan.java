package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static jett_apps.grouvie.SelectFilm.CINEMA_MESSAGE;
import static jett_apps.grouvie.SelectFilm.FILM_MESSAGE;
import static jett_apps.grouvie.SelectFilm.SHOWTIME_MESSAGE;

public class LeaderInitialPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        Intent intent = getIntent();
        final String filmTitle = intent.getStringExtra(FILM_MESSAGE);
        final String cinemaName = intent.getStringExtra(CINEMA_MESSAGE);
        final String showtime = intent.getStringExtra(SHOWTIME_MESSAGE);

        ((TextView) findViewById(R.id.SelectedFilm)).setText(filmTitle);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(cinemaName);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(showtime);


    }

    public void sendToGroup(View view) {
        //TODO: Send initial/draft plan to web server to update the database
        //TODO: Send current plan to rest of the group
        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
