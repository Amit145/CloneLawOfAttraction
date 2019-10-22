package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.service.AffirmationReceiver;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Affirmations extends AppCompatActivity {

    List<String> affirmationList;
    Button button;
    int count = 0 ;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    Long timeRemaining = 0L;
    String buttonText;
    Calendar calendarToday;
    Calendar calendarYesterday;
    ProgressBar progressBar;
    TextView setText,affirmTitle,affirmTextSubtitle,trackMyProgress;
    ImageView imageView;
    Affirmations affirmations;
    Double percentage = 0d;

    public Affirmations() {

        affirmationList = new ArrayList<>();
        affirmationList.add("\"You're one step closer to your dreams..\"");
        affirmationList.add("\"Trust the process ! You will manifest all your dreams..\"");
        affirmationList.add("\"Great thing's take time ! Stay Focused..\"");
        affirmationList.add("\"It is not over, unless you mean it..\"");
        affirmationList.add("\"Trust the Universe ! It is always there for you ..\"");
    }

    public List<String> getList() {
        return affirmationList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirmations);

        calendarToday = Calendar.getInstance();
        //cal.getTimeInMillis();
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView2);

        Glide.with(getApplicationContext()).load(R.drawable.likeaffirm).into(imageView);


        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("Affirmation_Counter", Affirmations.MODE_PRIVATE);
        count = sharedPreferencesManifestationType.getInt("counter", 0);
        buttonText = sharedPreferencesManifestationType.getString("Time", "NA");


        button = findViewById(R.id.affirmationButton);
        setText = findViewById(R.id.setText);
        affirmTitle = findViewById(R.id.affirmTitle);
        affirmTextSubtitle = findViewById(R.id.affirmTextSubtitle);
        trackMyProgress = findViewById(R.id.trackMyProgress);

        percentage = Double.valueOf(count)/Double.valueOf(affirmationList.size())*100;
        int val = (int) Math.round(percentage);

        progressBar.setVisibility(View.INVISIBLE);

        if(val==0){
            progressBar.setVisibility(View.INVISIBLE);
            trackMyProgress.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(val);
            affirmTitle.setText("My Progress");
            affirmTextSubtitle.setText(val+"%");
            affirmTextSubtitle.setTextSize(40f);

            trackMyProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    Intent intent = new Intent(getApplicationContext(),calendar.class);
                    startActivity(intent);

                }
            });
        }
        calendarYesterday = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            calendarYesterday.setTime(sdf.parse(buttonText));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(calendarYesterday.getTime().after(calendarToday.getTime()))
        {


            timeRemaining = calendarYesterday.getTimeInMillis() - calendarToday.getTimeInMillis();
            new CountDownTimer(timeRemaining, 1000) {

                public void onTick(long timeRemaining) {

                    button.setText(calculateTime(timeRemaining));
                    setText.setText("Next Affirmation In:");
                    button.setEnabled(false);
                }

                public void onFinish() {

                    button.setText("START");
                    setText.setText("Click Start to begin");
                    //startAlarm(calendarToday.getTimeInMillis());
                    button.setEnabled(true);
                }
            }.start();



            Toast.makeText(getApplicationContext(), "Progress "+val , Toast.LENGTH_LONG).show();

        }
        if(buttonText.equalsIgnoreCase("NA")){

            button.setText("BEGIN");

        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),affirmationactivity.class);
                intent.putExtra("counter",count);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });
    }

    private String calculateTime(Long millisUntilFinished) {

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

        return hms;
    }

    private void startAlarm(long timeInMilli)
    {
        AlarmManager m = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), AffirmationReceiver.class);
        //open = new Intent(getApplicationContext(),Home.class);
        //share = new Intent(getApplicationContext(), Settings.class);
        PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

        if(m!=null) {

            m.setRepeating(AlarmManager.RTC, timeInMilli, AlarmManager.INTERVAL_DAY, p);
        }

        //Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT).show();

    }
}
