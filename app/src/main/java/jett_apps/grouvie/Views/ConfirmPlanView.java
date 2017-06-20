package jett_apps.grouvie.Views;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperClasses.ServerContact;
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


}
