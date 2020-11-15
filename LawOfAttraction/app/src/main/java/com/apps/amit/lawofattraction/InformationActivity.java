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

public class InformationActivity extends AppCompatActivity {

    Button infoButon1;
    TextView txt1;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ImageView img1;
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
          setContentView(R.layout.activity_info);

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

                    /*
                    Not required
                     */
              }
          });

          infoButon1 =  findViewById(R.id.infoButtonn1);
          txt1 =  findViewById(R.id.ihome);
          img1 =  findViewById(R.id.igg1);
          textView1 =  findViewById(R.id.textView1);
          textView2 =  findViewById(R.id.textView2);
          textView3 =  findViewById(R.id.textView3);

          ButterKnife.bind(this);

          //get user selected language from shared preferences
          //Store selected language in a Variable called value
          SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);
          final String value1 = pref.getString("language","en");
          updateViews(value1);

          adView = findViewById(R.id.adView);
          AdRequest adRequest = new AdRequest.Builder().build();
          adView.loadAd(adRequest);

          adView1 = findViewById(R.id.adView1);
          AdRequest adRequest1 = new AdRequest.Builder().build();
          adView1.loadAd(adRequest1);

          img1.setImageBitmap(
                  decodeSampledBitmapFromResource(getResources(), R.drawable.lae, 200, 200));

          txt1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  v.startAnimation(buttonClick);
                  Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
                  startActivity(art1);
              }
          });

          infoButon1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  v.startAnimation(buttonClick);
                  Intent art1 = new Intent(InformationActivity.this, ExerciseInformationActivity.class);
                  startActivity(art1);
              }
          });
      } catch (OutOfMemoryError e) {
          Log.e("OutOfMemory",e.getLocalizedMessage());
      }
    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        infoButon1.setText(resources.getString(R.string.activity_info3));
        txt1.setText(resources.getString(R.string.activity_info4));
        textView1.setText(resources.getString(R.string.activity_info));
        textView2.setText(resources.getString(R.string.activity_info1));
        textView3.setText(resources.getString(R.string.activity_info2));
    }
}
