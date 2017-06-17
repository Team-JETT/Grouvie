package jett_apps.grouvie.Activities;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jett_apps.grouvie.Adapters.CustomFriendAdapter;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperObjects.FriendView;
import jett_apps.grouvie.HelperObjects.PropagationObject;
import jett_apps.grouvie.R;
import jett_apps.grouvie.HelperClasses.ServerContact;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class SelectGroup extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    // This will be updated by real values later.
    private ArrayAdapter<Friend> friendsAdapter;
    private ArrayList<Friend> friends;
    private ArrayList<Friend> selectedFriends;

    private PropagationObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        data = (PropagationObject) getIntent().getSerializableExtra(DATA);

        // Finds the listView resource.
        ListView listView = (ListView) findViewById(R.id.listView);
        // When item is tapped, checkBox and Friend object are updated.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = friendsAdapter.getItem(position);
                friend.toggleChecked();
                // To display friend that is checked.
                FriendView viewHolder = (FriendView) view.getTag();
                viewHolder.getCheckBox().setChecked(friend.isChecked());
            }
        });

        updateFriendsAuto();

        // Populates the friends list with friend objects.
        updateFriendSelectionList();

    }

    private void updateFriendSelectionList() {

        ListView listView = (ListView) findViewById(R.id.listView);

        friends = ProfileManager.getFriends(SelectGroup.this);


        // Set our adapter as the ListView's adapter.
        friendsAdapter = new CustomFriendAdapter(this, friends);
        listView.setAdapter(friendsAdapter);
    }



    public Object onRetainCustomNonConfigurationInstance() {
        return friends;
    }

    public void updateFriendsAuto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            try {
                updateFriends();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateFriends() throws JSONException {

        //Set difference with friendsList to obtain phone nums of users yet to use Grouvie
        ArrayList<String> grouvieContactsPhoneNum = new ArrayList<>();
        ArrayList<Friend> grouvieContacts = ProfileManager.getFriends(SelectGroup.this);
        for (Friend grouvieFriend : grouvieContacts) {
            grouvieContactsPhoneNum.add(grouvieFriend.getPhoneNum());
        }
        Log.v("Grouvie contacts:", grouvieContactsPhoneNum.toString());

        //Obtain all user contacts
        ArrayList<String> queryIfReg = getContactPhoneNums(SelectGroup.this);
        Log.v("PHONE CONTACTS:", queryIfReg.toString());
        // Remove friend phone numbers from all user contacts to obtain phone numbers that may
        // need to be added to grouvieFriendsList
        queryIfReg.removeAll(grouvieContactsPhoneNum);

        String[] possible_grouvie_contacts = queryIfReg.toArray(new String[queryIfReg.size()]);
        Log.v("POSSIBLE GROUVIES:", Arrays.toString(possible_grouvie_contacts));
        String result = null;
        try {
            result = new ServerContact().execute("verify_friends", Arrays.toString(possible_grouvie_contacts)).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to verify friends");
            e.printStackTrace();
        }
        JSONObject json_data = null;
        json_data = new JSONObject(result);

        ArrayList<Friend> regContactsToAdd = new ArrayList<>();
        Iterator<String> keys = json_data.keys();
        while (keys.hasNext()) {
            String phone_number = keys.next();
            regContactsToAdd.add(new Friend(json_data.getString(phone_number),
                                            phone_number));
        }

        // then add them to the user's contacts list
        ProfileManager.addFriends(regContactsToAdd, SelectGroup.this);

        updateFriendSelectionList();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                try {
                    updateFriends();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


                        ArrayList<String> validContacts = new ArrayList<>();

                        //Remove erroneous phone numbers and all should have same format
                        for (String phoneNum : contacts) {

                            //Remove spaces
                            phoneNum = phoneNum.replaceAll("\\s+","");

                            //Remove dashes
                            phoneNum.replaceAll("\\D", "");

//                          //Convert international phone numbers to UK local
                            if (phoneNum.startsWith("00")) {
                                phoneNum = phoneNum.substring(2);
                                phoneNum = "+" + phoneNum;
                            }

//                          //Convert international phone numbers to UK local
                            if (phoneNum.startsWith("+44")) {
                                phoneNum = phoneNum.substring(3);
                                phoneNum = "0" + phoneNum;
                            }

                            //Remove any non-mobile phone numbers
                            if (!phoneNum.startsWith("07")) {
                                continue;
                            }

                            //If phone number isn't valid in terms of length
                            if (phoneNum.length() != 11) {
                                continue;
                            }

                            if (phoneNum.equals(ProfileManager.getPhone(SelectGroup.this))) {
                                continue;
                            }

                            validContacts.add(phoneNum);
                        }

                        return validContacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
//        Friend Jay = new Friend("Jay", "077777777");
//        Friend Tarun = new Friend("Tarun", "0888888888");
//        Friend Tanmay = new Friend("Tanmay", "09999999999");
//        ArrayList<Friend> friends = new ArrayList<>();
//        friends.add(Jay);
//        friends.add(Tarun);
//        friends.add(Tanmay);
//        return new ArrayList<>(Arrays.asList("07777777777", "08888888888", "09999999999"));
        return null;
    }


    public void finishGroupSelection(View view) {

        Intent intent = new Intent(this, SelectFilm.class);
        selectedFriends = new ArrayList<>();

        int j = 0;
        for (Friend friend : friends) {
            if (friend.isChecked()) {
                selectedFriends.add(friend);
                friend.setChecked(false);
                j++;
            }
        }

        data.setSelectedFriends(selectedFriends);

        intent.putExtra(DATA, data);

        ServerContact.dialog = new ProgressDialog(SelectGroup.this, ProgressDialog.BUTTON_POSITIVE);
        ServerContact.dialog.setTitle("Please wait");
        ServerContact.dialog.setMessage("Obtaining listings from server");
        ServerContact.dialog.show();

        startActivity(intent);
    }
}
