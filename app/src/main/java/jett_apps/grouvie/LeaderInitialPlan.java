package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static jett_apps.grouvie.LandingPage.DATA;

public class LeaderInitialPlan extends AppCompatActivity {

    private double latitude, longitude;
    private String chosenFilm, chosenCinema, chosenTime, chosenDay;
    private ArrayList<Friend> chosenGroup;

    private PropogationObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        data = (PropogationObject) getIntent().getSerializableExtra(DATA);

        chosenFilm = data.getFilmTitle();
        chosenCinema = data.getCinemaData();
        chosenTime = data.getChosenTime();
        chosenDay = data.getDate();
        chosenGroup = data.getSelectedFriends();

        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDay);

    }

    public void sendToGroup(View view) throws IOException {

        JSONObject json = new JSONObject();
        String leaderPhoneNum = ProfileManager.getPhone(LeaderInitialPlan.this);
        try {
            json.accumulate("phone_number", leaderPhoneNum);
            json.accumulate("leader", leaderPhoneNum);
            json.accumulate("showtime", chosenTime);
            json.accumulate("film", chosenFilm);
            json.accumulate("cinema", chosenCinema);
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
            String[] friendsNumbers = getFriendsNumbers(data.getSelectedFriends());
            json.accumulate("friends", Arrays.toString(friendsNumbers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("MAKE PLAN:", json.toString());
        // Send initial/draft plan to web server to update the database
        new ServerContact().execute("make_plan", json.toString());

        for (Friend groupMember : chosenGroup) {
            String topicName = groupMember.getPhoneNum();
            String result = null;
            try {
                result = new FirebaseContact().execute("topics/to/" + topicName, "").get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Failed to send plan to: " + topicName);
                e.printStackTrace();
            }
            Log.e("FCM_RES", result);
        }

        Plan p = new Plan(chosenFilm, chosenCinema, chosenTime, chosenDay, chosenGroup,
                            leaderPhoneNum);
        CurrentPlans.addPlan(p, LeaderInitialPlan.this);

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
    }

    public String[] getFriendsNumbers(ArrayList<Friend> friends) {
        String[] numbers = new String[friends.size()];
        for (int i = 0; i < friends.size(); ++i) {
            numbers[i] = friends.get(i).getPhoneNum();
        }
        return numbers;
    }
}

