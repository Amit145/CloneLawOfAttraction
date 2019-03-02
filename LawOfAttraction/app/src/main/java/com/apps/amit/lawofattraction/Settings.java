package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Settings extends AppCompatActivity {

    //Variable Declaration

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Intent art1;
    private AdView mAdView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        img1=null;
        img2=null;
        img3=null;
        mAdView=null;
        this.finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAdView=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        Resources resources = context.getResources();

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            mAdView = findViewById(R.id.adView10);
            AdRequest adRequest1 = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest1);

            img1 = findViewById(R.id.img1);
            img2 = findViewById(R.id.img2);
            img3 = findViewById(R.id.img3);

            if(value.equalsIgnoreCase("hi")) {

                Glide.with(getApplicationContext()).load(R.drawable.hi_sett1).thumbnail(0.1f).into(img1);
                Glide.with(getApplicationContext()).load(R.drawable.hi_sett2).thumbnail(0.1f).into(img2);
                Glide.with(getApplicationContext()).load(R.drawable.hi_set33).thumbnail(0.1f).into(img3);

            } else {

                Glide.with(getApplicationContext()).load(R.drawable.sett1).thumbnail(0.1f).into(img1);
                Glide.with(getApplicationContext()).load(R.drawable.sett2).thumbnail(0.1f).into(img2);
                Glide.with(getApplicationContext()).load(R.drawable.set33).thumbnail(0.1f).into(img3);
            }

        }  catch (OutOfMemoryError e) {

            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();

        }
    }

    public void timePicker(View view) {

        view.startAnimation(buttonClick);
        art1 = new Intent(getApplicationContext(),timepicker.class);
        startActivity(art1);
    }

    public void setTime(View view) {

        view.startAnimation(buttonClick);
        art1 = new Intent(getApplicationContext(), SetTime.class);
        startActivity(art1);
    }

    public void shareApp(View view) {

        view.startAnimation(buttonClick);
        art1 = new Intent(getApplicationContext(),info.class);
        startActivity(art1);
    }
}
