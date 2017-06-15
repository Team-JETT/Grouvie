package jett_apps.grouvie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static jett_apps.grouvie.LandingPage.PLAN_MESSAGE;
import static jett_apps.grouvie.R.id.currentFilm;

public class SuggestChangeInPlan extends AppCompatActivity {

    private Plan p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_change_in_plan);

        p = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);

        TextView film = (TextView) findViewById(R.id.currentFilm);
        film.setText(p.getSuggestedFilm());

        TextView date = (TextView) findViewById(R.id.currentDate);
        date.setText(p.getSuggestedDate());

        TextView time = (TextView) findViewById(R.id.currentShowtime);
        time.setText(p.getSuggestedShowTime());

        TextView cinema = (TextView) findViewById(R.id.currentCinema);
        cinema.setText(p.getSuggestedCinema());
    }
}
