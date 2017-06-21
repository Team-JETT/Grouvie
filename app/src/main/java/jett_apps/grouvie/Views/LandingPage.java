package jett_apps.grouvie.Views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;

import jett_apps.grouvie.Adapters.CustomPlanAdapter;
import jett_apps.grouvie.HelperClasses.PlanManager;
import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.PlanningActivities.SelectGroup;
import jett_apps.grouvie.R;

public class LandingPage extends AppCompatActivity {

    public static final String PLAN_MESSAGE = "PLAN_MESSAGE";

    public static final String DATA = "DATA";
    public static final String CHANGE_MESSAGE = "CHANGE_MESSAGE";
    public static final String SENT_PLAN = "SENT_PLAN";
    public static final String CHANGED_PLAN_MESSAGE = "CHANGED_PLAN_MESSAGE";

    private TextView name;
    private Plan leaderData;
    private Plan sentPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        /* Subscribe user to their own phone number as a topic in order to send
           notifications to their phone. */
        String phoneNum = ProfileManager.getPhone(this);
//        String phoneNum = "07942948248";
        FirebaseMessaging.getInstance().subscribeToTopic(phoneNum);

        /* Retrieve the value in SENT_PLAN. If this value is non-null, then the app is being run
           by a group member who needs to see the plan sent by the leader. */
        sentPlan = (Plan) getIntent().getSerializableExtra(SENT_PLAN);
        if (sentPlan != null) {
            PlanManager.addPlan(sentPlan, LandingPage.this);
        }

        leaderData = new Plan();
        leaderData.setInitialPlan(true);

        final ArrayList<Plan> currentPlans = PlanManager.getPlans(LandingPage.this);
        ListAdapter planAdapter = new CustomPlanAdapter(this, currentPlans);

        ListView plansListView = (ListView) findViewById(R.id.plansList);
        plansListView.setAdapter(planAdapter);

        plansListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Plan p = currentPlans.get(position);
                        //Sending the current plan to the final planning page
                        Intent intent = new Intent(view.getContext(), CurrentPlanView.class);
                        intent.putExtra(PLAN_MESSAGE, p);
                        Plan savedP = (Plan) intent.getSerializableExtra(PLAN_MESSAGE);
                        startActivity(intent);

                    }
                }
        );


    }

    public void startPlanning(View view) {

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Calendar cMax = Calendar.getInstance();
        cMax.add(Calendar.DAY_OF_MONTH, 7);
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH) + 1;
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        //Fixing the off by one in the date picker
                        month = month + 1;

                        String chosenDate = day + "/" + month + "/" + year;

                        Intent intent = new Intent(view.getContext(), SelectGroup.class);

                        leaderData.setSuggestedDate(chosenDate);
                        leaderData.setSuggestedDay(day);
                        leaderData.setSuggestedMonth(month);
                        leaderData.setSuggestedYear(year);

                        intent.putExtra(DATA, leaderData);


                        SelectGroup.startProgressBar(LandingPage.this);

                        startActivity(intent);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
        datePickerDialog.updateDate(mYear, mMonth - 1, mDay);
        datePickerDialog.show();

    }

    @Override
    public void onBackPressed() {
    }

    public void viewProfile(View view) {
        Intent profileIntent = new Intent(LandingPage.this, ProfilePage.class);
        startActivity(profileIntent);
    }

}
