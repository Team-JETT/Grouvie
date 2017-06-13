package jett_apps.grouvie;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

class ServerContact extends AsyncTask<String, Integer, String> {

    static ProgressDialog dialog;
//    Debug IP
    final static String WebServerAddr = "192.168.1.70";
//    final static String WebServerAddr = "https://murmuring-peak-87447.herokuapp.com/";

    /*
     * HOW TO USE PARAMS:
     * params[0] = URL to send data to.
     * params[1] = String data to POST to that URL.
     */
    @Override
    protected String doInBackground(String... params) {

        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(WebServerAddr + params[0]);
        HttpPost httpPost = new HttpPost("http://" + WebServerAddr + ":5000/" + params[0]);

        StringEntity se = null;
        try {
            // We only ever pass in 1 string so grab the first element in the array.
            se = new StringEntity(params[1]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(se);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse httpResponse;
        InputStream inputStream = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (inputStream != null) ?
                convertStreamToString(inputStream) : "Did not work!";
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
