package jett_apps.grouvie.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import jett_apps.grouvie.HelperClasses.PlanManager;
import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.Notifications.FirebaseContact;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class LeaderInitialPlan extends AppCompatActivity {

    private double latitude, longitude;
    private String chosenFilm, chosenCinema, chosenTime, chosenDate;
    private ArrayList<Friend> chosenGroup;
    private String cinemaData;

    private Plan data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        data = (Plan) getIntent().getSerializableExtra(DATA);

        chosenFilm = data.getSuggestedFilm();
        chosenCinema = data.getSuggestedCinema();
        chosenTime = data.getSuggestedShowTime();
        chosenDate = data.getSuggestedDate();
        chosenGroup = data.getEventMembers();
        cinemaData = data.getCinemaData();


        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDate);

    }

    public void sendToGroup(View view) throws IOException {
        /* Create JSON file to hold information about the plan. */
        JSONObject json = new JSONObject();
        String leaderPhoneNum = ProfileManager.getPhone(LeaderInitialPlan.this);
        String leaderName = ProfileManager.getName(this);
        try {
            json.accumulate("leader_name", leaderName);
            json.accumulate("phone_number", leaderPhoneNum);
            json.accumulate("leader", leaderPhoneNum);
            json.accumulate("showtime", chosenTime);
            json.accumulate("chosenFilm", chosenFilm);
            json.accumulate("chosenCinema", chosenCinema);
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
            json.accumulate("date", chosenDate);
            ArrayList<Friend> friends = chosenGroup;
            String[] friendsNames = getFriendsNames(friends);
            json.accumulate("friend_list", Arrays.toString(friendsNames));
            String[] friendsNumbers = getFriendsNumbers(friends);
            json.accumulate("friends", Arrays.toString(friendsNumbers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* Send initial/draft plan to web server to update the database. */
        new ServerContact().execute("make_plan", json.toString());

        /* Send initial/draft plan to other group members. */
        for (Friend groupMember : chosenGroup) {
            String topicName = groupMember.getPhoneNum();
            try {
                json.remove("phone_number");
                json.accumulate("phone_number", topicName);
                new FirebaseContact().execute("07434897141"/*topicName*/, json.toString());
            } catch (JSONException e) {
                Log.e("CHANGE PHONE_NUMBER", "Failed to change phone number in JSON");
                e.printStackTrace();
            }
        }

        data.setLeaderPhoneNum(leaderPhoneNum);
        data.setInitialPlan(false);

        PlanManager.addPlan(data, LeaderInitialPlan.this);

        Toast.makeText(getApplicationContext(), "Plan submitted to group", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
    }

    public String[] getFriendsNames(ArrayList<Friend> friends) {
        String[] names = new String[friends.size()];
        for (int i = 0; i < friends.size(); ++i) {
            names[i] = friends.get(i).getName();
        }
        return names;
    }

    public String[] getFriendsNumbers(ArrayList<Friend> friends) {
        String[] numbers = new String[friends.size()];
        for (int i = 0; i < friends.size(); ++i) {
            numbers[i] = friends.get(i).getPhoneNum();
        }
        return numbers;
    }
}

