package jett_apps.grouvie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jett_apps.grouvie.LandingPage.CINEMA_DATA;
import static jett_apps.grouvie.LandingPage.DAY_MESSAGE;
import static jett_apps.grouvie.LandingPage.FILM_MESSAGE;
import static jett_apps.grouvie.LandingPage.USER_NAME;
import static jett_apps.grouvie.LandingPage.GROUP_LIST;

public class SelectGroup extends AppCompatActivity {



    // This will be updated by real values later.
    private Friend[] friends;
    private ArrayAdapter<Friend> friendsAdapter;
    private String[] selectedFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        // Finds the listView resource.
        ListView listView = (ListView) findViewById(R.id.listView);
        // When item is tapped, checkBox and Friend object are updated.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = friendsAdapter.getItem(position);
                friend.toggleChecked();
                // To display friend that is checked.
                FriendViewHolder viewHolder = (FriendViewHolder) view.getTag();
                viewHolder.getCheckBox().setChecked(friend.isChecked());
            }
        });
        // Populates the friends list with friend objects.
        friends = (Friend[]) getLastNonConfigurationInstance();
        if (friends == null) {
            friends = new Friend[] {
                    new Friend("Steve"),
                    new Friend("Diana"),
                    new Friend("Bruce"),
                    new Friend("Carol")
            };
        }

        ArrayList<Friend> friendList = new ArrayList<>();
        friendList.addAll(Arrays.asList(friends));
        // Set our adapter as the ListView's adapter.
        friendsAdapter = new FriendsAdapter(this, friendList);
        listView.setAdapter(friendsAdapter);

    }

    // Adapter for displaying an array of Friend objects.
    private static class FriendsAdapter extends ArrayAdapter<Friend> {

        private LayoutInflater inflater;

        public FriendsAdapter(Context context, List<Friend> friendList) {
            super(context, R.layout.simplerow, R.id.rowTextView, friendList);
            inflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Friend friend = this.getItem(position);

            CheckBox checkBox;
            TextView textView;
            // Creates a new row view.
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.simplerow, null);

                textView = (TextView) convertView.findViewById(R.id.rowTextView);
                checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
                // Tagging the row with the child view means that it's easier
                // to access later on.
                convertView.setTag(new FriendViewHolder(textView, checkBox));

                checkBox.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Friend friend = (Friend) cb.getTag();
                        friend.setChecked(cb.isChecked());
                    }
                });
            } else {
                FriendViewHolder viewHolder = (FriendViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox();
                textView = viewHolder.getTextView();
            }

            checkBox.setTag(friend);

            checkBox.setChecked(friend.isChecked());
            textView.setText(friend.getName());

            return convertView;
        }

    }

    // Object holding the child views of the rows.
    private static class FriendViewHolder {
        private CheckBox checkBox;
        private TextView textView;

        public FriendViewHolder(TextView textView, CheckBox checkBox) {
            this.checkBox = checkBox;
            this.textView = textView;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }

    public Object onRetainCustomNonConfigurationInstance() {
        return friends;
    }

    private static class Friend {
        private String name = "";
        private boolean checked = false;

        public Friend(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String toString() {
            return name;
        }
        public void toggleChecked() {
            checked = !checked;
        }
    }



    public void finishGroupSelection(View view) {
        Intent currIntent = getIntent();
        String filmTitle = currIntent.getStringExtra(FILM_MESSAGE);
        String chosenDay = currIntent.getStringExtra(DAY_MESSAGE);
        String cinemaData = currIntent.getStringExtra(CINEMA_DATA);
        String user_name = currIntent.getStringExtra(USER_NAME);
        Intent intent = new Intent(this, SelectCinema.class);
        selectedFriends = new String[friends.length];

        int j = 0;
        for (int i = 0; i < friends.length; i++) {
            if(friends[i].isChecked()) {
                selectedFriends[j] = friends[i].getName();
                j++;
            }
        }

        intent.putExtra(FILM_MESSAGE, filmTitle);
        intent.putExtra(DAY_MESSAGE, chosenDay);
        intent.putExtra(CINEMA_DATA, cinemaData);
        intent.putExtra(USER_NAME, user_name);
        intent.putExtra(GROUP_LIST, selectedFriends);

        startActivity(intent);
    }
}
