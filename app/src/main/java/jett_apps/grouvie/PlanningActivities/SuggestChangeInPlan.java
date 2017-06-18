package jett_apps.grouvie.SuggestionActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import jett_apps.grouvie.Activities.SelectCinema;
import jett_apps.grouvie.Activities.SelectFilm;
import jett_apps.grouvie.Activities.SelectShowtime;
import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.LandingPage;

import static jett_apps.grouvie.Views.LandingPage.DATA;
import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class SuggestChangeInPlan extends AppCompatActivity {

    private Plan leaderPlan;
    private Plan suggestedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_change_in_plan);

        //Update suggested plan so far
        suggestedPlan = (Plan) getIntent().getSerializableExtra(DATA);

        if (suggestedPlan == null) {

            //Get current leader plan
            leaderPlan = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);

            //Obtain current plan suggested by the leader if it hasn't been obtaned already
            suggestedPlan = new Plan(leaderPlan);

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
                datePickerDialog.show();
            }
        });

        //Changing the cinema
        TextView cinema = (TextView) findViewById(R.id.currentCinema);
        cinema.setText(suggestedPlan.getSuggestedCinema());
        cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectCinema.class);
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

    public void done(View view) {
        //TODO: Send the suggested plan to the group leader
        Intent intent = new Intent(SuggestChangeInPlan.this, LandingPage.class);
        startActivity(intent);
    }
}
