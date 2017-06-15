package jett_apps.grouvie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static jett_apps.grouvie.LandingPage.PLAN_MESSAGE;

public class SuggestChange extends AppCompatActivity {

    Plan p;
    String chosenDate;
    String chosenTime;
    String chosenFilm;
    String chosenCinema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_change);

        p = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);

        chosenFilm = p.getSuggestedFilm();
        chosenDate = p.getSuggestedDate();
        chosenTime = p.getSuggestedShowTime();
        chosenCinema = p.getSuggestedCinema();

        TextView film = (TextView) findViewById(R.id.SelectedFilm);
        film.setText(chosenFilm);
        film.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestChange.this, SelectFilm.class);
                //Small change

            }
        });

        TextView cinema = (TextView) findViewById(R.id.SelectedCinema);
        cinema.setText(chosenCinema);
        cinema.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        TextView time = (TextView) findViewById(R.id.SelectedShowtime);
        time.setText(chosenTime);
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        TextView day = (TextView) findViewById(R.id.SelectedDay);
        day.setText(chosenDate);
        day.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

    }
}
