package jett_apps.grouvie.Views;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class ConfirmPlanView extends AppCompatActivity {

    private Plan p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_view);

        p = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);

    }

    public void getDirections(View view) {

    }

    public void bookTickets(View view) {

    }

    public void addToCalendar(View view) {
        int day = p.getSuggestedDay();
        int month = p.getSuggestedMonth();
        int year = p.getSuggestedYear();

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, "Grouvie Event: "+p.getSuggestedFilm());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, p.getSuggestedCinema());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, p.getSuggestedFilm());

        GregorianCalendar calDate = new GregorianCalendar(year, month, day);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());

        startActivity(intent);


    }


}
