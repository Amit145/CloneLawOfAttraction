package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.ButterKnife;

public class Exercise1 extends AppCompatActivity {

    Button buttonStart;
    ImageView img;
    TextView txt;
    TextView actText1;
    TextView actText2;
    TextView actText3;
    TextView actText4;
    LinearLayout mainlayout;
    TextView actText5;
    TextView actText6;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    int value;
    public static long startTime;


    ImageView button,button1,button2,button3,button4,button5,button6,button7;
    Button continueButton;
    Boolean bool;
    String value1 = "NA";

    @Override
    public void onBackPressed() {

        ButterKnife.bind(this);

        if (bool) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.stopManifestation_text));
            builder.setPositiveButton(getString(R.string.Yes_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    Intent art1 = new Intent(getApplicationContext(), Home.class);
                    startActivity(art1);
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

        } else {

            Intent art1 = new Intent(getApplicationContext(), Home.class);
            // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(art1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1.MODE_PRIVATE);
        String manifestationTypeValue = sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "");

        bool = sharedPreferencesManifestationType.getBoolean("RAN_BEFORE", false);



        if (!bool)  {

            setContentView(R.layout.activity_intro1);

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

            button = findViewById(R.id.imgButton1);
            button1 = findViewById(R.id.imgButton2);
            button2 = findViewById(R.id.imgButton3);
            button3 = findViewById(R.id.imgButton4);
            button4 = findViewById(R.id.imgButton5);
            button5 = findViewById(R.id.imgButton6);
            button6 = findViewById(R.id.imgButton7);
            button7 = findViewById(R.id.imgButton8);

            continueButton = findViewById(R.id.continueButton);

            //.thumbnail(0.1f)
            Glide.with(getApplicationContext()).load(R.drawable.money_picsay).into(button);
            Glide.with(getApplicationContext()).load(R.drawable.home_picsay).into(button1);
            Glide.with(getApplicationContext()).load(R.drawable.love_picsay).into(button2);
            Glide.with(getApplicationContext()).load(R.drawable.car_picsay).into(button3);
            Glide.with(getApplicationContext()).load(R.drawable.happy_picsay).into(button4);
            Glide.with(getApplicationContext()).load(R.drawable.health_picsay).into(button5);
            Glide.with(getApplicationContext()).load(R.drawable.job_picsay).into(button6);
            Glide.with(getApplicationContext()).load(R.drawable.other_picsay).into(button7);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value);

                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button.setBackgroundResource(R.drawable.highlight);
                    button.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value1);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button1.setBackgroundResource(R.drawable.highlight);
                    button1.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);


                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value2);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button2.setBackgroundResource(R.drawable.highlight);
                    button2.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value3);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button3.setBackgroundResource(R.drawable.highlight);
                    button3.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value4);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button4.setBackgroundResource(R.drawable.highlight);
                    button4.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);
                }
            });

            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value5);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button5.setBackgroundResource(R.drawable.highlight);
                    button5.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);
                }
            });
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value6);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button6.setBackgroundResource(R.drawable.highlight);
                    button6.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);

                    button7.setBackgroundResource(0);
                    button7.setColorFilter(0);
                }
            });

            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    value1 = getString(R.string.value7);
                    Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                    button7.setBackgroundResource(R.drawable.highlight);
                    button7.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                    //button7.setColorFilter();

                    button.setBackgroundResource(0);
                    button.setColorFilter(0);

                    button2.setBackgroundResource(0);
                    button2.setColorFilter(0);

                    button3.setBackgroundResource(0);
                    button3.setColorFilter(0);

                    button4.setBackgroundResource(0);
                    button4.setColorFilter(0);

                    button5.setBackgroundResource(0);
                    button5.setColorFilter(0);

                    button6.setBackgroundResource(0);
                    button6.setColorFilter(0);

                    button1.setBackgroundResource(0);
                    button1.setColorFilter(0);
                }
            });

            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(value1.equalsIgnoreCase("NA")) {
                        Toast.makeText(getApplicationContext(), " Please select what you would like to manifest ", Toast.LENGTH_LONG).show();

                    } else {
                       // Toast.makeText(getApplicationContext(), " Value : " + value1, Toast.LENGTH_LONG).show();

                        SharedPreferences sp = getSharedPreferences("MANIFESTATION_TYPE", Intro1.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("MANIFESTATION_TYPE_VALUE", value1);
                        editor.putBoolean("RAN_BEFORE", true);
                        editor.apply();

                        v.startAnimation(buttonClick);
                        Intent art1 = new Intent(getApplicationContext(), Exercise1.class);
                        startActivity(art1);

                    }

                }
            });

        } else {

            setContentView(R.layout.activity_exercise1);

            AdView mAdView = findViewById(R.id.adView1);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            startTime = SystemClock.elapsedRealtime();
            final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            buttonStart = findViewById(R.id.startButton);
            txt = findViewById(R.id.skip);
            actText1 = findViewById(R.id.textView2);
            actText2 = findViewById(R.id.textView3);
            actText3 = findViewById(R.id.textView4);
            actText4 = findViewById(R.id.textView5);
            actText5 = findViewById(R.id.textView6);
            actText6 = findViewById(R.id.textView7);
            img = findViewById(R.id.imageView2);


            if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Money));
                actText2.setText(getString(R.string.activity1_text2_Money));
                actText3.setText(getString(R.string.activity1_text3_Money));
                actText4.setText(getString(R.string.activity1_text4_Money));
                actText5.setText(getString(R.string.activity1_text5_Money));
                actText6.setText(getString(R.string.activity1_text6_Money));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value1))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Home));
                actText2.setText(getString(R.string.activity1_text2_Home));
                actText3.setText(getString(R.string.activity1_text3_Home));
                actText4.setText(getString(R.string.activity1_text4_Home));
                actText5.setText(getString(R.string.activity1_text5_Home));
                actText6.setText(getString(R.string.activity1_text6_Home));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value2))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Love));
                actText2.setText(getString(R.string.activity1_text2_Love));
                actText3.setText(getString(R.string.activity1_text3_Love));
                actText4.setText(getString(R.string.activity1_text4_Love));
                actText5.setText(getString(R.string.activity1_text5_Love));
                actText6.setText(getString(R.string.activity1_text6_Love));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value3))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Car));
                actText2.setText(getString(R.string.activity1_text2_Car));
                actText3.setText(getString(R.string.activity1_text3_Car));
                actText4.setText(getString(R.string.activity1_text4_Car));
                actText5.setText(getString(R.string.activity1_text5_Car));
                actText6.setText(getString(R.string.activity1_text6_Car));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value4))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Happy));
                actText2.setText(getString(R.string.activity1_text2_Happy));
                actText3.setText(getString(R.string.activity1_text3_Happy));
                actText4.setText(getString(R.string.activity1_text4_Happy));
                actText5.setText(getString(R.string.activity1_text5_Happy));
                actText6.setText(getString(R.string.activity1_text6_Happy));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value5))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Health));
                actText2.setText(getString(R.string.activity1_text2_Health));
                actText3.setText(getString(R.string.activity1_text3_Health));
                actText4.setText(getString(R.string.activity1_text4_Health));
                actText5.setText(getString(R.string.activity1_text5_Health));
                actText6.setText(getString(R.string.activity1_text6_Health));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value6))) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1_Job));
                actText2.setText(getString(R.string.activity1_text2_Job));
                actText3.setText(getString(R.string.activity1_text3_Job));
                actText4.setText(getString(R.string.activity1_text4_Job));
                actText5.setText(getString(R.string.activity1_text5_Job));
                actText6.setText(getString(R.string.activity1_text6_Job));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            } if (manifestationTypeValue.equalsIgnoreCase(getString(R.string.value7)) || manifestationTypeValue.equalsIgnoreCase("")) {

                txt.setVisibility(View.INVISIBLE);
                txt.setText(getString(R.string.skip_text));
                actText1.setText(getString(R.string.activity1_text1));
                actText2.setText(getString(R.string.activity1_text2));
                actText3.setText(getString(R.string.activity1_text3));
                actText4.setText(getString(R.string.activity1_text4));
                actText5.setText(getString(R.string.activity1_text5));
                actText6.setText(getString(R.string.activity1_text6));
                buttonStart.setText(getString(R.string.start_text));
                Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);


        }



       // Toast.makeText(getApplicationContext(), " Value : " + manifestationTypeValue, Toast.LENGTH_LONG).show();


            startTime = SystemClock.elapsedRealtime();


            SharedPreferences sp = getSharedPreferences("your_prefs", Exercise1.MODE_PRIVATE);
            value = sp.getInt("your_int_key", 60);


            SharedPreferences sp1 = getSharedPreferences("timerEnable", Exercise1.MODE_PRIVATE);
            String timervalue = sp1.getString("timerEnable","ON");

            Glide.with(getApplicationContext()).load(R.drawable.ex1).thumbnail(0.1f).into(img);

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(),Home.class);
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
                                        Intent art1 = new Intent(getApplicationContext(), Exercise2.class);
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
                                        Intent art1 = new Intent(getApplicationContext(), Exercise2.class);
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
                        Intent art1 = new Intent(Exercise1.this, Exercise2.class);
                        startActivity(art1);
                        img=null;


                    }
                });
            }

    }


}

}
