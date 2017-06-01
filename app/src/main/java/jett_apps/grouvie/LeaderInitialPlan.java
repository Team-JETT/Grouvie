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
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public void sendToGroup(View view) throws IOException {
        //TODO: Send initial/draft plan to web server to update the database
        //TODO: Send current plan to rest of the group
        enableStrictMode();
//        ServerContact sc = new ServerContact();
//        sc.doInBackground();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://129.31.228.213:5000/insert");
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
        String json_str = json.toString();
        StringEntity se = new StringEntity(json_str);
        httpPost.setEntity(se);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpClient.execute(httpPost);
        InputStream inputStream = httpResponse.getEntity().getContent();

        String result;
        if(inputStream != null) {
            result = inputStream.toString();
        } else {
            result = "Did not work!";
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

