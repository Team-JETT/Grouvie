package jett_apps.grouvie;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class LandingPage extends AppCompatActivity {

    public static final String FILM_MESSAGE = "FILMTITLE";
    public static final String CINEMA_MESSAGE= "CINEMATITLE";
    public static final String SHOWTIME_MESSAGE = "SHOWTIME";
    public static final String DAY_MESSAGE = "EVENTDAY";
    public static final String CINEMA_DATA = "CINEMADATA";
    public static final String USER_NAME = "USERNAME";

    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        name = (TextView) findViewById(R.id.user_name);
        Intent currIntent = getIntent();
        name.setText(currIntent.getStringExtra(USER_NAME));
    }

    public void startPlanning(View view) {
        Intent intent = new Intent(this, SelectFilm.class);

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Calendar cMax = Calendar.getInstance();
        cMax.add(Calendar.DAY_OF_MONTH, 7);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String chosenDay = dayOfMonth + "/" + month + "/" + year;

                        Intent intent = new Intent(view.getContext(), SelectFilm.class);
                        intent.putExtra(DAY_MESSAGE, chosenDay);

                        ServerContact.dialog = new ProgressDialog(LandingPage.this, ProgressDialog.BUTTON_POSITIVE);
                        ServerContact.dialog.setTitle("Please wait");
                        ServerContact.dialog.setMessage("Obtaining listings from server");
                        ServerContact.dialog.show();

                        startActivity(intent);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
        datePickerDialog.show();

    }


}
