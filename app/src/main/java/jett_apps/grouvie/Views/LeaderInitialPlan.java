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
import jett_apps.grouvie.HelperObjects.Film;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.HelperObjects.PropagationObject;
import jett_apps.grouvie.Notifications.FirebaseContact;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class LeaderInitialPlan extends AppCompatActivity {

    private double latitude, longitude;
    private String chosenFilm, chosenCinema, chosenTime, chosenDay;
    private ArrayList<Friend> chosenGroup;
    private String cinemaData;

    private PropagationObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_initial_plan);

        data = (PropagationObject) getIntent().getSerializableExtra(DATA);

        chosenFilm = data.getFilmTitle();
        chosenCinema = data.getCinema();
        chosenTime = data.getChosenTime();
        chosenDay = data.getDate();
        chosenGroup = data.getSelectedFriends();
//        cinemaData = data.getCinema();


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
            json.accumulate("chosenFilm", chosenFilm);
            json.accumulate("chosenCinema", chosenCinema);
            json.accumulate("latitude", latitude);
            json.accumulate("longitude", longitude);
            json.accumulate("date", chosenDay);
            ArrayList<Friend> friends = data.getSelectedFriends();
            String[] friendsNames = getFriendsNames(friends);
            json.accumulate("friend_list", Arrays.toString(friendsNames));
            String[] friendsNumbers = getFriendsNumbers(friends);
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
                json.remove("phone_number");
                json.accumulate("phone_number", topicName);
                result = new FirebaseContact().execute(topicName, json.toString()).get();
            } catch (InterruptedException | ExecutionException | JSONException e) {
                System.out.println("Failed to send plan to: " + topicName);
                e.printStackTrace();
            }
            Log.e("FCM_RES", result);
        }

        Plan p = new Plan(chosenFilm, chosenCinema, chosenTime, chosenDay, chosenGroup,
                            leaderPhoneNum);
        p.setCinemaData(cinemaData);
        p.setSuggestedDay(data.getDay());
        p.setSuggestedMonth(data.getMonth());
        p.setSuggestedYear(data.getYear());

        ArrayList<Film> listOfFilms = data.getListOfFilms();
        p.setListOfFilms(listOfFilms);
        PlanManager.addPlan(p, LeaderInitialPlan.this);

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

