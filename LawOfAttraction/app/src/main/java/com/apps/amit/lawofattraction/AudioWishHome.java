package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AudioWishHome extends AppCompatActivity {

    ImageView imageView;
    TextView myAudioWish;
    Button startAudioWishButton;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(art1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_wish_home);

        imageView = findViewById(R.id.audioImageView);
        myAudioWish = findViewById(R.id.viewMyAudio);
        startAudioWishButton = findViewById(R.id.audioStartButton);

        Glide.with(getApplicationContext()).load(R.drawable.girlspeak).into(imageView);

        startAudioWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call activity
                Intent intent = new Intent(getApplicationContext(), AudioManifestation.class);
                startActivity(intent);
            }
        });

        myAudioWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call activity
                Intent intent = new Intent(getApplicationContext(), RecordingListActivity.class);
                startActivity(intent);
            }
        });
    }
}