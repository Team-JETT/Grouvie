package jett_apps.grouvie;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ServerContact extends AsyncTask<JSONObject, Void, Void> {

    @Override
    protected Void doInBackground(JSONObject... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/get_films");
        try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        JSONObject json = new JSONObject();
//        try {
//            json.put("PHONE_NUMBER", "1");
//            json.put("GROUP_ID", 0);
//            json.put("SHOWTIME", "s");
//            json.put("FILM", "GOTG3");
//            json.put("PRICE", 32.22);
//            json.put("LOCATION_LAT", 52.111100);
//            json.put("LOCATION_LONG", 21.211122);
//            json.put("IMAGE", "HTTP");
//            json.put("IS_LEADER", "0");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            HttpResponse response = httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

}