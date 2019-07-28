package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import butterknife.ButterKnife;

public class Exercise2 extends AppCompatActivity {

    Button buttonStart;
    Resources resources;
    Context context;
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


    @Override
    public void onBackPressed() {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value1 = pref.getString("language","en");
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
                Intent art1 = new Intent(getApplicationContext(), Home.class);
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
            setContentView(R.layout.activity_exercise2);

            AdView mAdView =  findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

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
            final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            SharedPreferences sp = getSharedPreferences("your_prefs", Exercise2.MODE_PRIVATE);
            value = sp.getInt("your_int_key", 60);


            SharedPreferences sp1 = getSharedPreferences("timerEnable", Exercise2.MODE_PRIVATE);
            String timervalue = sp1.getString("timerEnable", "ON");


            ButterKnife.bind(this);

            //get user selected language from shared preferences

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            //final String value1 = pref.getString("language","en");
            final String value1 = "en";

            context = LocaleHelper.setLocale(getApplicationContext(), value1);
            resources = context.getResources();

            updateViews(value1);

            Glide.with(getApplicationContext()).load(R.drawable.ex2).thumbnail(0.1f).into(img);


            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), Home.class);
                    startActivity(art1);
                    img = null;


                }
            });

            if (timervalue.contains("ON")) {
                buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);

                        txt.setVisibility(View.VISIBLE);

                        new CountDownTimer((value + 1) * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {


                                context = LocaleHelper.setLocale(getApplicationContext(), value1);
                                resources = context.getResources();
                                buttonStart.setEnabled(false);
                                String textTime = "" + millisUntilFinished / 1000;
                                buttonStart.setText(textTime);
                                txt.setText(resources.getString(R.string.skip_text));

                                txt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        v.startAnimation(buttonClick);
                                        Intent art1 = new Intent(getApplicationContext(), Exercise3.class);
                                        startActivity(art1);
                                        cancel();
                                        img = null;


                                    }
                                });

                            }

                            @Override
                            public void onFinish() {


                                if(vib != null) {
                                    vib.vibrate(500);
                                }

                                context = LocaleHelper.setLocale(getApplicationContext(), value1);
                                resources = context.getResources();
                                buttonStart.setText(resources.getString(R.string.done_text));
                                buttonStart.setEnabled(true);
                                txt.setText("");

                                buttonStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        v.startAnimation(buttonClick);
                                        Intent art1 = new Intent(getApplicationContext(), Exercise3.class);
                                        startActivity(art1);
                                        img = null;
                                        cancel();


                                    }
                                });


                            }
                        }.start();

                    }
                });

            } else {

                context = LocaleHelper.setLocale(getApplicationContext(), value1);
                resources = context.getResources();
                buttonStart.setText(resources.getString(R.string.next_text));

                buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);
                        Intent art1 = new Intent(Exercise2.this, Exercise3.class);
                        startActivity(art1);
                        img = null;
                        finish();

                    }
                });
            }

        } catch(OutOfMemoryError e) {

            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
        }
    }

    private void updateViews(String languageCode) {

         context = LocaleHelper.setLocale(this, languageCode);
         resources = context.getResources();

        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1.MODE_PRIVATE);
        String manifestationTypeValue = sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "");

        //Story
        if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("MONEY");
            actText2.setText("MONEY");
            actText3.setText("MONEY");
            actText4.setText("MONEY");
            actText5.setText("MONEY");
            actText6.setText("MONEY");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value1))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("Home");
            actText2.setText("Home");
            actText3.setText("Home");
            actText4.setText("Home");
            actText5.setText("Home");
            actText6.setText("Home");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value2))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("Love");
            actText2.setText("Love");
            actText3.setText("Love");
            actText4.setText("Love");
            actText5.setText("Love");
            actText6.setText("Love");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value3))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("Car");
            actText2.setText("Car");
            actText3.setText("Car");
            actText4.setText("Car");
            actText5.setText("Car");
            actText6.setText("Car");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value4))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("Happyness");
            actText2.setText("Happyness");
            actText3.setText("Happyness");
            actText4.setText("Happyness");
            actText5.setText("Happyness");
            actText6.setText("Happyness");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value5))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("Health");
            actText2.setText("Health");
            actText3.setText("Health");
            actText4.setText("Health");
            actText5.setText("Health");
            actText6.setText("Health");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value6))) {

            txt.setVisibility(View.INVISIBLE);
            txt.setText(getString(R.string.skip_text));
            actText1.setText("Job");
            actText2.setText("Job");
            actText3.setText("Job");
            actText4.setText("Job");
            actText5.setText("Job");
            actText6.setText("Job");
            buttonStart.setText(getString(R.string.start_text));
            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

        }  if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value7)) || manifestationTypeValue.equalsIgnoreCase("")) {
            txt.setText(resources.getString(R.string.skip_text));
            actText1.setText(resources.getString(R.string.activity2_text1));
            actText2.setText(resources.getString(R.string.activity2_text2));
            actText3.setText(resources.getString(R.string.activity2_text3));
            actText4.setText(resources.getString(R.string.activity2_text4));
            actText5.setText(resources.getString(R.string.activity2_text5));
            actText6.setText(resources.getString(R.string.activity2_text6));
            buttonStart.setText(resources.getString(R.string.start_text));

        }

    }
}
