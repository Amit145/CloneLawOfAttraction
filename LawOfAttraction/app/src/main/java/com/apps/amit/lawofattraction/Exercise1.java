package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
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
    TextView actText5;
    TextView actText6;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    int value;
    public static long startTime;

    @Override
    public void onBackPressed() {

        ButterKnife.bind(this);

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
        builder.setNegativeButton(getString(R.string.No_text),new DialogInterface.OnClickListener() {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise1);

        AdView mAdView =  findViewById(R.id.adView1);
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

        txt.setText(getString(R.string.skip_text));
        actText1.setText(getString(R.string.activity1_text1));
        actText2.setText(getString(R.string.activity1_text2));
        actText3.setText(getString(R.string.activity1_text3));
        actText4.setText(getString(R.string.activity1_text4));
        actText5.setText(getString(R.string.activity1_text5));
        actText6.setText(getString(R.string.activity1_text6));
        buttonStart.setText(getString(R.string.start_text));

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
