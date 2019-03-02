package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetLanguage extends AppCompatActivity {

    private RadioButton eng;
    private RadioButton fre;
    private RadioButton spa;
    private RadioButton jap;
    private RadioButton dut;
    private RadioButton tha;
    private RadioButton german;
    private RadioButton portuguese;
    private RadioButton italian;
    private RadioButton hindi;
    private String userLanguage = "en";
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        eng =  findViewById(R.id.English);
        fre =  findViewById(R.id.French);
        spa =  findViewById(R.id.Spanish);
        jap =  findViewById(R.id.Japanese);
        dut =  findViewById(R.id.Dutch);
        tha =  findViewById(R.id.Thai);
        german =  findViewById(R.id.German);
        portuguese =  findViewById(R.id.Portuguese);
        italian =  findViewById(R.id.Italian);
        hindi =  findViewById(R.id.Hindi);
    }

    public void SelLanEng(View view){


        eng.setChecked(true);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);

    }

    public void SelLanFre(View view){

        userLanguage = "fr";
        eng.setChecked(false);
        fre.setChecked(true);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);

    }

    public void SelLanSpa(View view){

        userLanguage = "es";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(true);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);

    }

    public void SelLanJap(View view){

        userLanguage = "ja";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(true);
        dut.setChecked(false);
        tha.setChecked(false);

    }

    public void SelLanDut(View view){

        userLanguage = "nl";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(true);
        tha.setChecked(false);

    }

    public void SelLanTha(View view){

        userLanguage = "th";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(true);

    }

    public void SelLanGerman(View view) {

        userLanguage = "de";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);
        german.setChecked(true);
        portuguese.setChecked(false);
        italian.setChecked(false);
        hindi.setChecked(false);
    }

    public void SelLanPortuguese(View view) {

        userLanguage = "pt";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);
        german.setChecked(false);
        portuguese.setChecked(true);
        italian.setChecked(false);
        hindi.setChecked(false);
    }

    public void SelLanItalian(View view) {

        userLanguage = "it";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);
        german.setChecked(false);
        portuguese.setChecked(false);
        italian.setChecked(true);
        hindi.setChecked(false);
    }

    public void SelLanHindi(View view) {

        userLanguage = "hi";
        eng.setChecked(false);
        fre.setChecked(false);
        spa.setChecked(false);
        jap.setChecked(false);
        dut.setChecked(false);
        tha.setChecked(false);
        german.setChecked(false);
        portuguese.setChecked(false);
        italian.setChecked(false);
        hindi.setChecked(true);
    }

    public void SetLang(View view) {

        //When user Sets New language PrevIOus stored language is removed
        SharedPreferences pref1 = getSharedPreferences("UserLang", Context.MODE_PRIVATE);
        pref1.edit().clear().apply();

        //update new value based on user selection
        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("language", userLanguage);
        editor.apply();

        //Display Popup
        Toast.makeText(getApplicationContext(),"Selected Language is "+userLanguage,Toast.LENGTH_LONG).show();

        //Open Home
        view.startAnimation(buttonClick);


        Intent art1 = new Intent(getApplicationContext(),Home.class);

        //clear previous child references
        art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(art1);

        //close current activity
        finish();

    }
}
