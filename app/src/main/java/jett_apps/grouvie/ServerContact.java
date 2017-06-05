package jett_apps.grouvie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerContact {
    
    static String WebServerIP = "54.148.4.84";

    protected Void doInBackground(JSONObject... params) throws IOException {
        URL url = new URL("http://" + WebServerIP + ":5000/insert");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestProperty("charset", "utf-8");

        DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
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
        printout.write(json.toString().getBytes("UTF8"));

        printout.flush();
        printout.close();


//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost("http://129.31.228.213:5000/insert");
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
//        return null;
        return null;
    }
}
