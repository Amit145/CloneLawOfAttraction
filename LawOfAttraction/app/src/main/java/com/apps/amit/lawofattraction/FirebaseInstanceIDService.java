package com.apps.amit.lawofattraction;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {

    String token = FirebaseInstanceId.getInstance().getToken();

    registerToken(token);
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
      e.printStackTrace();
    }

  }
}
