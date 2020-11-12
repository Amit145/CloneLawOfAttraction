package com.apps.amit.lawofattraction.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetService {

    public NetworkInfo checkInternetConnection(ConnectivityManager connectivityManager, Context applicationContext) {

        NetworkInfo netInfo = null;
        connectivityManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null && connectivityManager.getActiveNetworkInfo() != null){

            netInfo = connectivityManager.getActiveNetworkInfo();
        }

        return netInfo;
    }
}
