package com.apps.amit.lawofattraction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class SelectManifestationTypeActivity extends AppCompatActivity {

    ImageView button;
    ImageView button1;
    ImageView button2;
    ImageView button3;
    ImageView button4;
    ImageView button5;
    ImageView button6;
    ImageView button7;
    Button continueButton;
    LinearLayout mainlayout;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    String value = "NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        mainlayout = findViewById(R.id.mainlayout);

        Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainlayout.setBackground(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

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
                button7.setBackgroundResource(R.drawable.highlight);
                button7.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);

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
                    Toast.makeText(getApplicationContext(), " Please select what you would like to manifest ", Toast.LENGTH_LONG).show();

                } else {
                    SharedPreferences sp = getSharedPreferences("MANIFESTATION_TYPE", SelectManifestationTypeActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("MANIFESTATION_TYPE_VALUE", value);
                    editor.apply();

                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), Exercise1Activity.class);
                    startActivity(art1);
                }
            }
        });
    }
}
