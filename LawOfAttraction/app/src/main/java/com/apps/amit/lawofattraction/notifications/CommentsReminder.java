package com.apps.amit.lawofattraction.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.apps.amit.lawofattraction.HomeActivity;
import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.CommentsActivity;

import static com.apps.amit.lawofattraction.SetReminderActivity.p1;

/**
 * Created by amit on 27/3/18.
 */

public class CommentsReminder extends BroadcastReceiver {


    int notificationId = 236;
    String channelID = "my_channel_01";
    CharSequence name = "my_channel";
    String description = "This is my channel";

    @Override
    public void onReceive(Context context, Intent arg2) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);

            SharedPreferences sp1 = context.getSharedPreferences("timerEnable", context.MODE_PRIVATE);
            String userName = sp1.getString("userName","");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, "\t SAY TO THE UNIVERSE", p1)
                    .setContentTitle("Hi "+userName+", Say Your Wish To The Universe !!")
                    .setContentText("See What Other's Are Manifesting !!")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");


            Intent resultIntent = new Intent(context, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            notificationManager.notify(notificationId, builder.build());
        }

        else {

            Intent i = new Intent(context, CommentsActivity.class);
            PendingIntent p1 = PendingIntent.getActivity(context, 0, i, 0);
            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context);
            builder1.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, "\t SAY TO THE UNIVERSE", p1)
                    .setContentTitle("Say Your Wish To The Universe !!")
                    .setContentText("See What Other's Are Manifesting !!")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            notificationManager.notify(699, builder1.build());
        }

    }
}
