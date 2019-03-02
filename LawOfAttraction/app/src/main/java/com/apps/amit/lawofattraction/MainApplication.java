package com.apps.amit.lawofattraction;

import android.app.Application;
import android.content.Context;

import com.apps.amit.lawofattraction.helper.LocaleHelper;

public class MainApplication extends Application {
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
	}
}
