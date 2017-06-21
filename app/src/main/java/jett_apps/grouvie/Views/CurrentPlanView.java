package jett_apps.grouvie.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import jett_apps.grouvie.Notifications.FirebaseContact;
import jett_apps.grouvie.PlanningActivities.SuggestChangeInPlan;
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

    private boolean isAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan_view);
        isAccepted = false;

        Intent intent = getIntent();

        Plan updatedPlan = (Plan) intent.getSerializableExtra(PLAN_MESSAGE);
        if (updatedPlan != null) {
            p = updatedPlan;
            chosenFilm = p.getSuggestedFilm();
            chosenCinema = p.getSuggestedCinema();
            chosenTime = p.getSuggestedShowTime();
            chosenDay = p.getSuggestedDate();
            chosenFriends = p.getEventMembers();
        }

        Button button = (Button) findViewById(R.id.cancelPlan);
        Button acceptButton = (Button) findViewById(R.id.acceptPlan);

        if(!ProfileManager.getPhone(this).equals(p.getLeaderPhoneNum())) {
            button.setVisibility(View.INVISIBLE);
            acceptButton.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.INVISIBLE);
        }

        ImageButton getDirections = (ImageButton) findViewById(R.id.imageButton);
        ImageButton bookTickets = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton addEvent = (ImageButton) findViewById(R.id.addToCalendar);

        if (/*p.isConfirmed()*/true) {
            getDirections.setVisibility(View.VISIBLE);
            bookTickets.setVisibility(View.VISIBLE);
            addEvent.setVisibility(View.VISIBLE);
        } else {
            getDirections.setVisibility(View.INVISIBLE);
            bookTickets.setVisibility(View.INVISIBLE);
            addEvent.setVisibility(View.INVISIBLE);
        }


        ImageView moviePoster = (ImageView) findViewById(R.id.moviePoster);
        RequestOptions options = new RequestOptions();
        int posterWidth = 200;
        int posterHeight = 400;
        options.override(posterWidth, posterHeight).fitCenter();
        Glide.with(this).load(p.getMoviePoster()).apply(options).into(moviePoster);

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
            json_data.accumulate("creation_datetime", p.getCreationDateTime());
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
            json_data.accumulate("creation_datetime", p.getCreationDateTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ServerContact().execute("cancel_plan", json_data.toString());
        Intent intent = new Intent(view.getContext(), LandingPage.class);
        startActivity(intent);

    }

    public void getDirections(View view) {
        String postcode = ProfileManager.getPostcode(view.getContext());

        String uri = String.format(Locale.ENGLISH,"http://maps.google.com/maps?&saddr="
                + postcode
                + "&daddr="
                + p.getSuggestedCinema());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void addToCalendar(View view) {
        int day = p.getSuggestedDay();
        int month = p.getSuggestedMonth() - 1;
        int year = p.getSuggestedYear();

        String time = p.getSuggestedShowTime();
        String[] hourAndMinute = time.split(":");
        int hour = Integer.parseInt(hourAndMinute[0]);
        int minute = Integer.parseInt(hourAndMinute[1]);

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, "Grouvie Event: "+ p.getSuggestedFilm());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, p.getSuggestedCinema());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, p.getSuggestedFilm());

        GregorianCalendar calDateStart = new GregorianCalendar(year, month, day, hour, minute);
        GregorianCalendar calDateEnd = new GregorianCalendar(year, month, day, hour+3, minute);

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDateStart.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDateEnd.getTimeInMillis());

        startActivity(intent);
    }

    public void bookTickets(View view) {
        String googleSearch = "https://www.google.co.uk/search?q=" + p.getSuggestedCinema();
        String cinemaUrl = "";
        try {
            cinemaUrl = new ServerContact().execute("get_cinema_url", p.getSuggestedCinema()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse(cinemaUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void acceptPlan(View view) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate("phone_number", ProfileManager.getPhone(CurrentPlanView.this));
            json.accumulate("leader", p.getLeaderPhoneNum());
            json.accumulate("creation_datetime", p.getCreationDateTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ServerContact().execute("accept_plan", json.toString());
        Intent intent = new Intent(CurrentPlanView.this, LandingPage.class);
        startActivity(intent);
    }


    public void onBackPressed(View view) {
        Intent intent = new Intent(CurrentPlanView.this, LandingPage.class);
        startActivity(intent);
    }

}
