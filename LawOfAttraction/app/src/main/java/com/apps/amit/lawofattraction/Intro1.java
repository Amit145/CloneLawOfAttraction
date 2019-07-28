package com.apps.amit.lawofattraction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Intro1 extends AppCompatActivity {

    ImageView button,button1,button2,button3,button4,button5,button6,button7;
    Button continueButton;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    String value = "NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

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

                value = getString(R.string.value);

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

                value = getString(R.string.value1);
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

                value = getString(R.string.value2);
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

                value = getString(R.string.value3);
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

                value = getString(R.string.value4);
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

                value = getString(R.string.value5);
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

                value = getString(R.string.value6);
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

                value = getString(R.string.value7);
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

                if(value.equalsIgnoreCase("NA")) {
                    Toast.makeText(getApplicationContext(), " Please select a value ", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), " Value : " + value, Toast.LENGTH_LONG).show();

                    SharedPreferences sp = getSharedPreferences("MANIFESTATION_TYPE", Intro1.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("MANIFESTATION_TYPE_VALUE", value);
                    editor.apply();

                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), Exercise1.class);
                    startActivity(art1);

                }

            }
        });

    }


}
