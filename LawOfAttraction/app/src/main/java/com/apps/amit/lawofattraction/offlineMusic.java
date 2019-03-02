package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class offlineMusic extends AppCompatActivity {

    Button play,playPause;
    SeekBar seekBar;
    final int REQUEST_WRITE_STORAGE_REQUEST_CODE=0;
    MediaPlayer mediaPlayer;
    TextView mTime,title,subtitle,body,body1,body2;
    private int current = 0;
    Resources resources;
    private int paused = 0;
    private boolean   running = true;
    private int duration = 0;


    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }



    @Override
    protected void onDestroy() {
        if (onEverySecond != null)
            seekBar.removeCallbacks(onEverySecond );
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        try {
            if(mediaPlayer==null)
            {

                Intent i = new Intent(offlineMusic.this, Home.class);
                startActivity(i);
            }

            if(mediaPlayer.isPlaying()){
                mediaPlayer.release();
                mediaPlayer=null;

                Intent i = new Intent(offlineMusic.this, Home.class);
                startActivity(i);

            }

            if(!mediaPlayer.isPlaying())
            {
                mediaPlayer.release();
                mediaPlayer=null;

                Intent i = new Intent(offlineMusic.this, Home.class);
                startActivity(i);
            }

        } catch (NullPointerException e) {

            //Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }

        catch (IllegalStateException e) {

            //Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }

    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_music);


        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(getApplicationContext());
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {



            @Override
            public void onAdLoaded() {
                 Calendar c1 = Calendar.getInstance();

                SharedPreferences pref = getSharedPreferences("AdvsPref",MODE_PRIVATE);

                Calendar dummyDate = Calendar.getInstance();

                dummyDate.set(Calendar.YEAR, 2011);

                //Store selected language in a Variable called value
                String value = pref.getString("adsDate",dummyDate.getTime().toString());


                if(!value.isEmpty())
                {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    try {
                        cal.setTime(sdf.parse(value));// all done

                    } catch (ParseException e) {

                        Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
                    }

                    if(cal.getTime().after(c1.getTime()))
                    {
                        //Toast.makeText(getApplicationContext(),cal.getTime()+" "+c1.getTime() , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        displayInterstitial();
                    }

                }

            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                interstitial.setAdListener(null);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {

                interstitial.setAdListener(null);
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                interstitial.setAdListener(null);
                // Code to be executed when when the interstitial ad is closed.
            }


        });

        play = findViewById(R.id.playBtn);


        playPause = findViewById(R.id.playPause);

        seekBar = findViewById(R.id.seekBar);

        mTime = findViewById(R.id.mediaTime);

        title = findViewById(R.id.textView);
        subtitle = findViewById(R.id.textView1);
        body = findViewById(R.id.textView2);
        body1 = findViewById(R.id.textView3);
        body2= findViewById(R.id.textView4);

        seekBar.setEnabled(false);


        title.setText(resources.getString(R.string.offMusicTitle));
        subtitle.setText(resources.getString(R.string.offMusicSubtitle));
        body.setText(resources.getString(R.string.offMusicText1));
        body1.setText(resources.getString(R.string.offMusicText2));
        body2.setText(resources.getString(R.string.offMusicText3));
        //resources.getString(R.string.musicButtonSave)


        playPause.setEnabled(false);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestAppPermissions();

                seekBar.setEnabled(true);


                playPause.setEnabled(true);

                mediaPlayer  = new MediaPlayer();

                String media_path = Environment.getExternalStorageDirectory().getPath()+"/"+".LawOfAttractionAffirms"+"/"+ "1.mp3";

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Uri uri = Uri.parse(media_path);
                try {
                    mediaPlayer.setDataSource(getApplicationContext(),uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    play.setEnabled(false);
                   // Toast.makeText(getApplicationContext(),"Playing : ",Toast.LENGTH_LONG).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        Intent i = new Intent(offlineMusic.this, offlineMusic.class);
                        startActivity(i);
                        //mTime.setVisibility(View.INVISIBLE);
                        //seekBar.setEnabled(false);
                        mediaPlayer.release();
                        mediaPlayer = null;

                        //Toast.makeText(getApplicationContext(),"Stopped : ",Toast.LENGTH_LONG).show();

                    }
                });

                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.postDelayed(onEverySecond, 1000);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        try {
                            if(fromUser){
                                mediaPlayer.seekTo(progress);
                                updateTime();

                                paused=progress;
                            }
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }


                    }

                });


                if(paused>0 )
                {

                    mediaPlayer.seekTo(paused);
                    mediaPlayer.start();
                }





            }


        });




        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playPause.setEnabled(false);
                play.setEnabled(true);
                play.setText(resources.getString(R.string.offMusicResume));
                paused = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        });




    }


    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run(){
            if(true == running){
                try {
                    if(seekBar != null) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }


                if(mediaPlayer.isPlaying()) {
                    seekBar.postDelayed(onEverySecond, 1000);
                    updateTime();
                }

                } catch (NullPointerException e) {

                }

                catch (IllegalStateException e) {

                }
            }
        }
    };


    private void updateTime(){
        do {
            current = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();

            //Toast.makeText(getApplicationContext(),"duration - " + duration + " current- ",Toast.LENGTH_LONG).show();

            int dSeconds = (int) (duration / 1000) % 60 ;
            int dMinutes = (int) ((duration / (1000*60)) % 60);
            int dHours   = (int) ((duration / (1000*60*60)) % 24);

            int cSeconds = (int) (current / 1000) % 60 ;
            int cMinutes = (int) ((current / (1000*60)) % 60);
            int cHours   = (int) ((current / (1000*60*60)) % 24);


                mTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
           // mTime.setText(""+cHours+":"+cMinutes+":"+cSeconds+" / "+dHours+":"+dMinutes+":"+dSeconds);

            try{
               // Log.d("Value: ", String.valueOf((int) (current * 100 / duration)));
                if(seekBar.getProgress() >= 100){
                    break;
                }
            }catch (Exception e) {}
        }while (seekBar.getProgress() <= 100);
    }






    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
