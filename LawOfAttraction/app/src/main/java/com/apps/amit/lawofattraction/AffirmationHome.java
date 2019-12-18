package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class AffirmationHome extends AppCompatActivity {

    List<String> affirmationList;
    Button button;
    int count = 0 ;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Long timeRemaining = 0L;
    String buttonText, value;
    EditText edt, edt1;
    Calendar calendarToday;
    AlertDialog alert;
    Calendar calendarYesterday;
    ProgressBar progressBar;
    TextView setText,affirmTitle,affirmTextSubtitle,trackMyProgress,affirmTryAgain;
    TextView txt,txt1,nameText,wishText;
    ImageView imageView;
    Double percentage = 0d;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    AffirmationHome affirmations;
    String dataParseUrl = "";

    public AffirmationHome() {

        affirmationList = new ArrayList<>();

        affirmationList.add("\"I have the ability to create anything that I want.\"");
        affirmationList.add("\"My life is filled with great abundance in every area.\"");
        affirmationList.add("\"I accept and allow success in all areas of my life.\"");
        affirmationList.add("\"I firmly believe in my ability to attract success.\"");
        affirmationList.add("\"Great things are coming to me today.\"");
        affirmationList.add("\"I attract everything that I desire.\"");
        affirmationList.add("\"I am totally committed to achieve my goals and dreams.\"");
        affirmationList.add("\"Money comes to me easily and effortlessly.\"");
        affirmationList.add("\"All good things flow into my life.\"");
        affirmationList.add("\"I am a Powerful Money Magnet.\"");
        affirmationList.add("\"I attract success into my life effortlessly.\"");
        affirmationList.add("\"I am open and ready to attract abundance into my life.\"");
        affirmationList.add("\"I am a magnet for financial abundance and prosperity.\"");
        affirmationList.add("\"I am attracting positive and abundant people into my life.\"");
        affirmationList.add("\"I am positive, passionate and successful.\"");
        affirmationList.add("\"I learn from the past, live in the present and plan for the future.\"");
        affirmationList.add("\"I easily attract the things that I desire most.\"");
        affirmationList.add("\"My positive belief in myself is transforming my life.\"");
        affirmationList.add("\"My dreams are manifesting before my eyes.\"");
        affirmationList.add("\"I am filled with limitless potential.\"");
        affirmationList.add("\"I am worthy of love, abundance, success, happiness & fullfilment.\"");
        affirmationList.add("\"Abundance surrounds me everywhere I go.\"");
        affirmationList.add("\"I rejoice in others success, wealth, and prosperity.\"");
        affirmationList.add("\"Everyday I turn my dreams into reality.\"");
        affirmationList.add("\"I have the power to manifest wealth and abundance in my life.\"");
        affirmationList.add("\"The power to be successful is within me.\"");
        affirmationList.add("\"Everything I need for success comes to me easily and effortlessly.\"");
        affirmationList.add("\"Every day I attract people who help me achieve my goals.\"");
        affirmationList.add("\"I am Living the Life of my Dreams.\"");
        affirmationList.add("\"Money flows easily into my life.\"");

    }

    public List<String> getList() {
        return affirmationList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirmations);

        calendarToday = Calendar.getInstance();
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView2);

        Glide.with(getApplicationContext()).load(R.drawable.likeaffirm).into(imageView);
        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("Affirmation_Counter", AffirmationHome.MODE_PRIVATE);
        count = sharedPreferencesManifestationType.getInt("counter", 0);
        buttonText = sharedPreferencesManifestationType.getString("Time", "NA");

        button = findViewById(R.id.affirmationButton);
        setText = findViewById(R.id.setText);
        affirmTitle = findViewById(R.id.affirmTitle);
        affirmTextSubtitle = findViewById(R.id.affirmTextSubtitle);
        affirmTryAgain= findViewById(R.id.affirmTryAgain);
        trackMyProgress = findViewById(R.id.trackMyProgress);

        percentage = Double.valueOf(count)/Double.valueOf(affirmationList.size())*100;
        int val = (int) Math.round(percentage);
        progressBar.setVisibility(View.INVISIBLE);


        if(val==0){
            progressBar.setVisibility(View.INVISIBLE);
            trackMyProgress.setVisibility(View.INVISIBLE);

        } else if(val==100){

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(R.drawable.tick).into(imageView);
            progressBar.setProgress(val);
            affirmTitle.setText(getString(R.string.affirmCongrats));
            affirmTextSubtitle.setText(val+getString(R.string.percentSign));
            affirmTextSubtitle.setTextSize(40f);
            affirmTryAgain.setText(getString(R.string.affirmWannaTryAgain));
            affirmTryAgain.setTextSize(20f);

            button.setText(getString(R.string.Home_startButtonText));
            setText.setText(getString(R.string.Home_textBelowPager));
            button.setEnabled(true);

            trackMyProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    Intent intent = new Intent(getApplicationContext(), ManifestationTrackerActivity.class);
                    startActivity(intent);

                }
            });

        } else {


            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(val);
            affirmTitle.setText(getString(R.string.myProgress));
            affirmTextSubtitle.setText(val+getString(R.string.percentSign));
            affirmTextSubtitle.setTextSize(40f);

            trackMyProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    Intent intent = new Intent(getApplicationContext(), ManifestationTrackerActivity.class);
                    startActivity(intent);

                }
            });

        }

        calendarYesterday = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            calendarYesterday.setTime(sdf.parse(buttonText));// all done
        } catch (ParseException e) {
            Log.e(e.getMessage(),e.getMessage());
        }

        if(calendarYesterday.getTime().after(calendarToday.getTime()) && val!=100 )
        {
            timeRemaining = calendarYesterday.getTimeInMillis() - calendarToday.getTimeInMillis();

            new CountDownTimer(timeRemaining, 1000) {

                public void onTick(long timeRemaining) {

                    button.setText(calculateTime(timeRemaining));
                    setText.setText(getString(R.string.nextAffirm));
                    button.setEnabled(false);
                }

                public void onFinish() {

                    button.setText(getString(R.string.Home_startButtonText));
                    setText.setText(getString(R.string.Home_textBelowPager));
                    button.setEnabled(true);
                }
            }.start();
        }
        if(buttonText.equalsIgnoreCase("NA")){

            button.setText(getString(R.string.Home_startButtonText));

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AffirmationActivity.class);
                intent.putExtra("counter",count);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private String calculateTime(Long millisUntilFinished) {

        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
    }
}
