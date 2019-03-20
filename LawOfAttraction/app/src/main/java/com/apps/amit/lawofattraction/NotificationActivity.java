package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity {

    private Button btn;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView notificationTitle;
    Context context;
    Resources resources;
    private TextView notificationSubTitle;
    private TextView notificationBody;
    private TextView notificationSubBody;
    String valueLanguage = "en";
    private TextView notificationSubBody1;
    private TextView notificationSubBody2;
    private RewardedVideoAd rewardedVideoAd;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String APP_ID = "ca-app-pub-3940256099942544~3347511713";
    private static final String IS_ADS_ENABLED_STATUS = "AdvsPref";
    private static final String CURRENT_ADS_STATUS = "adsDate";

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent art1 = new Intent(getApplicationContext(), Home.class);
        // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(art1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        context = LocaleHelper.setLocale(getApplicationContext(), valueLanguage);
        resources = context.getResources();

        Calendar c1 = Calendar.getInstance();

        // Create the "show" button, which shows a rewarded video if one is loaded.
        btn = findViewById(R.id.button3);

        linearLayout = findViewById(R.id.activityHead);
        imageView = findViewById(R.id.image);
        notificationTitle = findViewById(R.id.notificationTitle);
        notificationSubTitle = findViewById(R.id.notificationSubTitle);
        notificationBody = findViewById(R.id.notificationBody);
        notificationSubBody = findViewById(R.id.notificationSubBody);
        notificationSubBody1 = findViewById(R.id.notificationSubBody1);
        notificationSubBody2 = findViewById(R.id.notificationSubBody2);

        imageView.setBackgroundResource(R.drawable.reward);


        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn.setText(resources.getString(R.string.notiLoad));
                btn.setEnabled(false);
                showRewardedVideo();
                loadRewardedVideoAd();
            }
        });


        SharedPreferences pref = getSharedPreferences(IS_ADS_ENABLED_STATUS,MODE_PRIVATE);

        //Store selected language in a Variable called value
        String value = pref.getString(CURRENT_ADS_STATUS,c1.getTime().toString());

        if(!value.isEmpty())
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(value));// all done
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
            }

            if(cal.getTime().after(c1.getTime()))
            {

                notificationTitle.setText(resources.getString(R.string.notiSuccessTitle));
                notificationSubTitle.setText(resources.getString(R.string.notiSuccessSubTitle));
                notificationBody.setText(resources.getString(R.string.notiSuccessHead));
                notificationSubBody.setText(cal.getTime().toString());
                btn.setVisibility(View.VISIBLE);
                notificationSubBody.setTextSize(17.0f);
                notificationSubBody2.setText(resources.getString(R.string.notiSuccessBody));
                notificationSubBody2.setTextSize(25.0f);
                btn.setEnabled(true);
                btn.setText(resources.getString(R.string.start_text));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openIntent = new Intent (getApplicationContext(),Home.class);
                        startActivity(openIntent);
                    }
                });
                notificationSubBody1.setVisibility(View.INVISIBLE);
                notificationSubBody2.setVisibility(View.VISIBLE);
                linearLayout.setBackgroundColor(Color.parseColor("#44bb05"));
                imageView.setBackgroundResource(R.drawable.tick);
            }

        }
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, APP_ID);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                Toast.makeText(getApplicationContext(), "SUCCESS ! " , Toast.LENGTH_SHORT).show();
                // Reward the user.

                Calendar c1 = Calendar.getInstance();

                Date dt = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                c.add(Calendar.DATE, 2);

                SharedPreferences adsPref = getSharedPreferences(IS_ADS_ENABLED_STATUS,MODE_PRIVATE);
                SharedPreferences.Editor editor = adsPref.edit();
                editor.putString(CURRENT_ADS_STATUS, c.getTime().toString());
                editor.apply();

                SharedPreferences pref = getSharedPreferences(IS_ADS_ENABLED_STATUS,MODE_PRIVATE);

                //Store selected language in a Variable called value
                String value = pref.getString(CURRENT_ADS_STATUS,c1.getTime().toString());

                if(!value.isEmpty())
                {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    try {
                        cal.setTime(sdf.parse(value));// all done
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();

                    }

                    if(cal.getTime().after(c1.getTime()))
                    {

                        notificationTitle.setText(resources.getString(R.string.notiSuccessTitle));
                        notificationSubTitle.setText(resources.getString(R.string.notiSuccessSubTitle));
                        notificationBody.setText(resources.getString(R.string.notiSuccessHead));
                        notificationSubBody.setText(cal.getTime().toString());
                        btn.setVisibility(View.VISIBLE);
                        notificationSubBody.setTextSize(17.0f);
                        notificationSubBody2.setText(resources.getString(R.string.notiSuccessBody));
                        notificationSubBody2.setTextSize(25.0f);
                        btn.setEnabled(true);
                        btn.setText(resources.getString(R.string.start_text));
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent openIntent = new Intent (getApplicationContext(),Home.class);
                                startActivity(openIntent);
                            }
                        });
                        notificationSubBody1.setVisibility(View.INVISIBLE);
                        notificationSubBody2.setVisibility(View.VISIBLE);
                        linearLayout.setBackgroundColor(Color.parseColor("#44bb05"));
                        imageView.setBackgroundResource(R.drawable.tick);

                    }
                }
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

                //Not in USE
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

                //Not in USE
            }

            @Override
            public void onRewardedVideoCompleted() {

                //Not in USE
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                btn.setText(resources.getString(R.string.start_text));
                btn.setEnabled(true);

            }

            @Override
            public void onRewardedVideoAdOpened() {

                //Not in USE

            }

            @Override
            public void onRewardedVideoStarted() {

                //Not in USE
            }

            @Override
            public void onRewardedVideoAdClosed() {

                //Not in USE

                loadRewardedVideoAd();
            }
        });

        loadRewardedVideoAd();

    }

    private void loadRewardedVideoAd() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() {
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }
}
