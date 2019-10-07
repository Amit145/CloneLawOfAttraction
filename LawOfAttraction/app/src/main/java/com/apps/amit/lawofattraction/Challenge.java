package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private ImageView image1,image2,image3,image4;
    private int selectedIndex = 0 ;
    private Button beginButton1;
    String challengeTitle = null;
    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        image1 = findViewById(R.id.imgButton1);
        image2 = findViewById(R.id.imgButton2);
        image3 = findViewById(R.id.imgButton3);
        image4 = findViewById(R.id.imgButton4);

        beginButton1 = findViewById(R.id.beginButton);

        Glide.with(getApplicationContext()).load(R.drawable.money_picsay).into(image1);
        Glide.with(getApplicationContext()).load(R.drawable.brainexer).into(image2);
        Glide.with(getApplicationContext()).load(R.drawable.buddha).into(image3);
        Glide.with(getApplicationContext()).load(R.drawable.car_picsay).into(image4);


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedIndex=1;

                challengeTitle = "Confident";
                image1.setBackgroundResource(R.drawable.highlight);
                image1.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                image2.setBackgroundResource(0);
                image2.setColorFilter(0);
                image3.setBackgroundResource(0);
                image3.setColorFilter(0);
                image4.setBackgroundResource(0);
                image4.setColorFilter(0);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedIndex=2;

                challengeTitle = "GoodPerson";

                image2.setBackgroundResource(R.drawable.highlight);
                image2.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                image1.setBackgroundResource(0);
                image1.setColorFilter(0);
                image3.setBackgroundResource(0);
                image3.setColorFilter(0);
                image4.setBackgroundResource(0);
                image4.setColorFilter(0);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedIndex=3;
                challengeTitle = "Happy";

                image3.setBackgroundResource(R.drawable.highlight);
                image3.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                image1.setBackgroundResource(0);
                image1.setColorFilter(0);
                image2.setBackgroundResource(0);
                image2.setColorFilter(0);
                image4.setBackgroundResource(0);
                image4.setColorFilter(0);
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedIndex=4;
                challengeTitle = "Positive";

                image4.setBackgroundResource(R.drawable.highlight);
                image4.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

                image1.setBackgroundResource(0);
                image1.setColorFilter(0);
                image2.setBackgroundResource(0);
                image2.setColorFilter(0);
                image3.setBackgroundResource(0);
                image3.setColorFilter(0);
            }
        });

        beginButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                if(selectedIndex==0) {
                    Toast.makeText(getApplicationContext(), "Please Select a Challenge to begin " + selectedIndex, Toast.LENGTH_LONG).show();

                } else {
                    //Toast.makeText(getApplicationContext(), "Selected Index " + selectedIndex, Toast.LENGTH_LONG).show();
                    Intent art1 = new Intent(getApplicationContext(), ChallengeTask.class);
                    art1.putExtra("taskTitle", challengeTitle);
                    // art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(art1);
                }

            }
        });
    }


}
