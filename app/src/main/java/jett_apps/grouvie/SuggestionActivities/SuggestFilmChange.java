package jett_apps.grouvie.SuggestionActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import jett_apps.grouvie.Adapters.CustomFilmAdapter;
import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Film;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.HelperObjects.PropagationObject;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.CHANGED_PLAN_MESSAGE;

public class SuggestFilmChange extends AppCompatActivity {

    private Plan p;
    private PropagationObject suggestedPlanData;
    private ArrayList<Film> films;
    double latitude = 51.499074;
    double longitude = -0.177070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_film_change);

        Intent intent = getIntent();
        suggestedPlanData = (PropagationObject) intent.getSerializableExtra(CHANGED_PLAN_MESSAGE);


        //TODO: Get rid of massive duplication
        final JSONObject local_data = getLocalData();
        Log.v("LOCAL DATA:", local_data.toString());

        films = new ArrayList<>();
        Iterator<String> iter = local_data.keys();
        while (iter.hasNext()) {
            String filmName = iter.next();
            String imageUrl = "https://literalminded.files.wordpress.com/2010/11/image-unavailable1.png";
            try {
                imageUrl = local_data.getJSONObject(filmName).get("image").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            films.add(new Film(filmName, imageUrl));
        }

        ListAdapter filmAdapter = new CustomFilmAdapter(SuggestFilmChange.this, films);
        ListView filmsListView = (ListView) findViewById(R.id.storedFilmList);
        filmsListView.setAdapter(filmAdapter);

        ServerContact.dialog.dismiss();

        filmsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String filmTitle = films.get(position).getFilmName();

                        Log.v("CHOSEN FILM", filmTitle);
                        JSONArray cinema_data = null;
                        try {
                            cinema_data = local_data.getJSONObject(filmTitle).getJSONArray("cinema");
                            Log.v("CINEMA DATA", cinema_data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final JSONArray cinemaData = cinema_data;

                        Intent intent = new Intent(view.getContext(), SuggestCinemaChange.class);
                        suggestedPlanData.setFilmTitle(filmTitle);
                        suggestedPlanData.setCinemaData(cinemaData.toString());
                        intent.putExtra(CHANGED_PLAN_MESSAGE, suggestedPlanData);
                        startActivity(intent);
                    }
                }
        );
    }

    private JSONObject getLocalData() {
        // Grab the chosenDate from the MainActivity
        final int day = suggestedPlanData.getDay();
        final int month = suggestedPlanData.getMonth();
        final int year = suggestedPlanData.getYear();

        JSONObject json = new JSONObject();
        try {
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
            json.accumulate("day", day);
            json.accumulate("month", month);
            json.accumulate("year", year);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            result = new ServerContact().execute("get_local_data", json.toString()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject local_data = null;
        try {
            if (result == null) {
                Log.e("DANK MEMES", "Failed to get anything back from web server.");
            }
            Log.e("DANK MEMES", result);
            local_data = new JSONObject(result);
            Log.v("DANK MEMES:", local_data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return local_data;
    }
}
