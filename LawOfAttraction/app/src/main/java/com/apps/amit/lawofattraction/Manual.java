package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.ButterKnife;

import static com.apps.amit.lawofattraction.ScaleImage.decodeSampledBitmapFromResource;

public class Manual extends AppCompatActivity {

    Button infoButon1;
    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ImageView img1;

    @Override
    public void onBackPressed() {

        Intent art1 = new Intent(getApplicationContext(), exer.class);
        // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(art1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_manual);

            infoButon1 =  findViewById(R.id.infoButtonn);
            txt1 =  findViewById(R.id.ihome);

            txt2 =  findViewById(R.id.textView1);
            txt3 =  findViewById(R.id.textView2);
            txt4 =  findViewById(R.id.textView3);
            txt5 =  findViewById(R.id.textView4);
            txt6 =  findViewById(R.id.textView5);
            txt7 =  findViewById(R.id.textView6);
            txt8 =  findViewById(R.id.textView7);
            txt9 =  findViewById(R.id.textView8);
            txt10 =  findViewById(R.id.textView9);
            txt11 =  findViewById(R.id.textView10);

            img1 =  findViewById(R.id.igg);

            ButterKnife.bind(this);

            //get user selected language from shared preferences

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            updateViews(value1);

            AdView mAdView29 =  findViewById(R.id.adViewi22);
            AdRequest adRequest29 = new AdRequest.Builder().build();
            mAdView29.loadAd(adRequest29);

            AdView mAdView19 =  findViewById(R.id.adViewi11);
            AdRequest adRequest19 = new AdRequest.Builder().build();
            mAdView19.loadAd(adRequest19);

            img1.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.anc, 200, 200));

            txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), Home.class);
                    // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(art1);


                }
            });

            infoButon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(Manual.this, Exercise1.class);
                    //art1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(art1);


                }
            });

        }catch (OutOfMemoryError e)
        {
            System.gc();
        }

    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();


        //txt1= (TextView) findViewById(R.id.textView3);

        //Story

        infoButon1.setText(resources.getString(R.string.activity_manual10));
        txt1.setText(resources.getString(R.string.activity_manual11));


        txt2.setText(resources.getString(R.string.activity_manual));
        txt3.setText(resources.getString(R.string.activity_manual1));
        txt4.setText(resources.getString(R.string.activity_manual2));
        txt5.setText(resources.getString(R.string.activity_manual3));
        txt6.setText(resources.getString(R.string.activity_manual4));
        txt7.setText(resources.getString(R.string.activity_manual5));
        txt8.setText(resources.getString(R.string.activity_manual6));
        txt9.setText(resources.getString(R.string.activity_manual7));
        txt10.setText(resources.getString(R.string.activity_manual8));
        txt11.setText(resources.getString(R.string.activity_manual9));






    }
}
