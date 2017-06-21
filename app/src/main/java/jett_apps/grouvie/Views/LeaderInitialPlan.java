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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import jett_apps.grouvie.HelperClasses.PlanManager;
import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.Notifications.FirebaseContact;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Notifications.FirebaseContact.SEND_PLAN_TO_GROUP;
import static jett_apps.grouvie.Views.LandingPage.DATA;

public class LeaderInitialPlan extends AppCompatActivity {

    private String chosenFilm, chosenCinema, chosenTime, chosenDate;
    private ArrayList<Friend> chosenGroup;
    private String cinemaData;

    private Plan data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        data = (Plan) getIntent().getSerializableExtra(DATA);

        chosenDate = data.getSuggestedDate();
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        data.setCreationDateTime(dateFormat.format(date));

        try {
            json.accumulate("phone_number", leaderPhoneNum);
            json.accumulate("leader", leaderPhoneNum);
            json.accumulate("date", chosenDate);
            json.accumulate("creation_datetime", dateFormat.format(date));
            json.accumulate("showtime", chosenTime);
            json.accumulate("film", chosenFilm);
            json.accumulate("cinema", chosenCinema);
            String[] friendsNumbers = getFriendsNumbers(chosenGroup);
            json.accumulate("friends", Arrays.toString(friendsNumbers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* Send initial/draft plan to web server to update the database. */
        new ServerContact().execute("make_plan", json.toString());

        /* Add names of friends to JSON plan. This is needed to create an ArrayList<Friend> from
           the JSON values in "friends" and "friend_list". This is needed for the Plan object in
           MessagingService, else friends will not display correctly in "View Group Replies". */
        String[] friendsNames = getFriendsNames(chosenGroup);
        try {
            json.put("friend_list", Arrays.toString(friendsNames));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String type = "" + SEND_PLAN_TO_GROUP;
        /* Create notification body for message. */
        String messageBody = ((leaderName == null) ? "Someone" : leaderName.trim()) +
                " has created a new plan. Click here to accept it!";

        /* Send initial/draft plan to other group members. */
        for (Friend groupMember : chosenGroup) {
            /* Get the recipient member's phone number. */
            String topicName = groupMember.getPhoneNum();
            try {
                /* Update phone_number field in JSON and send it to the user. */
                json.put("phone_number", topicName);
                /* Send notification to phone number. */
                new FirebaseContact().execute(type, topicName, messageBody, json.toString());
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

    public static String[] getFriendsNames(ArrayList<Friend> friends) {
        String[] names = new String[friends.size()];
        for (int i = 0; i < friends.size(); ++i) {
            names[i] = friends.get(i).getName();
        }
        return names;
    }

    public static String[] getFriendsNumbers(ArrayList<Friend> friends) {
        String[] numbers = new String[friends.size()];
        for (int i = 0; i < friends.size(); ++i) {
            numbers[i] = friends.get(i).getPhoneNum();
        }
        return numbers;
    }
}

