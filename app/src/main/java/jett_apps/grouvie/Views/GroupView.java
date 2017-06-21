package jett_apps.grouvie.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import jett_apps.grouvie.Adapters.CustomGroupAdapter;
import jett_apps.grouvie.HelperClasses.ProfileManager;
import jett_apps.grouvie.HelperClasses.ServerContact;
import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.R;

import static jett_apps.grouvie.Views.LandingPage.DATA;

public class GroupView extends AppCompatActivity {

    private Plan p;
    ArrayList<Friend> chosenFriends = new ArrayList<>();
    private ListAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        p = (Plan) getIntent().getSerializableExtra(DATA);

        Button confirmPlan = (Button) findViewById(R.id.confirmPlan);

        if(ProfileManager.getPhone(this).equals(p.getLeaderPhoneNum())) {
            confirmPlan.setVisibility(View.VISIBLE);
        } else {
            confirmPlan.setVisibility(View.INVISIBLE);
        }

        JSONObject json = new JSONObject();
        String result;
        JSONObject group_replies = null;
        try {
            json.accumulate("leader", p.getLeaderPhoneNum());
            json.accumulate("creation_datetime", p.getCreationDateTime());
            result = new ServerContact().execute("group_replies", json.toString()).get();
            group_replies = new JSONObject(result);
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        chosenFriends = p.getEventMembers();
        if (group_replies != null) {


            for (int i = 0; i < chosenFriends.size(); i++) {
                Friend friend = chosenFriends.get(i);
                String phoneNum = friend.getPhoneNum();

                String film = null;
                String time = null;
                String date = null;
                String cinema = null;
                boolean hasAccepted = false;

                JSONObject changes = null;
                try {
                    changes = group_replies.getJSONObject(phoneNum);
                    film = changes.getString("film");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    time = changes.getString("showtime");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    date = changes.getString("date");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    cinema = changes.getString("cinema");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String b = null;
                try {
                    b = changes.getString("accepted");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hasAccepted = Boolean.parseBoolean(b);

                friend.setFilmChange(film);
                friend.setTimeChange(time);
                friend.setDateChange(date);
                friend.setCinemaChange(cinema);
                friend.setHasAccepted(hasAccepted);
            }

            p.setEventMembers(chosenFriends);
        }

        groupAdapter = new CustomGroupAdapter(GroupView.this, chosenFriends);

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                groupAdapter = new CustomGroupAdapter(GroupView.this, chosenFriends);
                handler.postDelayed( this, 1000 );
            }
        }, 1000 );

        ListView groupListView = (ListView) findViewById(R.id.groupView);
        groupListView.setAdapter(groupAdapter);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend f = chosenFriends.get(position);
                if (!f.hasAccepted()) {
                    changesMadeByFriend(f);
                }
            }
        });

    }

    public void changesMadeByFriend(Friend friend) {

        String film = friend.getFilmChange();
        String time = friend.getTimeChange();
        String date = friend.getDateChange() ;
        String cinema = friend.getCinemaChange();

        AlertDialog.Builder builder = new AlertDialog.Builder(GroupView.this);
        builder.setCancelable(true);
        builder.setTitle(friend.getName());

        builder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        String message = "";
        if(film !=null) {
            message +="Suggested Film: " + film;
        }
        if(time !=null) {
            message += "\n" + "Suggested Time: " + time;
        }
        if(date !=null) {
            message += "\n" + "Suggested Date: " + date;
        }
        if(cinema !=null) {
            message += "\n" + "Suggested Cinema: " + cinema;
        }

        dialog.setMessage(message);
        dialog.show();
    }

    public void confirmPlan(View view) {
        JSONObject json = new JSONObject();
        try {
            json.accumulate("leader", p.getLeaderPhoneNum());
            json.accumulate("creation_datetime", p.getCreationDateTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ServerContact().execute("confirm_plan", json.toString());

        Intent intent = new Intent(view.getContext(), LandingPage.class);
        startActivity(intent);
    }

}
