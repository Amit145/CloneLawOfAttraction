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

public class info extends AppCompatActivity {

    Button infoButon1;
    TextView txt1,TextView1,TextView2,TextView3;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ImageView img1;

    @Override
    public void onBackPressed() {

        Intent art1 = new Intent(getApplicationContext(), Home.class);
        // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(art1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      try {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_info);

          infoButon1 =  findViewById(R.id.infoButtonn1);
          txt1 =  findViewById(R.id.ihome);
          img1 =  findViewById(R.id.igg1);
          TextView1 =  findViewById(R.id.textView1);
          TextView2 =  findViewById(R.id.textView2);
          TextView3 =  findViewById(R.id.textView3);

          ButterKnife.bind(this);

          //get user selected language from shared preferences

          SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

          //Store selected language in a Variable called value
          final String value1 = pref.getString("language","en");

          updateViews(value1);

          AdView mAdView299 =  findViewById(R.id.adViewi221);
          AdRequest adRequest299 = new AdRequest.Builder().build();
          mAdView299.loadAd(adRequest299);

          AdView mAdView199 =  findViewById(R.id.adViewi111);
          AdRequest adRequest199 = new AdRequest.Builder().build();
          mAdView199.loadAd(adRequest199);

          img1.setImageBitmap(
                  decodeSampledBitmapFromResource(getResources(), R.drawable.lae, 200, 200));

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
                  Intent art1 = new Intent(info.this, exer.class);
                  // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(art1);


              }
          });
      }
      catch (OutOfMemoryError e)
      {
          System.gc();
      }
    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();


        //txt1= (TextView) findViewById(R.id.textView3);

        //Story

        infoButon1.setText(resources.getString(R.string.activity_info3));
        txt1.setText(resources.getString(R.string.activity_info4));
        TextView1.setText(resources.getString(R.string.activity_info));
        TextView2.setText(resources.getString(R.string.activity_info1));
        TextView3.setText(resources.getString(R.string.activity_info2));




    }
}
