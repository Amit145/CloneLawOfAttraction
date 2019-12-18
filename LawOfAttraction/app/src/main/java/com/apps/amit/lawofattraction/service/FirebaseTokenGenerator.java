package com.apps.amit.lawofattraction.service;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.apps.amit.lawofattraction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class FirebaseTokenGenerator {

    private String token = null;

    public String getFirebaseToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());

                } else {

                    if(task.getResult()!=null) {
                        // Get new Instance ID token
                         token = task.getResult().getToken();
                    }
                }
            }
        });

        return token;
    }
}
