package com.apps.amit.lawofattraction;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.apps.amit.lawofattraction.helper.LocaleHelper;


import static android.content.Context.MODE_PRIVATE;
import static com.apps.amit.lawofattraction.timepicker.p1;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {



        int notificationID = 234;
        NotificationManager mngr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences sp1 = context.getSharedPreferences("timerEnable", MODE_PRIVATE);
        String naam = sp1.getString("userName","");
        String [] name1 = naam.split(" ");

        //Store selected language in a Variable called value
        String value = "en";

        context = LocaleHelper.setLocale(context, value);
        Resources resources = context.getResources();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String channelID = "my_channel_01";
            CharSequence name = "my_channel";
            String description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);

            if(mngr!=null ){
                mngr.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, resources.getString(R.string.Home_clickToStart), p1)
                    .setContentTitle("Law Of Attraction Daily")
                    .setContentText(resources.getString(R.string.Home_textBelowPager1)+" "+name1[0]+" "+resources.getString(R.string.Home_Notification))
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            Intent resultIntent = new Intent(context, Home.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Home.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            if(mngr!=null ){

                mngr.notify(notificationID, builder.build());
            }

        }


        else {
            String channelID = "my_channel_02";
            Intent i = new Intent(context, Home.class);
            PendingIntent p1 = PendingIntent.getActivity(context, 0, i, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelID);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, resources.getString(R.string.Home_clickToStart), p1)
                    .setContentTitle("Law Of Attraction Daily")
                    .setContentText(resources.getString(R.string.Home_textBelowPager1)+" "+name1[0]+" "+resources.getString(R.string.Home_Notification))
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");

            if(mngr!=null ){

                mngr.notify(999, builder.build());
            }

        }
    }
}
