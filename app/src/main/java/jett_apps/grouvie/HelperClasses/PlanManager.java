package jett_apps.grouvie.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

import jett_apps.grouvie.HelperObjects.Plan;

public class PlanManager {

    public static final String PLANS_KEY = "PLANS_KEY";


    //Obtain current plans
    public static ArrayList<Plan> getPlans(Context context) {
//        clearPlans(context);

        SharedPreferences sp = context.getSharedPreferences(PLANS_KEY, Context.MODE_PRIVATE);
        final Gson gson = new Gson();

        String empty_list = gson.toJson(new ArrayList<Plan>());

        ArrayList<Plan> mSelectedList = gson.fromJson(sp.getString(PLANS_KEY, empty_list),
                new TypeToken<ArrayList<Plan>>() {}.getType());

        if (mSelectedList == null) {
            mSelectedList = new ArrayList<>();
        }

        Collections.sort(mSelectedList);

//        mSelectedList.sort(new Comparator<Plan>() {
//            @Override
//            public int compare(Plan o1, Plan o2) {
//                boolean earlierMonth = o1.getSuggestedMonth() <= o2.getSuggestedMonth();
//                boolean earlierDay = o1.getSuggestedDay() <= o2.getSuggestedDay();
//
//                int earlierMonth = o1.getSuggestedMonth() - o2.getSuggestedMonth();
//                int earlierDay = o1.getSuggestedDay() - o2.getSuggestedDay();
//
//                return earlierMonth + earlierDay;
//            }
//        });

        return mSelectedList;

    }

    public static void addPlan(Plan plan, Context context) {

        //Obtain current plans
        ArrayList<Plan> currentPlans = getPlans(context);

        //Create new Gson to represent current plans with plan specified
        Gson gson = new Gson();

        if (currentPlans == null) {
            currentPlans = new ArrayList<>();
        }
        currentPlans.add(plan);

        String jsonString = gson.toJson(currentPlans);
        SharedPreferences sp = context.getSharedPreferences(PLANS_KEY, Context.MODE_PRIVATE);

        //Save it through sharedPreferences
        sp.edit().putString(PLANS_KEY, jsonString).apply();

    }

    public static void deletePlan(Plan plan, Context context) {
        //Obtain current plans
        ArrayList<Plan> currentPlans = getPlans(context);
        JSONArray json = new JSONArray(currentPlans);

        //Create new Gson to represent current plans without plan specified
        Gson gson = new Gson();
        currentPlans.remove(plan);
        String jsonString = gson.toJson(currentPlans);
        SharedPreferences sp = context.getSharedPreferences(PLANS_KEY, Context.MODE_PRIVATE);

        //Save it through sharedPreferences
        sp.edit().putString(PLANS_KEY, jsonString).apply();

    }

    //Clear current plans
    public static void clearPlans(Context context) {

        //Obtain current plans
        ArrayList<Plan> currentPlans = new ArrayList<>();

        //Create new Gson to represent current plans
        Gson gson = new Gson();
        String jsonString = gson.toJson(currentPlans);
        SharedPreferences sp = context.getSharedPreferences(PLANS_KEY, Context.MODE_PRIVATE);

        //Save it through sharedPreferences
        sp.edit().putString(PLANS_KEY, jsonString).apply();

    }


}
