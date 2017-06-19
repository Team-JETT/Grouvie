package jett_apps.grouvie.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;

import jett_apps.grouvie.HelperObjects.Friend;

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

    public static void updateAll(Context c, String name, String phone, String latitude,
                                 String longitude) {

        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();
        sp.putString("name", name);
        sp.putString("phone", phone);
        sp.putString("latitude", latitude);
        sp.putString("longitude", longitude);
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

    public static String getLatitude(Context c) {
        SharedPreferences sp = c.getSharedPreferences(USER_PROFILE_ID, MODE_PRIVATE);
        return sp.getString("latitude", null);
    }

    public static void setLatitude(Context c, String latitude) {
        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("latitude", latitude);
        sp.apply();
    }

    public static String getLongitude(Context c) {
        SharedPreferences sp = c.getSharedPreferences(USER_PROFILE_ID, MODE_PRIVATE);
        return sp.getString("longitude", null);
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
//        clearFriends(context);

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
//        if (friendList.isEmpty()) {
//            friendList = new ArrayList<>();
//            friendList.add(new Friend("Tarun", "07964006128"));
//            friendList.add(new Friend("Erkin", "07587247113"));
//            friendList.add(new Friend("Jay", "07942 948248"));
//
//            String jsonString = gson.toJson(friendList);
//            sp.edit().putString(USER_FRIENDS_ID, jsonString).apply();
//        }
//        friendList.remove(2);

        friendList.sort(new Comparator<Friend>() {
            @Override
            public int compare(Friend o1, Friend o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return friendList;
    }

    //Clear all grouvieFriendsList
    public static void clearFriends(Context context) {
        //Obtain current friends
        ArrayList<Friend> currentFriends = new ArrayList<>();

        //Create new Gson to represent current friends including new friend
        Gson gson = new Gson();

        String jsonString = gson.toJson(currentFriends);
        SharedPreferences sp = context.getSharedPreferences(USER_FRIENDS_ID, Context.MODE_PRIVATE);

        //Save it through sharedPreferences
        sp.edit().putString(USER_FRIENDS_ID, jsonString).apply();
    }

    public static void setLongitude(Context c, String longitude) {
        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("longitude", longitude);
        sp.apply();
    }
}
