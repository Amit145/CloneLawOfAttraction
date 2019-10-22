package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ChallengeTask extends AppCompatActivity {

    TextView text;
    Button doneButton;
    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    int count = 0;
    Long TimeDifference = 0L;
    Long timeInMillis = 0L;
    int indexOfTask = 999 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_task);
        text = findViewById(R.id.taskTitle);
        doneButton = findViewById(R.id.taskDone);

        Intent result = getIntent();

        if(result.getExtras()!=null) {

            indexOfTask = result.getExtras().getInt("indexOfTask");
            timeInMillis = result.getExtras().getLong("timeOfTask");
        }

        if(indexOfTask<4) {


            Calendar cal = Calendar.getInstance();

                if(timeInMillis==0) {
                    TimeDifference = 60000L ;

                } else {
                    TimeDifference = cal.getTimeInMillis() - timeInMillis ;
                }
                String value = "taskTitle"+indexOfTask;

                int taskTitle =  getResources().getIdentifier(value, "string", getPackageName());
                //int taskTitle = idField.getInt(idField);

                text.setText(getString(taskTitle));

                //String time = calculateTime(TimeDifference);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new CountDownTimer(TimeDifference, 1000) {

                            public void onTick(long TimeDifference) {
                                //here you can have your logic to set text to edittext

                                doneButton.setText("Next Task in  " + calculateTime(TimeDifference));
                                doneButton.setEnabled(false);
                                Calendar cal = Calendar.getInstance();
                                SharedPreferences sp = getSharedPreferences("Challenge", ChallengeTask.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                cal.add(Calendar.MINUTE, 1);
                                editor.putLong("TaskDate", cal.getTimeInMillis());
                                editor.putInt("TaskIndex",indexOfTask);
                                editor.apply();
                            }

                            public void onFinish() {
                                doneButton.setText("done!");
                                doneButton.setEnabled(true);

                                doneButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {



                                        Intent i = new Intent(getApplicationContext(), Challenge.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        ChallengeTask.this.finish();
                                        startActivity(i);
                                    }
                                });
                            }

                        }.start();
                    }
                });

                // doneButton.setText(time);
            }
        }


    private String calculateTime(Long millisUntilFinished) {

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

        return hms;
    }


}
