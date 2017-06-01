package jett_apps.grouvie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        enableStrictMode();
//        ServerContact sc = new ServerContact();
//        sc.doInBackground();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://129.31.228.213:5000/insert");
        JSONObject json = new JSONObject();
        try {
            json.put("PHONE_NUMBER", "1");
            json.put("GROUP_ID", 0);
            json.put("SHOWTIME", "s");
            json.put("FILM", "GOTG3");
            json.put("PRICE", 32.22);
            json.put("LOCATION_LAT", 52.111100);
            json.put("LOCATION_LONG", 21.211122);
            json.put("IMAGE", "HTTP");
            json.put("IS_LEADER", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

}

