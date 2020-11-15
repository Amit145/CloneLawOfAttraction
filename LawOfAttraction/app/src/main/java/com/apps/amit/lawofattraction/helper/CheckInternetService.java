package com.apps.amit.lawofattraction.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetService {

    public NetworkInfo checkInternetConnection(Context applicationContext) {

        NetworkInfo netInfo = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager !=null && connectivityManager.getActiveNetworkInfo() != null){

            netInfo = connectivityManager.getActiveNetworkInfo();
        }

        return netInfo;
    }
}
