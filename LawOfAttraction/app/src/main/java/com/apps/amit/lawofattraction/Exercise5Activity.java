package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import butterknife.ButterKnife;

public class Exercise5Activity extends AppCompatActivity {

    Button buttonStart;
    Context context;
    Resources resources;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    TextView txt;
    TextView actText1;
    TextView actText2;
    TextView actText3;
    TextView actText4;
    TextView actText5;
    TextView actText6;
    ImageView img;
    int value;
    private AdView adView;


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        //Store selected language in a Variable called value
        final String value1 = "en";

        context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(resources.getString(R.string.stopManifestation_text));
        builder.setPositiveButton(resources.getString(R.string.Yes_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(art1);
            }
        });
        builder.setNegativeButton(resources.getString(R.string.No_text),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exercise5);


            adView = new AdView(this, getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

            // Add the ad view to your activity layout
            adContainer.addView(adView);

            // Request an ad
            adView.loadAd();

            buttonStart =  findViewById(R.id.startButton);
            txt =  findViewById(R.id.skip);
            actText1=  findViewById(R.id.textView2);
            actText2=  findViewById(R.id.textView3);
            actText3=  findViewById(R.id.textView4);
            actText4=  findViewById(R.id.textView5);
            actText5=  findViewById(R.id.textView6);
            actText6=  findViewById(R.id.textView7);
            img =  findViewById(R.id.imageView2);

            txt.setVisibility(View.INVISIBLE);


            SharedPreferences sp = getSharedPreferences("your_prefs", Exercise5Activity.MODE_PRIVATE);
            value = sp.getInt("your_int_key", 60);


            ButterKnife.bind(this);

            //get user selected language from shared preferences


            //Store selected language in a Variable called value
            final String value1 = "en";

             context = LocaleHelper.setLocale(getApplicationContext(), value1);
             resources = context.getResources();

            updateViews(value1);


            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(art1);


                }
            });



        }catch(OutOfMemoryError e)
        {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();

        }
    }

    private void updateViews(String languageCode) {

         context = LocaleHelper.setLocale(this, languageCode);
         resources = context.getResources();



        //StoryActivity

        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);
        String manifestationTypeValue = sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "");

        //StoryActivity
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Money));
            actText2.setText(resources.getString(R.string.activity5_text2_Money));
            actText3.setText(resources.getString(R.string.activity5_text3_Money));
            actText4.setText(resources.getString(R.string.activity5_text4_Money));
            actText5.setText(resources.getString(R.string.activity5_text5_Money));
            actText6.setText(resources.getString(R.string.activity5_text6_Money));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value1))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Home));
            actText2.setText(resources.getString(R.string.activity5_text2_Home));
            actText3.setText(resources.getString(R.string.activity5_text3_Home));
            actText4.setText(resources.getString(R.string.activity5_text4_Home));
            actText5.setText(resources.getString(R.string.activity5_text5_Home));
            actText6.setText(resources.getString(R.string.activity5_text6_Home));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value2))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Love));
            actText2.setText(resources.getString(R.string.activity5_text2_Love));
            actText3.setText(resources.getString(R.string.activity5_text3_Love));
            actText4.setText(resources.getString(R.string.activity5_text4_Love));
            actText5.setText(resources.getString(R.string.activity5_text5_Love));
            actText6.setText(resources.getString(R.string.activity5_text6_Love));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value3))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Love));
            actText2.setText(resources.getString(R.string.activity5_text2_Love));
            actText3.setText(resources.getString(R.string.activity5_text3_Love));
            actText4.setText(resources.getString(R.string.activity5_text4_Love));
            actText5.setText(resources.getString(R.string.activity5_text5_Love));
            actText6.setText(resources.getString(R.string.activity5_text6_Love));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value4))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Happy));
            actText2.setText(resources.getString(R.string.activity5_text2_Happy));
            actText3.setText(resources.getString(R.string.activity5_text3_Happy));
            actText4.setText(resources.getString(R.string.activity5_text4_Happy));
            actText5.setText(resources.getString(R.string.activity5_text5_Happy));
            actText6.setText(resources.getString(R.string.activity5_text6_Happy));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value5))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Health));
            actText2.setText(resources.getString(R.string.activity5_text2_Health));
            actText3.setText(resources.getString(R.string.activity5_text3_Health));
            actText4.setText(resources.getString(R.string.activity5_text4_Health));
            actText5.setText(resources.getString(R.string.activity5_text5_Health));
            actText6.setText(resources.getString(R.string.activity5_text6_Health));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value6))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1_Job));
            actText2.setText(resources.getString(R.string.activity5_text2_Job));
            actText3.setText(resources.getString(R.string.activity5_text3_Job));
            actText4.setText(resources.getString(R.string.activity5_text4_Job));
            actText5.setText(resources.getString(R.string.activity5_text5_Job));
            actText6.setText(resources.getString(R.string.activity5_text6_Job));
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value7)) || manifestationTypeValue.equalsIgnoreCase("")) {
            txt.setText(resources.getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity5_text1));
            actText2.setText(resources.getString(R.string.activity5_text2));
            actText3.setText(resources.getString(R.string.activity5_text3));
            actText4.setText(resources.getString(R.string.activity5_text4));
            actText5.setText(resources.getString(R.string.activity5_text5));
            actText6.setText(resources.getString(R.string.activity5_text6));
            buttonStart.setText(resources.getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex5).thumbnail(0.1f).into(img);

        }

        final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        SharedPreferences sp = getSharedPreferences("your_prefs", Exercise5Activity.MODE_PRIVATE);
        value = sp.getInt("your_int_key", 60);


        SharedPreferences sp1 = getSharedPreferences("timerEnable", Exercise5Activity.MODE_PRIVATE);
        String timervalue = sp1.getString("timerEnable","ON");


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(art1);


            }
        });

        if(timervalue.contains("ON")) {
            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    txt.setVisibility(View.VISIBLE);
                    new CountDownTimer(+((value + 1) * 1000), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            buttonStart.setEnabled(false);
                            String textTime = "" + millisUntilFinished / 1000;
                            buttonStart.setText(textTime);
                            txt.setText(getString(R.string.skip_text));

                            txt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    v.startAnimation(buttonClick);
                                    Intent art1 = new Intent(getApplicationContext(), ExerciseFinishActivity.class);
                                    startActivity(art1);
                                    img=null;
                                    cancel();

                                }
                            });
                        }

                        @Override
                        public void onFinish() {


                            if(vib != null) {
                                vib.vibrate(500);
                            }


                            buttonStart.setText(getString(R.string.done_text));
                            buttonStart.setEnabled(true);
                            txt.setText("");

                            buttonStart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    v.startAnimation(buttonClick);
                                    Intent art1 = new Intent(getApplicationContext(), ExerciseFinishActivity.class);
                                    startActivity(art1);
                                    img=null;


                                }
                            });
                        }

                    }.start();
                }
            });

        } else {

            buttonStart.setText(getString(R.string.next_text));

            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(Exercise5Activity.this, ExerciseFinishActivity.class);
                    startActivity(art1);
                    img=null;


                }
            });
        }

    }
}
