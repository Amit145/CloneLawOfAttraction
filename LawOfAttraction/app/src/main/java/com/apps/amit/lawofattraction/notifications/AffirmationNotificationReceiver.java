package com.apps.amit.lawofattraction.notifications;

/*
 * Created by amit on 6/2/18.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import com.apps.amit.lawofattraction.AffirmationHome;
import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import static android.content.Context.MODE_PRIVATE;
import static com.apps.amit.lawofattraction.SetReminderActivity.p1;

public class AffirmationNotificationReceiver extends BroadcastReceiver {

    String [] userNameArray;
    static final String APP_NAME = "Law Of Attraction Daily";
    int notificationID = 2341;
    String channelID = "my_channel_011";
    String channelID1 = "my_channel_021";
    CharSequence name = "my_channel1";
    String description = "This is my channel1";

    @Override
    public void onReceive(Context context, Intent arg1) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences sp1 = context.getSharedPreferences("timerEnable", MODE_PRIVATE);
        String userName = sp1.getString("userName", "");

        if (userName != null) {
            userNameArray = userName.split(" ");

        //Store selected language in a Variable called value
        String value = "en";
        context = LocaleHelper.setLocale(context, value);
        Resources resources = context.getResources();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, resources.getString(R.string.Home_clickToStart), p1)
                    .setContentTitle(APP_NAME)
                    .setContentText(resources.getString(R.string.Home_textBelowPager1) + " " + userNameArray[0] + " It's time for your Affirmation")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            Intent resultIntent = new Intent(context, AffirmationHome.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AffirmationHome.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            if (notificationManager != null) {
                notificationManager.notify(notificationID, builder.build());
            }
        } else {

            Intent i = new Intent(context, AffirmationHome.class);
            PendingIntent p1 = PendingIntent.getActivity(context, 0, i, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID1);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, resources.getString(R.string.Home_clickToStart), p1)
                    .setContentTitle(APP_NAME)
                    .setContentText(resources.getString(R.string.Home_textBelowPager1) + " " + userNameArray[0] + " It's time for your Affirmation")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            if (notificationManager != null) {

                notificationManager.notify(9991, builder.build());
            }
        }
    }
    }
}
