package com.apps.amit.lawofattraction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import static com.apps.amit.lawofattraction.timepicker.p1;
import static com.apps.amit.lawofattraction.timepicker.p2;
/**
 * Created by amit on 27/3/18.
 */

public class CommentsReminder extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent arg2) {
        //Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        int NOTIFICATION_ID = 236;
        NotificationManager mngr1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            mngr1.createNotificationChannel(mChannel);

            SharedPreferences sp1 = context.getSharedPreferences("timerEnable", context.MODE_PRIVATE);
            String naam = sp1.getString("userName","");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.status)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play_arrow_white_24dp, "\t SAY TO THE UNIVERSE", p1)
                    .setContentTitle("Hi "+naam+", Say Your Wish To The Universe !!")
                    .setContentText("See What Other's Are Manifesting !!")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("");


            Intent resultIntent = new Intent(context, Home.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Home.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            mngr1.notify(NOTIFICATION_ID, builder.build());
        }


        else {


            Intent i = new Intent(context, comments.class);
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

            //builder.setContentIntent= PendingIntent.getActivity(this,0, new Intent(AlarmReceiver.this,Home.class),PendingIntent.FLAG_UPDATE_CURRENT);

            mngr1.notify(699, builder1.build());
        }

    }
}
