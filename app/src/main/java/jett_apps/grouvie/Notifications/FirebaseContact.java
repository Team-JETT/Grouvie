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

    public static final int SEND_PLAN_TO_GROUP = 1;
    public static final int SUGGEST_CHANGE_TO_LEADER = 2;


    /*
     * HOW TO USE PARAMS:
     * params[0] = Type of message you want to send
     * params[1] = Topic (Phone number) to send data to.
     * params[2] = Notification message you want to send.
     * params[3] = Plan in JSON format being sent to the user?
     */
    @Override
    protected String doInBackground(String... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(WebServerAddr);

        /* Retrieve and initialise variables. */
        String type = params[0];
        String topicName = params[1];
        String notifyMsg = params[2];

        /* Create JSON to send to the user. */
        JSONObject notification = new JSONObject();

        int id = Integer.parseInt(type);
        switch (id) {
            case SEND_PLAN_TO_GROUP:
                notification = sendPlan(id, topicName, notifyMsg, params[3]);
                break;
            case SUGGEST_CHANGE_TO_LEADER:
                notification = pingLeader(id, topicName, notifyMsg);
                break;
            default:
                Log.v("WE HAVE PROBLEMS", "Unknown id " + id + " passed");
        }

        try {
            /* We only need to pass the notification JSON in the body of the httpPost. */
            httpPost.setEntity(new StringEntity(notification.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /* Set headers for httpPost. Authorization key is necessary to send a message through
           the WebServerAddr above. */
        httpPost.setHeader("Content-type", "application/json");
        String auth_key = "AAAAaPCfQAA:APA91bHZqvzR9hSDUdKVZS7YrZT1yFk1tI09I8JvGf1" +
                "ATWF66L7-SVP2axZcXW3BLUzbpAJrlEcwbqM82eesc3TTJLyH4PK0K865zFsGKYq" +
                "7glxKIplq0HTTOKLa9niwJ9_NgtrbmtEQ";
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

    private JSONObject pingLeader(int type, String topicName, String notifyMsg) {
        return createNotification(type, topicName, notifyMsg, new JSONObject());
    }

    private JSONObject sendPlan(int type, String topicName, String notifyMsg, String data) {
        JSONObject plan = null;
        /* Initialise plan with the JSON string planInJSON. */
        try {
            plan = new JSONObject(data);
        } catch (JSONException e) {
            Log.e("BAD JSON", "params[3] was not a JSON Object!");
            e.printStackTrace();
        }

        JSONObject notification = createNotification(type, topicName, notifyMsg, plan);

        Log.v("SEND PLAN:", notification.toString());

        return notification;
    }

    private JSONObject createNotification(int type, String topicName, String notifyMsg, JSONObject plan) {
        JSONObject notification = new JSONObject();
        try {
            /* This sends the notification to the user's phone number, which they will be
               subscribed to once they've gone through the LandingPage. */
            notification.accumulate("to", "/topics/" + topicName);
            /* This sends the plan to the user. */
            JSONObject data = new JSONObject();
            data.accumulate("plan", plan);
            data.accumulate("id", type);
            data.accumulate("message", notifyMsg);
            notification.accumulate("data", data);
        } catch (JSONException e) {
            Log.e("UNLUCKY", "Could not create/accumulate JSON Object!");
            e.printStackTrace();
        }
        return notification;
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
