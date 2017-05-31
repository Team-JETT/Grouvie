package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SelectFilm extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "FILMTITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_film);

        final String[] showingFilmsArray = {"Guardians of the Galaxy Vol 2",
                                        "The Fate of the Furious",
                                        "Boss Baby",
                                        "WonderWoman",
                                        "Baywatch",
                                        "Alien: Covenant",
                                        "Beauty and the Beast",
                                        "Lion",
                                        "Pirates of the Caribbean"};

        ListAdapter filmAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, showingFilmsArray);
        ListView filmsListView = (ListView) findViewById(R.id.filmList);
        filmsListView.setAdapter(filmAdapter);

        filmsListView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String filmTitle = showingFilmsArray[position];
                    Intent intent = new Intent(view.getContext(), SelectShowtime.class);
                    intent.putExtra(EXTRA_MESSAGE, filmTitle);
                    startActivity(intent);
                }
            }
        );

    }

}
