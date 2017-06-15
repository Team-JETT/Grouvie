package jett_apps.grouvie;

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

import java.util.ArrayList;
import java.util.Calendar;

public class LandingPage extends AppCompatActivity {


    public static final String FILM_MESSAGE = "FILMTITLE";
    public static final String CINEMA_MESSAGE = "CINEMATITLE";
    public static final String SHOWTIME_MESSAGE = "SHOWTIME";
    public static final String DATE_MESSAGE = "EVENTDAY";
    public static final String CINEMA_DATA = "CINEMADATA";
    public static final String USER_NAME = "USERNAME";
    public static final String SHOWTIME_DISTANCE_DATA = "SHOWTIMEDISTANCEDATA";
    public static final String GROUP_LIST = "GROUPLIST";
    public static final String PLAN_MESSAGE = "PLAN_MESSAGE";

    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";
    public static final String DATA = "DATA";

    private TextView name;
    private PropogationObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        data = new PropogationObject();

        final ArrayList<Plan> currentPlans = CurrentPlans.getPlans(LandingPage.this);
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
                        String chosenDay = day + "/" + month + "/" + year;

                        Intent intent = new Intent(view.getContext(), SelectGroup.class);

                        data.setDate(chosenDay);
                        data.setDay(day);
                        data.setMonth(month+1);
                        data.setYear(year);

                        intent.putExtra(DATA, data);

                        startActivity(intent);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
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
