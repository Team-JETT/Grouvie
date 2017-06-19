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

        /* Retrieve and initialise variables. */
        String topicName = params[0];
        String planInJSON = params[1];
        JSONObject plan = null;

        /* Initialise plan with the JSON string planInJSON. */
        try {
            plan = new JSONObject(planInJSON);
        } catch (JSONException e) {
            Log.e("BAD JSON", "params[1] was not a JSON Object!");
            e.printStackTrace();
        }

        /* Create JSON to send to the user. */
        JSONObject notification = new JSONObject();

        try {
            /* This sends the notification to the user's phone number, which they will be
               subscribed to once they've gone through the LandingPage. */
            notification.accumulate("to", "/topics/" + topicName);
            /* This sends the plan to the user. */
            notification.accumulate("data", plan);
        } catch (JSONException e) {
            Log.e("UNLUCKY", "Could not create/accumulate JSON Object!");
            e.printStackTrace();
        }
        Log.v("SEND PLAN:", notification.toString());

        try {
            /* We only need to pass the notification JSON in the body of the httpPost. */
            httpPost.setEntity(new StringEntity(notification.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /* Set headers for httpPost. Authorization key is necessary to send a message through
           the WebServerAddr above. */
        httpPost.setHeader("Content-type", "application/json");
        String auth_key = "AAAA6_Kxqhk:APA91bHRmBQFRxZt2Y93FV8MRYG92EOz3E4bEvmOTU49YM" +
                "vkgLUd0ddufcoRiFv_IbGPqPgjSitRgF8dZD1nQPb48zhC0gedcfQ-YUtPUJh" +
                "7z9CWcsF58VueXZmKU7MDhDX5nsW867Gg";
        httpPost.setHeader("Authorization", "key=" + auth_key);

        /* Send the httpPost to the user and retrieve the response body. */
        HttpResponse httpResponse;
        InputStream inputStream = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Return the result of the http request. */
        return (inputStream != null) ?
                convertStreamToString(inputStream) : "Did not work!";
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
