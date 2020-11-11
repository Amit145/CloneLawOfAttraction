package com.apps.amit.lawofattraction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class AudioManifestation extends AppCompatActivity implements View.OnClickListener {

    private int RECORD_AUDIO_REQUEST_CODE =123 ;
    private TextView chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private TextView viewMyAudio;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    CountDownTimer timer;
    boolean isRunning ;

    @Override
    public void onBackPressed() {

        if(isRunning) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("resources.getString(R.string.stopManifestation_text)");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timer.cancel();
                    prepareforStop();
                    stopRecording();
                    isRunning = false;
                    Intent art1 = new Intent(getApplicationContext(), AudioWishHome.class);
                    startActivity(art1);
                }
            });
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
        } else{
            Intent art1 = new Intent(getApplicationContext(), AudioWishHome.class);
            startActivity(art1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_manifestation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio();
        }
        //initializingViews
        initViews();

        viewMyAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecordingListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {

        linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
        chronometer =  findViewById(R.id.chronometerTimer);
        imageViewRecord = (ImageView) findViewById(R.id.imageViewRecord);
        imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
        linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        viewMyAudio = findViewById(R.id.viewMyAudio);

        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }


    @Override
    public void onClick(View view) {

        final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        isRunning = false;

        if( view == imageViewRecord ){

            if(vib != null) {
                vib.vibrate(100);
            }

            prepareforRecording();
            startRecording();

            timer = new CountDownTimer(60* 1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isRunning = true;
                    chronometer.setText( "00:"+String.format("%02d", (millisUntilFinished / 1000)));
                    Toast.makeText(getApplicationContext(),"00:"+String.format("%02d", (millisUntilFinished / 1000)),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {
                    prepareforStop();
                    stopRecording();
                    isRunning = false;
                }
            }.start();

        }else if( view == imageViewStop ){

            prepareforStop();
            stopRecording();
            chronometer.setText("00:00");

        }else if( view == imageViewPlay ){
            if( !isPlaying && fileName != null ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }
    }

    private void prepareforRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        }
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);
    }

    private void startRecording() {


        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/LawOfAttraction/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/LawOfAttraction/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");

        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", String.valueOf(e.getMessage()));
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
    }


    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            Log.e("LOG_TAG", String.valueOf(e.getMessage()));
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);

    }


    private void prepareforStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        }
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlay.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {
        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            Log.e("LOG_TAG", String.valueOf(e.getMessage()));
        }
        mRecorder = null;
        //starting the chronometer
        if(timer!=null) {
            timer.cancel();
        }
        //showing the play button
        Toast.makeText(this, "Wish saved successfully.", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedpreferences = getSharedPreferences("SocialAccount", Context.MODE_PRIVATE);
        String valueId = sharedpreferences.getString("personId", "");

    }



    private void startPlaying() {
        mPlayer = new MediaPlayer();
        Log.d("instartPlaying",fileName);
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();

        /* once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                linearLayoutPlay.setVisibility(View.INVISIBLE);
            }
        });

        /* moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }
}