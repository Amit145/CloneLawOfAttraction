package com.apps.amit.lawofattraction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.apps.amit.lawofattraction.sqlitedatabase.ActivityTrackerDatabaseHandler;
import com.apps.amit.lawofattraction.utils.ManifestationTrackerUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExerciseFinishActivity extends AppCompatActivity {

    ImageView track;
    ImageView home;
    ImageView share;
    ImageView img;
    int dummy = 99;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    TextView txt;
    TextView actText1;
    TextView actText2;
    TextView actText3;
    TextView actText4;
    TextView actText5;
    TextView actText6;
    long endTime;
    double elapsedSeconds;
    long elapsedMilliSeconds;
    ActivityTrackerDatabaseHandler db;
    List < String > quoteList = new ArrayList < > ();

    public static final String TAG = ExerciseFinishActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;
    private AdView adView;


    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        super.onDestroy();
        db.close();
        home = null;
        track = null;
        share = null;
        img = null;
        txt = null;
    }

    @Override
    public void onBackPressed() {

        db.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.Exit_Text));

        builder.setPositiveButton(getString(R.string.Yes_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                ActivityCompat.finishAffinity(ExerciseFinishActivity.this);
            }
        });

        builder.setNegativeButton(getString(R.string.No_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        img = null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            SharedPreferences sharedPreferencesManifestationType = getApplicationContext().getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);
            String manifestationTypeValue = sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_finish);

            SecureRandom rand = new SecureRandom();

            quoteList.add("\"You're one step closer to your dreams..\"");
            quoteList.add("\"Trust the process ! You will manifest all your dreams..\"");
            quoteList.add("\"Great thing's take time ! Stay Focused..\"");
            quoteList.add("\"It is not over, unless you mean it..\"");
            quoteList.add("\"Trust the Universe ! It is always there for you ..\"");


            adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            int answer = rand.nextInt(quoteList.size());

            db = new ActivityTrackerDatabaseHandler(getApplicationContext());

            home = findViewById(R.id.home1);
            track = findViewById(R.id.track);
            share = findViewById(R.id.share);
            img = findViewById(R.id.imageView2);

            actText1 = findViewById(R.id.textView1);
            actText2 = findViewById(R.id.textView2);
            actText3 = findViewById(R.id.textView3);
            actText4 = findViewById(R.id.textView4);
            actText5 = findViewById(R.id.textView5);
            actText6 = findViewById(R.id.textView6);
            endTime = SystemClock.elapsedRealtime();

            actText1.setText(getString(R.string.activity6_text1));
            actText2.setText(getString(R.string.activity6_text2));
            actText3.setText(quoteList.get(answer));
            actText4.setText(getString(R.string.activity6_text4));
            actText5.setText(getString(R.string.activity6_text5));
            actText6.setText(getString(R.string.activity6_text6));

            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",
                    Locale.getDefault());
            String date = df.format(c);

            db.addContact(new ManifestationTrackerUtils(date, manifestationTypeValue));

            Toast.makeText(getApplicationContext(), getString(R.string.event_add), Toast.LENGTH_LONG).show();

            Glide.with(getApplicationContext()).load(R.drawable.tick).thumbnail(0.1f).fitCenter().into(img);

            Glide.with(getApplicationContext()).load(R.drawable.home).thumbnail(0.1f).fitCenter().into(home);

            Glide.with(getApplicationContext()).load(R.drawable.trackprogress).thumbnail(0.1f).fitCenter().into(track);

            Glide.with(getApplicationContext()).load(R.drawable.shareee).thumbnail(0.1f).fitCenter().into(share);




            track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);


                    Intent art1 = new Intent(getApplicationContext(), ManifestationTrackerActivity.class);
                    startActivity(art1);
                    img = null;
                    finish();

                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);

                    Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(art1);
                    img = null;
                    finish();


                }
            });


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                    try {
                        startActivity(Intent.createChooser(shareIntent, getString(R.string.chooseToShare)));

                    } catch (android.content.ActivityNotFoundException ex) {

                        Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                    }

                }
            });

            // Create the InterstitialAd and set the adUnitId.
            interstitialAd = new InterstitialAd(this);
            // Defined in res/values/strings.xml
            interstitialAd.setAdUnitId(getString(R.string.TestInterstitialAdsBannerGoogle));
            interstitialAd.loadAd(new AdRequest.Builder().build());
            interstitialAd.setAdListener(new AdListener()
            {
                @Override
                public void onAdLoaded() {
                    Log.d(TAG, "Interstitial ad Loaded!");
                    displayInterstitial();
                }
            });



        } catch (OutOfMemoryError e) {
            Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

        }
    }


    public void displayInterstitial() {

        if (interstitialAd == null || !interstitialAd.isLoaded()) {
            return;
        }

        // Show the ad
        interstitialAd.show();
    }

}