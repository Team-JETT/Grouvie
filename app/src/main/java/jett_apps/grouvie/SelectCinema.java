package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static jett_apps.grouvie.SelectDay.CINEMA_MESSAGE;
import static jett_apps.grouvie.SelectDay.DAY_MESSAGE;
import static jett_apps.grouvie.SelectDay.FILM_MESSAGE;

public class SelectCinema extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cinema);

        Intent intent = getIntent();
        final String chosenFilm = intent.getStringExtra(FILM_MESSAGE);
        final String chosenDay = intent.getStringExtra(DAY_MESSAGE);
        ((TextView) findViewById(R.id.chosen_film)).setText(chosenFilm);

        //TODO: Obtain from web server
        final String[] cinemasArray = {"Cineworld - Fulham Road", "Vue - Shepard's Bush",
                "Odeon"};

        ListAdapter showtimeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cinemasArray);
        ListView showtimeListView = (ListView) findViewById(R.id.cinemaList);
        showtimeListView.setAdapter(showtimeAdapter);

        showtimeListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String chosenCinema = cinemasArray[position];

                    //Sending the current plan to the final planning page
                    Intent intent = new Intent(view.getContext(), SelectShowtime.class);
                    intent.putExtra(FILM_MESSAGE, chosenFilm);
                    intent.putExtra(DAY_MESSAGE, chosenDay);
                    intent.putExtra(CINEMA_MESSAGE, chosenCinema);
                    startActivity(intent);

                }
            }
        );

    }
}
