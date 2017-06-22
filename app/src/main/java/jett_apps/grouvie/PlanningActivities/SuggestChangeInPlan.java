package jett_apps.grouvie.PlanningActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.Notifications.FirebaseContact;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.CinemaLocations;
import jett_apps.grouvie.Views.CurrentPlanView;
import jett_apps.grouvie.Views.LandingPage;

import static jett_apps.grouvie.Notifications.FirebaseContact.PING_MEMBER;
import static jett_apps.grouvie.Views.CurrentPlanView.LEADER_DATA;
import static jett_apps.grouvie.Views.LandingPage.DATA;

public class SuggestChangeInPlan extends AppCompatActivity {

    public static String LEADER_PLAN_KEY  = "leader_plan_key";

    private Plan leaderPlan;
    private Plan suggestedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_change_in_plan);

        //Update suggested plan so far
        suggestedPlan = (Plan) getIntent().getSerializableExtra(DATA);

        if (leaderPlan == null) {
            restoreLeaderPlan();
        }

        //If this is initial run of this activity
        if (suggestedPlan == null ) {

            // Get current leader plan and save it to obtaining changes later on
            leaderPlan = (Plan) getIntent().getSerializableExtra(LEADER_DATA);
            saveLeaderPlan();

            // Obtain current plan suggested by the leader (leaderPlan)
            suggestedPlan = new Plan(leaderPlan);

        }


        if (ProfileManager.getPhone(SuggestChangeInPlan.this)
                .equals(leaderPlan.getLeaderPhoneNum())) {

            Button sendButton = (Button) findViewById(R.id.finishChange);
            sendButton.setText("SEND TO GROUP");

        }

        //Changing the film
        TextView film = (TextView) findViewById(R.id.currentFilm);
        film.setText(suggestedPlan.getSuggestedFilm());
        film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectFilm.class);
                intent.putExtra(DATA, suggestedPlan);
                ServerContact.startProgressBar(SuggestChangeInPlan.this);
                startActivity(intent);
            }
        });

        //Changing the date
        final TextView date = (TextView) findViewById(R.id.currentDate);
        date.setText(suggestedPlan.getSuggestedDate());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtaining the dates available for selection
                final Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 1);
                Calendar cMax = Calendar.getInstance();
                cMax.add(Calendar.DAY_OF_MONTH, 7);
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH) + 1;
                final int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog
                        = new DatePickerDialog(SuggestChangeInPlan.this
                                                , new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                String chosenDay = day + "/" + month + "/" + year;

                                Intent intent = new Intent(view.getContext(), SelectFilm.class);

                                suggestedPlan.setSuggestedDate(chosenDay);
                                suggestedPlan.setSuggestedDay(day);
                                suggestedPlan.setSuggestedMonth(month + 1);
                                suggestedPlan.setSuggestedYear(year);

                                intent.putExtra(DATA, suggestedPlan);
                                ServerContact.startProgressBar(SuggestChangeInPlan.this);
                                startActivity(intent);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
                datePickerDialog.updateDate(mYear, mMonth - 1, mDay);
                datePickerDialog.show();
            }
        });

        //Changing the cinema
        TextView cinema = (TextView) findViewById(R.id.currentCinema);
        cinema.setText(suggestedPlan.getSuggestedCinema());
        cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CinemaLocations.class);
                intent.putExtra(DATA, suggestedPlan);
                startActivity(intent);
            }
        });

        //Changing the showtime
        TextView time = (TextView) findViewById(R.id.currentShowtime);
        time.setText(suggestedPlan.getSuggestedShowTime());
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectShowtime.class);
                intent.putExtra(DATA, suggestedPlan);
                startActivity(intent);
            }
        });



    }

    private void saveLeaderPlan() {
        //Saving leader plan in shraed prefs
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(leaderPlan);
        prefsEditor.putString(LEADER_PLAN_KEY, json);
        prefsEditor.apply();
    }

    private void restoreLeaderPlan() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();
        Gson gson = new Gson();
        String json = sp.getString(LEADER_PLAN_KEY, "");
        leaderPlan = gson.fromJson(json, Plan.class);
//        spEditor.remove(LEADER_PLAN_KEY);
//        spEditor.apply();
    }

    public void done(View view) {

        restoreLeaderPlan();

        //If Leader is changing the plan
        if (ProfileManager.getPhone(SuggestChangeInPlan.this)
                .equals(leaderPlan.getLeaderPhoneNum())) {

            JSONObject json = new JSONObject();
            try {
                json.accumulate("leader", suggestedPlan.getLeaderPhoneNum());
                json.accumulate("creation_datetime", suggestedPlan.getCreationDateTime());
                json.accumulate("date", suggestedPlan.getSuggestedDate());
                json.accumulate("showtime", suggestedPlan.getSuggestedShowTime());
                json.accumulate("film", suggestedPlan.getSuggestedFilm());
                json.accumulate("cinema", suggestedPlan.getSuggestedCinema());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new ServerContact().execute("update_leader_plan", json.toString());

            String type = "" + PING_MEMBER;
            String suggesterName = ProfileManager.getName(this);
            String messageBody =  suggesterName + " has made a change. Click here to view it!";
            ArrayList<Friend> friends = leaderPlan.getEventMembers();

            for (Friend f : friends) {
                String friendPhone = f.getPhoneNum();
                new FirebaseContact().execute(type, friendPhone, messageBody);
            }


            Intent intent = new Intent(SuggestChangeInPlan.this, CurrentPlanView.class);
            intent.putExtra(DATA, suggestedPlan);
            startActivity(intent);


        } else { //If a group member is suggesting a plan

            // We do all these checks to avoid inserting duplicate data into the database.
            String date = null;
            String film = null;
            String cinema = null;
            String showtime = null;

            //Restoring original leader's plan

            //If a new date has been suggested, send to database
            String suggestedDate = suggestedPlan.getSuggestedDate();
            if (!suggestedDate.equals(leaderPlan.getSuggestedDate())) {
                date = suggestedDate;
            }

            //If a new film has been suggested, send to database
            String suggestedFilm = suggestedPlan.getSuggestedFilm();
            if (!suggestedFilm.equals(leaderPlan.getSuggestedFilm())) {
                film = suggestedFilm;
            }

            //If a new cinema has been suggested, send to database
            String suggestedCinema = suggestedPlan.getSuggestedCinema();
            if (!suggestedCinema.equals(leaderPlan.getSuggestedCinema())) {
                cinema = suggestedCinema;
            }

            //If a new showtime has been suggested, send to database
            String suggestedShowtime = suggestedPlan.getSuggestedShowTime();
            if (!suggestedShowtime.equals(leaderPlan.getSuggestedShowTime())) {
                showtime = suggestedShowtime;
            }

            JSONObject json = new JSONObject();
            String leaderPhoneNum = null;
            try {
                // TODO: Someone confirm with Erkin these are the right variables to use.
                json.accumulate("phone_number", ProfileManager.getPhone(SuggestChangeInPlan.this));
                leaderPhoneNum = leaderPlan.getLeaderPhoneNum();
                json.accumulate("leader", leaderPhoneNum);
                json.accumulate("creation_datetime", suggestedPlan.getCreationDateTime());
                json.accumulate("date", (date == null) ? JSONObject.NULL : date);
                json.accumulate("film", (film == null) ? JSONObject.NULL : film);
                json.accumulate("cinema", (cinema == null) ? JSONObject.NULL : cinema);
                json.accumulate("showtime", (showtime == null) ? JSONObject.NULL : showtime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new ServerContact().execute("suggest_plan", json.toString());
            String type = "" + PING_MEMBER;
            String suggesterName = ProfileManager.getName(this);
            String messageBody =  suggesterName + " has suggested a change. Click here to view it!";
            new FirebaseContact().execute(type, leaderPhoneNum, messageBody);
            Intent intent = new Intent(SuggestChangeInPlan.this, LandingPage.class);
            startActivity(intent);
        }
    }
}
