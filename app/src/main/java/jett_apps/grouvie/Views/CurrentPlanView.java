package jett_apps.grouvie.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jett_apps.grouvie.SuggestionActivities.SuggestChangeInPlan;
import jett_apps.grouvie.HelperClasses.PlanManager;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.R;
import jett_apps.grouvie.HelperClasses.ServerContact;

import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class CurrentPlanView extends AppCompatActivity {

    private ArrayList<Friend> chosenFriends;
    private String chosenDay;
    private String chosenTime;
    private String chosenCinema;
    private String chosenFilm;
    private Plan p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan_view);

        Intent intent = getIntent();
        p = (Plan) intent.getSerializableExtra(PLAN_MESSAGE);
        chosenFilm = p.getSuggestedFilm();
        chosenCinema = p.getSuggestedCinema();
        chosenTime = p.getSuggestedShowTime();
        chosenDay = p.getSuggestedDate();
        chosenFriends = p.getEventMembers();

        Button button = (Button) findViewById(R.id.cancelPlan);

        if(!ProfileManager.getPhone(this).equals(p.getLeaderPhoneNum())) {
            button.setVisibility(View.INVISIBLE);
        }

        TextView film = (TextView) findViewById(R.id.SelectedFilm);
        film.setText(chosenFilm);

        TextView cinema = (TextView) findViewById(R.id.SelectedCinema);
        cinema.setText(chosenCinema);

        TextView time = (TextView) findViewById(R.id.SelectedShowtime);
        time.setText(chosenTime);

        TextView day = (TextView) findViewById(R.id.SelectedDay);
        day.setText(chosenDay);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentPlanView.this, LandingPage.class);
                startActivity(intent);
            }
        });

    }

    public void viewGroupReplies(View view) {
        //TODO: Show activity with group replies and option to replan.
        Intent intent = new Intent(view.getContext(), GroupView.class);
        intent.putExtra(PLAN_MESSAGE, p);
        startActivity(intent);
    }

    public void makeChange(View view) {
        Intent intent = new Intent(view.getContext(), SuggestChangeInPlan.class);
        intent.putExtra(PLAN_MESSAGE, p);
        startActivity(intent);
    }

    public void cantGo(View view) {
        PlanManager.deletePlan(p, this);
        JSONObject json_data = new JSONObject();
        try {
            json_data.accumulate("phone_number", p.getLeaderPhoneNum());
            json_data.accumulate("leader", p.getLeaderPhoneNum());
            json_data.accumulate("showtime", p.getSuggestedShowTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Delete on web server here
        new ServerContact().execute("delete_single", json_data.toString());
        Intent intent = new Intent(view.getContext(), LandingPage.class);
        startActivity(intent);
    }

    public void cancelPlan(View view) {
        PlanManager.deletePlan(p, this);
        // Delete the plan on the server
        JSONObject json_data = new JSONObject();
        try {
            json_data.accumulate("leader", p.getLeaderPhoneNum());
            json_data.accumulate("showtime", p.getSuggestedShowTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ServerContact().execute("delete_plan", json_data.toString());
        Intent intent = new Intent(view.getContext(), LandingPage.class);
        startActivity(intent);

    }

    public void backButton(View view) {
        Intent intent = new Intent(CurrentPlanView.this, LandingPage.class);
        startActivity(intent);
    }

}
