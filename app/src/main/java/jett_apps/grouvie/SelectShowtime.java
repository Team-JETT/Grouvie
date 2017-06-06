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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static jett_apps.grouvie.SelectFilm.CINEMA_MESSAGE;
import static jett_apps.grouvie.SelectFilm.FILM_MESSAGE;
import static jett_apps.grouvie.SelectFilm.LOCAL_DATA;
import static jett_apps.grouvie.SelectFilm.SHOWTIME_MESSAGE;

public class SelectShowtime extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_showtime);

        Intent intent = getIntent();
        final String filmTitle  = intent.getStringExtra(FILM_MESSAGE);
        final String cinemaName = intent.getStringExtra(CINEMA_MESSAGE);
        final String localData = intent.getStringExtra(LOCAL_DATA);
        ((TextView) findViewById(R.id.tutorial_text)).setText(filmTitle);


        final String[] showtimesArray = {"09:00", "10:12", "11:40", "13:35", "15:50", "17:05",
                "18:45", "19:18", "20:32", "21:00", "22:12", "23:02"};

        ListAdapter showtimeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, showtimesArray);
        ListView showtimeListView = (ListView) findViewById(R.id.timeList);
        showtimeListView.setAdapter(showtimeAdapter);

        showtimeListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String showTime = showtimesArray[position];

                    //Sending the current plan to the final planning page
                    Intent intent = new Intent(view.getContext(), LeaderInitialPlan.class);
                    intent.putExtra(FILM_MESSAGE, filmTitle);
                    intent.putExtra(CINEMA_MESSAGE, cinemaName);
                    intent.putExtra(SHOWTIME_MESSAGE, showTime);
                    startActivity(intent);

                    }
                }
        );

    }
}
