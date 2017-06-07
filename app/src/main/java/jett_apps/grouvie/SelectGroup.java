package jett_apps.grouvie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static jett_apps.grouvie.SelectFilm.CINEMA_MESSAGE;
import static jett_apps.grouvie.SelectFilm.FILM_MESSAGE;

public class SelectGroup extends AppCompatActivity {

    public static final String GROUP_LIST = "GROUPLIST";
    public static final String FRIEND_LIST = "FRIENDLIST";

    // This will be updated by real values later.
    static final String[] friends = new String[]{
            "Diana", "Peter", "Carol", "Steve"
    };

    ArrayList<String> selectedFriends = new ArrayList<>();

    String cinemaName;
    String filmTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        ListAdapter friendsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, friends);
        ListView friendsView = (ListView) findViewById(R.id.filmList);
        friendsView.setAdapter(friendsAdapter);

        friendsView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendsView.setItemsCanFocus(false);

    }

    public void onListItemClick(ListView parent, View v,int position,long id){
        CheckedTextView item = (CheckedTextView) v;
        Toast.makeText(this, friends[position] + " checked : " +
                item.isChecked(), Toast.LENGTH_SHORT).show();
    }


    public void finishGroupSelection(View view) {
        Intent intent = new Intent(this, SelectCinema.class);

        intent.putExtra(FILM_MESSAGE, filmTitle);
        intent.putExtra(CINEMA_MESSAGE, cinemaName);
        intent.putExtra(GROUP_LIST, selectedFriends);

        startActivity(intent);
    }
}


//
//
//        groupGrid = (GridView) findViewById(R.id.groupList);
//        final GroupAdapter groupAdapter = new GroupAdapter(friendList);
//        groupGrid.setAdapter(groupAdapter);
//        groupGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
//        groupGrid.setMultiChoiceModeListener(AbsListView.CHOICE_MODE_MULTIPLE_MODAL listner);
//    }
//
//        groupGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int selectedIndex = groupAdapter.selectedPositions.indexOf(position);
//                if (selectedIndex > -1) {
//                    groupAdapter.selectedPositions.remove(selectedIndex);
//                    ((CustomView)view).display(false);
//                } else {
//                    groupAdapter.selectedPositions.add(position);
//                    ((CustomView)view).display(true);
//                }
//            }
//        });
//    }
//
//    public class GroupAdapter extends BaseAdapter {
//
//        private String[] friends;
//        List<Integer> selectedPositions = new ArrayList<>();
//
//        GroupAdapter(String[] friends) {
//            this.friends = friends;
//        }
//        @Override
//        public int getCount() {
//            return friends.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return friends[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            CustomView customView = (convertView == null)?
//                                    new CustomView(SelectGroup.this) :
//                                    (CustomView) convertView;
//            customView.display(friends[position], selectedPositions.contains(position));
//            return customView;
//        }
//    }
//
//    class CustomView extends FrameLayout {
//
//        TextView textView;
//
//        public CustomView(Context context) {
//            super(context);
//            LayoutInflater.from(context).inflate(R.layout.activity_select_group, this);
//            textView = (TextView) getRootView().findViewById(R.id.textView);
//        }
//
//        public void display(String text, boolean isSelected) {
//            textView.setText(text);
//            display(isSelected);
//        }
//
//        public void display(boolean isSelected) {
//            textView.setBackgroundColor(isSelected? Color.RED : Color.LTGRAY);
//        }
//    }
//
//        // This allows for the people to be removed from the list once selected.
//        if(intent.getStringArrayListExtra(FRIEND_LIST) == null) {
//            friendList.add("Diana");
//            friendList.add("Peter");
//            friendList.add("Bruce");
//            friendList.add("Alice");
//            friendList.add("Carol");
//            friendList.add("Peter");
//            friendList.add("Steve");
//        } else {
//            friendList = intent.getStringArrayListExtra(FRIEND_LIST);
//        }
//
//        ListAdapter groupAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, friendList);
//
//        groupGrid = (GridView) findViewById(R.id.groupList);
//        groupGrid.setAdapter(groupAdapter);
//
//        // Propogates the data from the last page to the next one.
//        filmTitle  = intent.getStringExtra(FILM_MESSAGE);
//        cinemaName = intent.getStringExtra(CINEMA_MESSAGE);
//
//        groupGrid.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String friend = friendList.get(position);
//                        // Removes the selected friend from options.
//                        friendList.remove(friend);
//                        // Adds friend to current group.
//                        selectedFriends.add(friend);
//                        Intent intent = getIntent();
//                        // Propogates data.
//                        intent.putExtra(FILM_MESSAGE, filmTitle);
//                        intent.putExtra(CINEMA_MESSAGE, cinemaName);
//                        intent.putExtra(FRIEND_LIST, friendList);
//                        intent.putExtra(GROUP_LIST, selectedFriends);
//                        startActivity(intent);
//                    }
//                }
//        );
//
//    }
//
