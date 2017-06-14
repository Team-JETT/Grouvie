package jett_apps.grouvie;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class ProfileManager {

    private static final String USER_PROFILE_ID = "userprofile_id";

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

    public static void setLongitude(Context c, String longitude) {
        SharedPreferences.Editor sp = c
                .getSharedPreferences(USER_PROFILE_ID, Context.MODE_PRIVATE)
                .edit();

        sp.putString("longitude", longitude);
        sp.apply();
    }
}
