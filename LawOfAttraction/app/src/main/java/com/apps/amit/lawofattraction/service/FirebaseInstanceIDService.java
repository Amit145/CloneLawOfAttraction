package com.apps.amit.lawofattraction.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseMessagingService {

  @Override
  public void onNewToken(String userToken) {

    FirebaseTokenGenerator firebaseTokenGenerator = new FirebaseTokenGenerator();
    String fetchToken = firebaseTokenGenerator.getFirebaseToken();

    registerToken(fetchToken);
  }

  private void registerToken(String token) {

    OkHttpClient client = new OkHttpClient();
    RequestBody body = new FormBody.Builder()
            .add("Token",token)
            .build();

    Request request = new Request.Builder()
            .url("http://www.innovativelabs.xyz/register.php")
            .post(body)
            .build();

    try {
      client.newCall(request).execute();
    } catch (IOException e) {

      Log.e(e.getMessage(),e.getMessage());
    }

  }
}
