package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static jett_apps.grouvie.LandingPage.PLAN_MESSAGE;

public class CurrentPlanView extends AppCompatActivity {

    private String[] chosenFriends;
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

        ((TextView) findViewById(R.id.SelectedFilm)).setText(chosenFilm);
        ((TextView) findViewById(R.id.SelectedCinema)).setText(chosenCinema);
        ((TextView) findViewById(R.id.SelectedShowtime)).setText(chosenTime);
        ((TextView) findViewById(R.id.SelectedDay)).setText(chosenDay);

    }

    public void viewGroupReplies(View view) {
        //TODO: Show activity with group replies and option to replan.
    }

    public void cantGo(View view) {
        CurrentPlans.deletePlan(p, this);
        // Delete the plan on the server
        new ServerContact().execute("delete_plan", )
        Intent intent = new Intent(view.getContext(), LandingPage.class);
        startActivity(intent);
        //TODO: Show activity with group replies and option to replan.
    }

    public void cancelPlan(View view) {
        CurrentPlans.deletePlan(p, this);
        // TODO: Delete the plan on the server - awaiting username in Plan class
        Intent intent = new Intent(view.getContext(), LandingPage.class);
        startActivity(intent);
        //TODO: Show activity with group replies and option to replan.

    }
}
