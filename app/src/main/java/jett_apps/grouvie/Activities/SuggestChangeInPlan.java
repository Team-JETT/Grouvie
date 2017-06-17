package jett_apps.grouvie.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.HelperObjects.PlanChange;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.CurrentPlanView;

import static jett_apps.grouvie.Views.LandingPage.CHANGE_MESSAGE;
import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class SuggestChangeInPlan extends AppCompatActivity {

    private Plan p;
    private PlanChange planChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_change_in_plan);

        p = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);

        planChange = (PlanChange) getIntent().getSerializableExtra(CHANGE_MESSAGE);

        if(planChange == null) {
            planChange = new PlanChange();
        }

        TextView film = (TextView) findViewById(R.id.currentFilm);
        String filmTitle = planChange.getFilmTitle();
        if(filmTitle == null) {
            filmTitle = p.getSuggestedFilm();
        }
        film.setText(filmTitle);
        film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectFilmChange.class);
                intent.putExtra(PLAN_MESSAGE, p);
                intent.putExtra(CHANGE_MESSAGE, planChange);
                startActivity(intent);
            }
        });

        TextView date = (TextView) findViewById(R.id.currentDate);
        date.setText(p.getSuggestedDate());


        TextView time = (TextView) findViewById(R.id.currentShowtime);
        time.setText(p.getSuggestedShowTime());

        TextView cinema = (TextView) findViewById(R.id.currentCinema);
        cinema.setText(p.getSuggestedCinema());


    }

    public void done(View view) {
        Intent intent = new Intent(SuggestChangeInPlan.this, CurrentPlanView.class);
        intent.putExtra(PLAN_MESSAGE, p);
        startActivity(intent);
    }
}
