package jett_apps.grouvie;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jett_apps.grouvie.LandingPage.DAY;
import static jett_apps.grouvie.LandingPage.DATE_MESSAGE;
import static jett_apps.grouvie.LandingPage.GROUP_LIST;
import static jett_apps.grouvie.LandingPage.MONTH;
import static jett_apps.grouvie.LandingPage.YEAR;

public class SelectGroup extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    // This will be updated by real values later.
    private ArrayAdapter<Friend> friendsAdapter;
    private ArrayList<Friend> friends;
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
        updateFriendSelectionList();

    }

    private void updateFriendSelectionList() {

        ListView listView = (ListView) findViewById(R.id.listView);

        friends = ProfileManager.getFriends(SelectGroup.this);
//        friends = (Friend[]) getLastNonConfigurationInstance();
//        if (friends == null) {
//            friends = new Friend[] {
//                    new Friend("Steve"),
//                    new Friend("Diana"),
//                    new Friend("Bruce"),
//                    new Friend("Carol")
//            };
//        }
//
//        ArrayList<Friend> friendList = new ArrayList<>();
//        friendList.addAll(Arrays.asList(friends));


        // Set our adapter as the ListView's adapter.
        friendsAdapter = new FriendsAdapter(this, friends);
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



    public void updateFriendsAfterPermission(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            updateFriends();
        }

    }

    private void updateFriends() {

        //Set difference with friendsList to obtain phone nums of users yet to use Grouvie
        ArrayList<String> grouvieContactsPhoneNum = new ArrayList<>();
        ArrayList<Friend> grouvieContacts = ProfileManager.getFriends(SelectGroup.this);
        for (Friend grouvieFriend : grouvieContacts) {
            grouvieContactsPhoneNum.add(grouvieFriend.getPhoneNum());
        }

        //Obtain all user contacts
        ArrayList<String> queryIfReg = getContactPhoneNums(SelectGroup.this);

        //Remove friend phone numbers from all user contacts to obtain phone numbers that may
        // need to be added to grouvieFriendsList
        queryIfReg.removeAll(grouvieContactsPhoneNum);

        //TODO: Erkin's Job: check which of queryIfReg numbers are registered using web server.
        //TODO: Gimme a list of Friends that need to be added to user's contacts
        ArrayList<Friend> regContactsToAdd = new ArrayList<>();

        //then add them to the user's contacts list
        ProfileManager.addFriends(regContactsToAdd, SelectGroup.this);

        updateFriendSelectionList();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                updateFriends();
            } else {
                Toast.makeText(this, "Refreshing contacts requires permission to your contacts"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<String> getContactPhoneNums(Context context) {

        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if(pCur != null){
            if(pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));
                    ArrayList<String> curPhones = new ArrayList<>();
                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);
                    }
                    curPhones.add(pCur.getString(pCur.getColumnIndex(PHONE_NUMBER)));
                    phones.put(contactId, curPhones);
                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,null);
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        ArrayList<String> contacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                            if(phones.containsKey(id)) {
                                contacts.addAll(phones.get(id));
                            }
                        }
                        return contacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }


    public void finishGroupSelection(View view) {

        Intent currIntent = getIntent();

        String chosenDate = currIntent.getStringExtra(DATE_MESSAGE);
        Integer chosenDay = currIntent.getIntExtra(DAY, 0);
        Integer chosenMonth = currIntent.getIntExtra(MONTH, 0);
        Integer chosenYear = currIntent.getIntExtra(YEAR, 1990);

        Intent intent = new Intent(this, SelectFilm.class);
        selectedFriends = new String[friends.size()];

        int j = 0;
        for (Friend friend : friends) {
            if (friend.isChecked()) {
                selectedFriends[j] = friend.getPhoneNum();
                friend.setChecked(false);
                j++;
            }
        }

        intent.putExtra(DATE_MESSAGE, chosenDate);
        intent.putExtra(DAY, chosenDay);
        intent.putExtra(MONTH, chosenMonth);
        intent.putExtra(YEAR, chosenYear);

        if (selectedFriends.length != 0) {
            intent.putExtra(GROUP_LIST, selectedFriends);
        } else {
            intent.putExtra(GROUP_LIST, "");
        }

        ServerContact.dialog = new ProgressDialog(SelectGroup.this, ProgressDialog.BUTTON_POSITIVE);
        ServerContact.dialog.setTitle("Please wait");
        ServerContact.dialog.setMessage("Obtaining listings from server");
        ServerContact.dialog.show();

        startActivity(intent);
    }
}
