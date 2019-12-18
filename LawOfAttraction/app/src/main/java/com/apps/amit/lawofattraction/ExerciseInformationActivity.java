package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

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

            Glide.with(this).load(R.drawable.starshd).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mainlayout.setBackground(drawable);
                    }
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

            adView = new com.facebook.ads.AdView(this, getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);
            adView1 = new com.facebook.ads.AdView(this, getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
            LinearLayout adContainer1 = (LinearLayout) findViewById(R.id.adView);

            // Add the ad view to your activity layout
            adContainer.addView(adView);
            adContainer1.addView(adView1);

            // Request an ad
            adView.loadAd();
            adView1.loadAd();

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
