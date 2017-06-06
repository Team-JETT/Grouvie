package jett_apps.grouvie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;

import static jett_apps.grouvie.SelectFilm.CINEMA_MESSAGE;
import static jett_apps.grouvie.SelectFilm.FILM_MESSAGE;

public class GroupSelection extends AppCompatActivity {
    public static final String FRIEND_MESSAGE = "FRIENDNAME";
    public static final String GROUP_LIST = "GROUPLIST";

    // This will be updated by real values later.
    ArrayList<String> friendList = new ArrayList<>();
    ArrayList<String> selectedFriends = new ArrayList<>();
    // Initialising the gridview.
    GridView groupGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection);
        Intent intent = getIntent();
        // This allows for the people to be removed from the list once selected.
        if(intent.getStringArrayListExtra(GROUP_LIST) == null) {
            friendList.add("Diana");
            friendList.add("Peter");
            friendList.add("Bruce");
            friendList.add("Alice");
            friendList.add("Carol");
            friendList.add("Peter");
            friendList.add("Steve");
        } else {
            friendList = intent.getStringArrayListExtra(GROUP_LIST);
        }

        ListAdapter groupAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, friendList);
        groupGrid = (GridView) findViewById(R.id.groupList);
        groupGrid.setAdapter(groupAdapter);
        // Propogates the data from the last page to the next one.
        final String filmTitle  = intent.getStringExtra(FILM_MESSAGE);
        final String cinemaName = intent.getStringExtra(CINEMA_MESSAGE);
        // TODO: Propogate the array of people who have been selected.
        groupGrid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String friend = friendList.get(position);
                        friendList.remove(friend);
                        Intent intent = getIntent();
                        intent.putExtra(FILM_MESSAGE, filmTitle);
                        intent.putExtra(CINEMA_MESSAGE, cinemaName);
                        intent.putExtra(FRIEND_MESSAGE, friend);
                        intent.putExtra(GROUP_LIST, friendList);
                        startActivity(intent);
                    }
                }
        );

    }

}
