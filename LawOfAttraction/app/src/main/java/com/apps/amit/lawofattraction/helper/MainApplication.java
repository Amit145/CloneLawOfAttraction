package com.apps.amit.lawofattraction.helper;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AudienceNetworkAds;

public class MainApplication extends Application {

	private static MainApplication sInstance;
	private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();

		if (AudienceNetworkAds.isInAdsProcess(this)) {
			return;
		}
		// Initialize the Audience Network SDK
		AudienceNetworkAds.initialize(this);

		// initialize the singleton
		sInstance = this;


	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
	}

	public static synchronized MainApplication getInstance() {
		return sInstance;
	}



	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}
}
