package com.apps.amit.lawofattraction.notifications;

/**
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
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.apps.amit.lawofattraction.HomeActivity;
import com.apps.amit.lawofattraction.R;

import static com.apps.amit.lawofattraction.SetReminderActivity.p1;

public class Notify extends BroadcastReceiver {

  String channelID = "my_channel_01";
  CharSequence name = "my_channel";
  String description = "This is my channel";

  @Override
  public void onReceive(Context context, Intent arg2) {

    int notificationID = 235;
    NotificationManager mngr1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel mChannel = new NotificationChannel(channelID, name, importance);
      mChannel.setDescription(description);
      mChannel.enableLights(true);
      mChannel.setLightColor(Color.RED);
      mChannel.enableVibration(true);
      mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
      mChannel.setShowBadge(false);
      mngr1.createNotificationChannel(mChannel);

      SharedPreferences sp1 = context.getSharedPreferences("timerEnable", context.MODE_PRIVATE);
      String naam = sp1.getString("userName","");

      NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
              .setDefaults(Notification.DEFAULT_ALL)
              .setWhen(System.currentTimeMillis())
              .setSmallIcon(R.drawable.status)
              .setAutoCancel(true)
              .addAction(R.drawable.ic_play_arrow_white_24dp, "\t CLICK TO START", p1)
              .setContentTitle("Hi "+naam+", Have You Done Your Manifestation ?")
              .setContentText("Manifest Daily To Pursue Your Dreams !!")
              .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
              .setContentInfo("");

      Intent resultIntent = new Intent(context, HomeActivity.class);
      TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
      stackBuilder.addParentStack(HomeActivity.class);
      stackBuilder.addNextIntent(resultIntent);
      PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

      builder.setContentIntent(resultPendingIntent);

      mngr1.notify(notificationID, builder.build());
    }


    else {


      Intent i = new Intent(context, HomeActivity.class);
      PendingIntent p1 = PendingIntent.getActivity(context, 0, i, 0);
      NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context);
      builder1.setAutoCancel(true)
              .setDefaults(Notification.DEFAULT_ALL)
              .setWhen(System.currentTimeMillis())
              .setSmallIcon(R.drawable.status)
              .setAutoCancel(true)
              .addAction(R.drawable.ic_play_arrow_white_24dp, "\t CLICK TO START", p1)
              .setContentTitle("Have You Done Your Manifestation ?")
              .setContentText("Manifest Daily To Pursue Your Dreams !!")
              .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
              .setContentInfo("");

      mngr1.notify(99, builder1.build());
    }

  }
}
