package jett_apps.grouvie.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.HelperObjects.PropagationObject;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.CurrentPlanView;

import static jett_apps.grouvie.Views.LandingPage.CHANGED_PLAN_MESSAGE;
import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class SuggestChangeInPlan extends AppCompatActivity {

    private Plan currentPlan;
    private PropagationObject suggestedPlanData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_change_in_plan);

        //Obtain current plan suggested by the leader if it hasn't been obtaned already
        if (currentPlan == null) {
            currentPlan = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);
        }

        //Update suggested plan so far
        suggestedPlanData = (PropagationObject) getIntent().getSerializableExtra(CHANGED_PLAN_MESSAGE);

        //Suggested plan for user is initially the same as their current plan
        if (suggestedPlanData == null) {
            suggestedPlanData = new PropagationObject();
            suggestedPlanData.setFilmTitle(currentPlan.getSuggestedFilm());
            suggestedPlanData.setCinemaData(currentPlan.getSuggestedCinema());
            suggestedPlanData.setChosenTime(currentPlan.getSuggestedShowTime());
            suggestedPlanData.setDate(currentPlan.getSuggestedDate());
            suggestedPlanData.setSelectedFriends(currentPlan.getEventMembers());

            suggestedPlanData.setDay(currentPlan.getSuggestedDay());
            suggestedPlanData.setMonth(currentPlan.getSuggestedMonth());
            suggestedPlanData.setYear(currentPlan.getSuggestedYear());
//            suggestedPlanData.setLeaderPhone
        }

//        if(suggestedPlan == null) {
//            suggestedPlan = new Plan(currentPlan.getSuggestedFilm(),
//                                        currentPlan.getSuggestedCinema(),
//                                        currentPlan.getSuggestedShowTime(),
//                                        currentPlan.getSuggestedDate(),
//                                        currentPlan.getEventMembers(),
//                                        currentPlan.getLeaderPhoneNum());
//
//            suggestedPlan.setSuggestedDay(currentPlan.getSuggestedDay());
//            suggestedPlan.setSuggestedMonth(currentPlan.getSuggestedMonth());
//            suggestedPlan.setSuggestedYear(currentPlan.getSuggestedYear());
//        }

        //Changing the film
        TextView film = (TextView) findViewById(R.id.currentFilm);
        film.setText(suggestedPlanData.getFilmTitle());
        film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectFilmChange.class);
                intent.putExtra(CHANGED_PLAN_MESSAGE, suggestedPlanData);
                ServerContact.startProgressBar(SuggestChangeInPlan.this);
                startActivity(intent);
            }
        });

        //Changing the date
        final TextView date = (TextView) findViewById(R.id.currentDate);
        date.setText(suggestedPlanData.getDate());
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

                                Intent intent = new Intent(view.getContext(), SelectFilmChange.class);

                                suggestedPlanData.setDate(chosenDay);
                                suggestedPlanData.setDay(day);
                                suggestedPlanData.setMonth(month + 1);
                                suggestedPlanData.setYear(year);

                                intent.putExtra(CHANGED_PLAN_MESSAGE, suggestedPlanData);
                                ServerContact.startProgressBar(SuggestChangeInPlan.this);
                                startActivity(intent);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        //Changing the showtime
        TextView time = (TextView) findViewById(R.id.currentShowtime);
        time.setText(suggestedPlanData.getChosenTime());
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SelectShowtimeChange.class);
//                intent.putExtra(CHANGED_PLAN_MESSAGE, suggestedPlan);
//                startActivity(intent);
            }
        });

        //Changing the cinema
        TextView cinema = (TextView) findViewById(R.id.currentCinema);
        cinema.setText(suggestedPlanData.getCinema());
        cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SelectCinemaChange.class);
//                intent.putExtra(CHANGED_PLAN_MESSAGE, suggestedPlan);
//                startActivity(intent);
            }
        });


    }

    public void done(View view) {
        //TODO: Send the suggested plan to the group leader
        //TODO: Don't really need to keep track of suggestions yet
        Intent intent = new Intent(SuggestChangeInPlan.this, CurrentPlanView.class);

        //TODO: Convert propogation object to plan
//        Plan suggestedPlan = suggestedPlanData.toPlan();
//        intent.putExtra(PLAN_MESSAGE, suggestedPlan);
        startActivity(intent);
    }
}
