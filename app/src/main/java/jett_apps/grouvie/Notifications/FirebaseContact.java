package jett_apps.grouvie.Notifications;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class FirebaseContact extends AsyncTask<String, Integer, String> {

    final static String WebServerAddr = "https://fcm.googleapis.com/fcm/send";

    /*
     * HOW TO USE PARAMS:
     * params[0] = Topic (Phone number) to send data to.
     * params[1] = Plan in JSON format being sent to the user.
     */
    @Override
    protected String doInBackground(String... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(WebServerAddr);

        String topicName = params[0];
        String planInJSON = params[1];
        JSONObject plan = null;

        try {
            plan = new JSONObject(planInJSON);
        } catch (JSONException e) {
            Log.e("BAD JSON", "params[1] was not a JSON Object!");
            e.printStackTrace();
        }

        JSONObject notification = new JSONObject();

        try {
            notification.accumulate("to", "/topics/" + topicName);
            notification.accumulate("data", plan);
        } catch (JSONException e) {
            Log.e("UNLUCKY", "Could not create/accumulate JSON Object!");
            e.printStackTrace();
        }
        Log.v("SEND PLAN:", notification.toString());

        try {
            // We only ever pass in 1 string so grab the first element in the array.
            httpPost.setEntity(new StringEntity(notification.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setHeader("Content-type", "application/json");
        String auth_key = "AAAA6_Kxqhk:APA91bHRmBQFRxZt2Y93FV8MRYG92EOz3E4bEvmOTU49YM" +
                "vkgLUd0ddufcoRiFv_IbGPqPgjSitRgF8dZD1nQPb48zhC0gedcfQ-YUtPUJh" +
                "7z9CWcsF58VueXZmKU7MDhDX5nsW867Gg";
        httpPost.setHeader("Authorization", "key=" + auth_key);

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
