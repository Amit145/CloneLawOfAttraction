package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import butterknife.ButterKnife;

import static com.apps.amit.lawofattraction.helper.ScaleImage.decodeSampledBitmapFromResource;

public class ExerciseInformationActivity extends AppCompatActivity {

    Button infoButon;
    TextView txt,txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ImageView img;
    LinearLayout mainlayout;
    private AdView adView;
    private AdView adView1;

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        if (adView1 != null) {
            adView1.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        if (adView1 != null) {
            adView1.pause();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        Intent art1 = new Intent(getApplicationContext(), InformationActivity.class);
        startActivity(art1);

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (adView1 != null) {
            adView1.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exer);

            mainlayout = findViewById(R.id.mainlayout);

            Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mainlayout.setBackground(resource);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

            infoButon =  findViewById(R.id.infoButton);
            txt =  findViewById(R.id.ihome);

            txt1 =  findViewById(R.id.textView1);
            txt2 =  findViewById(R.id.textView2);
            txt3 =  findViewById(R.id.textView3);
            txt4 =  findViewById(R.id.textView4);
            txt5 =  findViewById(R.id.textView5);
            txt6 =  findViewById(R.id.textView6);
            txt7 =  findViewById(R.id.textView7);
            txt8 =  findViewById(R.id.textView8);
            txt9 =  findViewById(R.id.textView9);
            txt10 =  findViewById(R.id.textView10);
            txt11 =  findViewById(R.id.textView11);
            txt12 =  findViewById(R.id.textView12);

            img =  findViewById(R.id.ig);

            img.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.brainexer, 200, 200));


            ButterKnife.bind(this);

            //get user selected language from shared preferences

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            updateViews(value1);

            adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView1 = findViewById(R.id.adView1);
            AdRequest adRequest1 = new AdRequest.Builder().build();
            adView1.loadAd(adRequest1);

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(art1);


                }
            });

            infoButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), UserManualActivity.class);
                    startActivity(art1);


                }
            });

        } catch (OutOfMemoryError e)
        {
            Log.e(e.getMessage(),e.getMessage());
        }
    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        infoButon.setText(resources.getString(R.string.activity_exer12));
        txt.setText(resources.getString(R.string.activity_exer13));


        txt1.setText(resources.getString(R.string.activity_exer));
        txt2.setText(resources.getString(R.string.activity_exer1));
        txt3.setText(resources.getString(R.string.activity_exer2));
        txt4.setText(resources.getString(R.string.activity_exer3));
        txt5.setText(resources.getString(R.string.activity_exer4));
        txt6.setText(resources.getString(R.string.activity_exer5));
        txt7.setText(resources.getString(R.string.activity_exer6));
        txt8.setText(resources.getString(R.string.activity_exer7));
        txt9.setText(resources.getString(R.string.activity_exer8));
        txt10.setText(resources.getString(R.string.activity_exer9));
        txt11.setText(resources.getString(R.string.activity_exer10));
        txt12.setText(resources.getString(R.string.activity_exer11));





    }
}
