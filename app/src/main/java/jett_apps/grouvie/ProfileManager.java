package jett_apps.grouvie;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ProfileManager {

    private static final String USER_PROFILE_ID = "userprofile_id";
    private static final String USER_FRIENDS_ID = "userfriends_id";

    public static void updateAll(Context c, String name, String phone, String postcode) {

        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("name", name);
        sp.putString("phone", phone);
        sp.putString("postcode", postcode);
        sp.apply();

    }

    public static String getName(Context c) {
        SharedPreferences sp = c.getSharedPreferences(USER_PROFILE_ID, MODE_PRIVATE);
        return sp.getString("name", null);
    }

    public static void setName(Context c, String name) {
        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("name", name);
        sp.apply();
    }

    public static String getPhone(Context c) {
        SharedPreferences sp = c.getSharedPreferences(USER_PROFILE_ID, MODE_PRIVATE);
        return sp.getString("phone", null);
    }

    public static void setPhone(Context c, String phone) {
        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("phone", phone);
        sp.apply();
    }

    public static String getPostcode(Context c) {
        SharedPreferences sp = c.getSharedPreferences(USER_PROFILE_ID, MODE_PRIVATE);
        return sp.getString("postcode", null);
    }

    public static void setPostcode(Context c, String postcode) {
        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("postcode", postcode);
        sp.apply();
    }

    //Add friends to friendList
    public static void addFriends(ArrayList<Friend> contactsToAdd, Context context) {

        //Obtain current friends
        ArrayList<Friend> currentFriends = getFriends(context);

        //Create new Gson to represent current friends including new friend
        Gson gson = new Gson();

        if (currentFriends == null) {
            currentFriends = new ArrayList<>();
        }
        currentFriends.addAll(contactsToAdd);

        String jsonString = gson.toJson(currentFriends);
        SharedPreferences sp = context.getSharedPreferences(USER_FRIENDS_ID, Context.MODE_PRIVATE);

        //Save it through sharedPreferences
        sp.edit().putString(USER_FRIENDS_ID, jsonString).apply();
    }

    //Obtain current friends
    public static ArrayList<Friend> getFriends(Context context) {

        SharedPreferences sp = context.getSharedPreferences(USER_FRIENDS_ID, Context.MODE_PRIVATE);
        final Gson gson = new Gson();

        String empty_list = gson.toJson(new ArrayList<Friend>());

        ArrayList<Friend> friendList = gson.fromJson(sp.getString(USER_FRIENDS_ID, empty_list),
                new TypeToken<ArrayList<Friend>>() {}.getType());

        if (friendList == null) {
            friendList = new ArrayList<>();
            String jsonString = gson.toJson(friendList);
            sp.edit().putString(USER_FRIENDS_ID, jsonString).apply();
        }

        //TODO: Remove this debugging section below
        if (friendList.isEmpty()) {
            friendList = new ArrayList<>();
            friendList.add(new Friend("Tarun", "07264006128"));
            friendList.add(new Friend("Erkin", "07264006128"));
            friendList.add(new Friend("Jay", "07962006128"));
            friendList.add(new Friend("Tanmay", "02964006128"));

            String jsonString = gson.toJson(friendList);
            sp.edit().putString(USER_FRIENDS_ID, jsonString).apply();
        }

        return friendList;
    }

}
