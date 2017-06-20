package jett_apps.grouvie.Views;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import jett_apps.grouvie.HelperClasses.ProfileManager;
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
        String postcode = ProfileManager.getPostcode(view.getContext());

        String uri = String.format(Locale.ENGLISH,"http://maps.google.com/maps?&saddr="
                       + postcode
                       + "&daddr="
                       + p.getSuggestedCinema());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void bookTickets(View view) {

    }

    public void addToCalendar(View view) {
        int day = p.getSuggestedDay();
        int month = p.getSuggestedMonth();
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

    public LatLng getLatLngFromLocationName(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng position = null;

        try {
            addressList = geocoder.getFromLocationName(address, 5);
            if(addressList == null) {
                return null;
            }
            Address location = addressList.get(0);
            location.getLongitude();
            location.getLatitude();
            position = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }



}
