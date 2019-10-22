package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Challenge extends AppCompatActivity {

    private ImageView image1;
    private Button beginButton1;
    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    int indexOfTask = 0 ;
    int totalCount = 3;
    Long timeInMillis = 0L;
    Double percentage = 0d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        image1 = findViewById(R.id.imgButton1);
        beginButton1 = findViewById(R.id.beginButton);

        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("Challenge", Challenge.MODE_PRIVATE);
        timeInMillis = sharedPreferencesManifestationType.getLong("TaskDate", 0);
        indexOfTask = sharedPreferencesManifestationType.getInt("TaskIndex", 0);

        Glide.with(getApplicationContext()).load(R.drawable.buddha).into(image1);

        if(indexOfTask!=0) {

            percentage = Double.valueOf(indexOfTask)/Double.valueOf(totalCount)*100;

            Toast.makeText(getApplicationContext(), "Progress: " + String.format("%.0f", percentage), Toast.LENGTH_LONG).show();

        }
        beginButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                Intent art1 = new Intent(getApplicationContext(), ChallengeTask.class);
                //art1.putExtra()
                art1.putExtra("indexOfTask",indexOfTask+1);
                art1.putExtra("timeOfTask",timeInMillis);
                startActivity(art1);

            }
        });
    }
}
