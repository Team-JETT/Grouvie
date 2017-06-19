package jett_apps.grouvie.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jett_apps.grouvie.HelperObjects.Friend;
import jett_apps.grouvie.HelperObjects.Plan;
import jett_apps.grouvie.R;
import jett_apps.grouvie.Views.LandingPage;

import static jett_apps.grouvie.Views.LandingPage.SENT_PLAN;

public class MessagingService extends FirebaseMessagingService {

    private Plan plan;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        JSONObject planInJSON = new JSONObject(remoteMessage.getData());
        String leaderName = null;
        /* Parse the data in the JSON file sent and create a plan from it to send to
           the LandingPage. */
        try {
            leaderName = planInJSON.getString("leader");
            String film = planInJSON.getString("film");
            String cinema = planInJSON.getString("cinema");
            String showtime = planInJSON.getString("showtime");
            String date = planInJSON.getString("date");
            /* Sending a Friend object is difficult/not possible in JSON, so we will need to
               get the arrays of the friend's names and phoneNumbers and recreate the
               ArrayList<Friend> object here.*/
            ArrayList<Friend> members = new ArrayList<>();
            String[] friendNumbers = planInJSON.getString("friends").split(",");
            String[] friendNames = planInJSON.getString("friend_list").split(",");
            int numOfContacts = friendNames.length;
            for (int i = 0; i < numOfContacts; i++) {
                String name = friendNames[i];
                String number = friendNumbers[i];
                /* Because we called Arrays.toString for the friend's names and phone numbers in
                   LeaderInitialPlan, we need to account for the leading "[" in the first friend
                   name and phone number and the trailing "]" in the last friend name and phone
                   number. */
                if (i == 0) {
                    /* Remove the leading "[" in both the name and phone number. */
                    name = name.substring(1);
                    number = number.substring(1);
                } else if (i == numOfContacts - 1) {
                    /* Remove the trailing "]" in both the name and phone number. */
                    name = name.substring(0, name.length() - 1);
                    number = number.substring(0, number.length() - 1);
                }
                members.add(new Friend(name, number));
            }
            String leaderPhoneNum = planInJSON.getString("leader");
            plan = new Plan(film, cinema, showtime, date, members, leaderPhoneNum);
        } catch (JSONException e) {
            Log.e("BAD JSON SENT", "JSON sent to user was not an event plan");
            e.printStackTrace();
        }
        /* Calling method to generate notification. */
        String messageBody = ((leaderName == null) ? "Someone" : leaderName.trim()) +
                " has created a new plan. Click here to accept it!";
        sendNotification(messageBody);
    }

    /* Generate push notification. */
    private void sendNotification(String messageBody) {
        /* Create an intent to store in the notification. */
        Intent intent = new Intent(this, LandingPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /* Send plan through intent. This should be retrieved in LandingPage. */
        intent.putExtra(SENT_PLAN, plan);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        /* Add notification sound. */
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        /* Add notification title. */
        String notifyTitle = "Grouvie Time";

        /* Generate the notification. */
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notifyTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(messageBody));
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* Create push notification at the top of the notifications bar */
        notificationManager.notify(0, notificationBuilder.build());
    }
    
}