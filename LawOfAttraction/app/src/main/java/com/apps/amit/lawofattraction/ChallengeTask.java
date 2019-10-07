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
    int indexOfTask =0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_task);
        text = findViewById(R.id.taskTitle);
        doneButton = findViewById(R.id.taskDone);

        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("Challenge", ChallengeTask.MODE_PRIVATE);
        Long timeInMillis = sharedPreferencesManifestationType.getLong("TaskDate", 0);
        indexOfTask = sharedPreferencesManifestationType.getInt("TaskIndex", 0);

        Intent result = getIntent();

        if(result.getExtras()!=null) {

           // text.setText(result.getExtras().getString("taskTitle"));
        }

        //Toast.makeText(getApplicationContext(), "DATE : " + timeInMillis, Toast.LENGTH_LONG).show();

        if (timeInMillis == 0 && indexOfTask == 0) {

            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                    //store date in shared preference
                    SharedPreferences sp = getSharedPreferences("Challenge", ChallengeTask.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    cal.add(Calendar.MINUTE, 1);
                    editor.putLong("TaskDate", cal.getTimeInMillis());
                    editor.putInt("TaskIndex",indexOfTask+1);
                    editor.apply();

                    long timeMillis = cal.getTimeInMillis();
                    Toast.makeText(getApplicationContext(), "DATE : " + cal.getTime(), Toast.LENGTH_LONG).show();


                    //if date already present
                    new CountDownTimer(cal.getTimeInMillis(), 1000) {

                        public void onTick(long timeMillis) {
                            //here you can have your logic to set text to edittext


                            doneButton.setText("Next Task in  " + calculateTime(timeMillis));
                            doneButton.setEnabled(false);
                        }

                        public void onFinish() {
                            doneButton.setText("done!");
                            doneButton.setEnabled(true);

                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Calendar cal = Calendar.getInstance();
                                    SharedPreferences sp = getSharedPreferences("Challenge", ChallengeTask.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    cal.add(Calendar.MINUTE, 1);
                                    editor.putLong("TaskDate", cal.getTimeInMillis());
                                    editor.putInt("TaskIndex",count+1);
                                    editor.apply();

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
            } else {

                Calendar cal = Calendar.getInstance();

                Long TimeDifference = timeInMillis - cal.getTimeInMillis();

                String value = "taskTitle"+indexOfTask;

                int taskTitle =  getResources().getIdentifier(value, "string", getPackageName());
                //int taskTitle = idField.getInt(idField);

                text.setText(getString(taskTitle));

                doneButton.setEnabled(false);

                String time = calculateTime(TimeDifference);

                new CountDownTimer(TimeDifference, 1000) {

                    public void onTick(long TimeDifference) {
                        //here you can have your logic to set text to edittext


                        doneButton.setText("Next Task in  " + calculateTime(TimeDifference));
                        doneButton.setEnabled(false);
                    }

                    public void onFinish() {
                        doneButton.setText("done!");
                        doneButton.setEnabled(true);

                        doneButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Calendar cal = Calendar.getInstance();
                                SharedPreferences sp = getSharedPreferences("Challenge", ChallengeTask.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                cal.add(Calendar.MINUTE, 1);
                                editor.putLong("TaskDate", cal.getTimeInMillis());
                                editor.putInt("TaskIndex",indexOfTask+1);
                                editor.apply();

                                Intent i = new Intent(getApplicationContext(), Challenge.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ChallengeTask.this.finish();
                                startActivity(i);
                            }
                        });
                    }

            }.start();
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
