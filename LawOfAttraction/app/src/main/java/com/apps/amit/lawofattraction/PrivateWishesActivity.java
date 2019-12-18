package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.amit.lawofattraction.adapters.PrivateWishRecyclerAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PrivateWishesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<PrivateWishesUtils> lsItem;
    Button button;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    EditText edt;
    EditText edt1;
    TextView txt;
    Resources resources;
    TextView nameText;
    TextView wishText;
    RecyclerView.Adapter adapter;
    LinearLayout mainlayout;
    private InterstitialAd interstitialAd;
    public static final String TAG = PrivateWishesActivity.class.getSimpleName();
    Boolean isAdLoaded = false;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_wishes);

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        if(netInfo!=null && netInfo.isConnected()) {
            interstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), getString(R.string.facebook_interstitial_id));
            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener((new PrivateWishesActivity.InterstitialListener())).build());
        }
        //Store selected language in a Variable called value
        Context context = LocaleHelper.setLocale(getApplicationContext(), "en");
        resources = context.getResources();

        recyclerView =  findViewById(R.id.recyclerView);
        button =  findViewById(R.id.sendPrivateWish);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainlayout = findViewById(R.id.mainlayout);


        Glide.with(this).load(R.drawable.starshd).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainlayout.setBackground(drawable);
                }
            }
        });

        loadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PrivateWishesActivity.this);
                final View dialogView = View.inflate(getApplicationContext(),R.layout.wish_options, null);
                builder.setCancelable(false);
                builder.setMessage(resources.getString(R.string.subUniverse_dialogTitle));

                builder.setView(dialogView);
                final AlertDialog alert = builder.create();
                alert.show();

                nameText =  dialogView.findViewById(R.id.name);
                wishText =  dialogView.findViewById(R.id.wish);
                edt =  dialogView.findViewById(R.id.username);
                edt1 =  dialogView.findViewById(R.id.comments);
                Button submit =  dialogView.findViewById(R.id.btnsubmit);
                Button cancel =  dialogView.findViewById(R.id.btncancel);

                nameText.setText(resources.getString(R.string.subUniverse_dialogName));
                wishText.setText(resources.getString(R.string.subUniverse_dialogWish));
                submit.setText(resources.getString(R.string.subUniverse_dialogSubmit));
                cancel.setText(resources.getString(R.string.subUniverse_dialogCancel));

                SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
                String naam = sp1.getString("userName","");

                if((naam!=null && naam.equalsIgnoreCase("")))
                {
                    edt.setText("");

                } else{

                    edt.setText(naam);
                }

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(buttonClick);

                            personalWish(alert);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);
                        alert.dismiss();
                    }
                });
            }
        });

    }

    private class InterstitialListener implements InterstitialAdExtendedListener {

        @Override
        public void onInterstitialActivityDestroyed() {

            Log.d(TAG, "Interstitial ad onInterstitialActivityDestroyed!");
        }

        @Override
        public void onInterstitialDisplayed(Ad ad) {

            Log.d(TAG, "Interstitial ad InterstitialDisplayed!");
        }

        @Override
        public void onInterstitialDismissed(Ad ad) {

            Log.d(TAG, "Interstitial ad InterstitialDismissed!");
        }

        @Override
        public void onError(Ad ad, AdError adError) {

            Log.d(TAG, "Interstitial ad onError!");
        }

        @Override
        public void onAdLoaded(Ad ad) {

            Log.d(TAG, "Interstitial ad Loaded!");
            isAdLoaded = Boolean.TRUE;
        }

        @Override
        public void onAdClicked(Ad ad) {

            Log.d(TAG, "Interstitial ad AdClicked!");
        }

        @Override
        public void onLoggingImpression(Ad ad) {

            Log.d(TAG, "Interstitial ad LoggingImpression!");
        }

        @Override
        public void onRewardedAdCompleted() {
            Log.d(TAG, "Interstitial ad RewardedAdCompleted!");
        }

        @Override
        public void onRewardedAdServerSucceeded() {

            Log.d(TAG, "Interstitial ad RewardedAdServerSucceeded!");
        }

        @Override
        public void onRewardedAdServerFailed() {

            Log.d(TAG, "Interstitial ad RewardedAdServerFailed!");
        }
    }

    public void displayInterstitial() {

        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (interstitialAd.isAdInvalidated()) {
            return;
        }
        // Show the ad
        if(isAdLoaded.equals(Boolean.TRUE)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        interstitialAd.show();
                    } catch (Exception e) {
                        Log.e("MyError",e.getMessage());
                    }
                }
            }, 1000);
        }


    }
    private void loadData() {

        lsItem = new ArrayList<>();

        WishDataBaseHandler db = new WishDataBaseHandler(this);

        List<PrivateWishesUtils> wishes = db.getAllWishes();

        for (PrivateWishesUtils ws : wishes) {

            PrivateWishesUtils wishDB = new PrivateWishesUtils(ws.getUserName(),ws.getUserWish(),ws.getUserDate());
            lsItem.add(wishDB);
            Collections.reverse(lsItem);
        }

        adapter = new PrivateWishRecyclerAdapter(lsItem,getApplicationContext());
        recyclerView.setAdapter(adapter);

        //loa Ad
        displayInterstitial();
    }

    public void personalWish( AlertDialog alert){

        String userName = edt.getText().toString();
        String userComment = edt1.getText().toString();
        String time =  DateFormat.getDateTimeInstance().format(new Date());

        if (TextUtils.isEmpty(userName)  )
        {

            edt.setError(resources.getString(R.string.enterName));

        }
        else if ( TextUtils.isEmpty(userComment) )
        {

            edt1.setError(resources.getString(R.string.enterWish));

        } else {

            SharedPreferences timerEnable = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = timerEnable.edit();
            editor.putString("userName", userName);
            editor.apply();

            WishDataBaseHandler db = new WishDataBaseHandler(this);

            db.addWish(new PrivateWishesUtils(userName, userComment, time));
            alert.dismiss();

            loadData();
        }
    }
}
