package jett_apps.grouvie.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import jett_apps.grouvie.Adapters.CustomFilmAdapter;
import jett_apps.grouvie.HelperObjects.Film;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.HelperObjects.PlanChange;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.CHANGE_MESSAGE;
import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class SelectFilmChange extends AppCompatActivity {

    private Plan p;
    private PlanChange planChange;
    private ArrayList<Film> films;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_film_change);

        p = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);
        planChange = (PlanChange) getIntent().getSerializableExtra(CHANGE_MESSAGE);

        films = p.getListOfFilms();

        ListAdapter filmAdapter = new CustomFilmAdapter(SelectFilmChange.this, films);
        ListView filmsListView = (ListView) findViewById(R.id.storedFilmList);
        filmsListView.setAdapter(filmAdapter);

        filmsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String filmTitle = films.get(position).getFilmName();
                        planChange.setFilmTitle(filmTitle);
                        Intent intent = new Intent(view.getContext(), SuggestChangeInPlan.class);
                        intent.putExtra(CHANGE_MESSAGE, planChange);
                        startActivity(intent);
                    }
                }
        );
    }
}
