package jett_apps.grouvie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import static jett_apps.grouvie.MainActivity.DAY_MESSAGE;

public class SelectDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);


        //TODO: Obtain from web server
        final String[] daysArray = {"7 June", "8 June", "9 June", "10 June", "11 June", "12 June",
                "13 June", "14 June"};

        ListAdapter showtimeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, daysArray);
        ListView showtimeListView = (ListView) findViewById(R.id.dayList);
        showtimeListView.setAdapter(showtimeAdapter);

        showtimeListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String chosenDay = daysArray[position];
                        //Sending the current plan to the final planning page
                        Intent intent = new Intent(view.getContext(), SelectFilm.class);
                        intent.putExtra(DAY_MESSAGE, chosenDay);

                        ServerContact.dialog = new ProgressDialog(SelectDay.this, ProgressDialog.BUTTON_POSITIVE);
//                        ServerContact.dialog = new ProgressDialog(SelectDay.this, ProgressDialog.THEME_HOLO_LIGHT);
                        ServerContact.dialog.setTitle("Please wait");
                        ServerContact.dialog.setMessage("Obtaining listings from server");
                        ServerContact.dialog.show();

                        startActivity(intent);

                    }
                }
        );

    }
}
