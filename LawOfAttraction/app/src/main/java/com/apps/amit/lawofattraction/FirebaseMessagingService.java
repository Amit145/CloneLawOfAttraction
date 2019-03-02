package com.apps.amit.lawofattraction;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {

    String activityName = remoteMessage.getNotification().getClickAction();

    showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"),remoteMessage.getData().get("img_url"),activityName);
  }

  private void showNotification(String title,String message,String img_url,String activityName) {

    Intent i = new Intent(activityName);
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    builder.setAutoCancel(true);
    builder.setContentTitle(title);
    builder.setContentText(message);
    builder.setSmallIcon(R.drawable.status);
    builder.setContentIntent(pendingIntent);
    builder.setSound(sound);

    ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
      @Override
      public void onResponse(Bitmap response) {

        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

      }
    }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    });

    MySingleton.getmInstance(this).addToRequestQue(imageRequest);

  }


}
