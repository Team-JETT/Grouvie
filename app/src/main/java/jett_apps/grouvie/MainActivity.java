package jett_apps.grouvie;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String FILM_MESSAGE = "FILMTITLE";
    public static final String CINEMA_MESSAGE= "CINEMATITLE";
    public static final String SHOWTIME_MESSAGE = "SHOWTIME";
    public static final String DAY_MESSAGE = "EVENTDAY";
    public static final String LOCAL_DATA = "LOCALDATA";

    private int mYear;
    private int mDay;
    private int mMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPlanning(View view) {
        Intent intent = new Intent(this, SelectFilm.class);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog =
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String chosenDay = dayOfMonth + "/" + month + "/" + year;

                    Intent intent = new Intent(view.getContext(), SelectFilm.class);
                    intent.putExtra(DAY_MESSAGE, chosenDay);

                    ServerContact.dialog = new ProgressDialog(MainActivity.this, ProgressDialog.BUTTON_POSITIVE);
                    ServerContact.dialog.setTitle("Please wait");
                    ServerContact.dialog.setMessage("Obtaining listings from server");
                    ServerContact.dialog.show();

                    startActivity(intent);
                }
            }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

}
