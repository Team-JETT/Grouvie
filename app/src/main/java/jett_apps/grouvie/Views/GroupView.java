package jett_apps.grouvie.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.PLAN_MESSAGE;

public class GroupView extends AppCompatActivity {

    private Plan p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        p = (Plan) getIntent().getSerializableExtra(PLAN_MESSAGE);

        final ArrayList<Friend> chosenFriends = p.getEventMembers();

        ListAdapter groupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, extractNames(chosenFriends));
        ListView groupListView = (ListView) findViewById(R.id.groupView);
        groupListView.setAdapter(groupAdapter);

        groupListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        );


    }


    public ArrayList<String> extractNames(ArrayList<Friend> list) {
        ArrayList<String> friends = new ArrayList<>();
        for (int i=0; i<list.size(); i++) {
            friends.add(list.get(i).getName());
        }
        return friends;
    }
}
