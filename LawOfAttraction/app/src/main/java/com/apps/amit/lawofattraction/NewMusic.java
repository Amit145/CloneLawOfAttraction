package com.apps.amit.lawofattraction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;

public class NewMusic extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    private TextView textView;

    private ImageView musicView;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimeLength;
    final Handler handler = new Handler();

    @Override
    protected void onStart() {
        super.onStart();

        btn_play_pause.setImageResource(R.drawable.ic_play);
        //Toast.makeText(getApplicationContext(), "on Start called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            btn_play_pause.setImageResource(R.drawable.ic_pause);
            //Toast.makeText(getApplicationContext(), "Play Back Paused", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer.isPlaying())
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.musicInfoGoBack));
            builder.setPositiveButton(getString(R.string.Yes_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            });
            builder.setNegativeButton(getString(R.string.No_text),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();


        }
        //Toast.makeText(getApplicationContext(), "on Destroy called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_music);

        musicView = findViewById(R.id.musicView);

       // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(musicView);
        //Glide.with(this).load(R.drawable.giphy).into(imageViewTarget);
        Glide.with(this).load(R.drawable.giphy).asGif().into(musicView);
        //Glide.with(getApplicationContext()).load(R.drawable.giphy).asGif().into(musicView);



        seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setMax(99); // 100% (0~99)
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.isPlaying())
                {
                    SeekBar seekBar = (SeekBar)v;
                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }
                return false;
            }
        });

        textView = (TextView)findViewById(R.id.textTimer);
        textView.setVisibility(View.INVISIBLE);

        btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
        btn_play_pause.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {



                final ProgressDialog mDialog = new ProgressDialog(NewMusic.this);


                AsyncTask<String,String,String> mp3Play = new AsyncTask<String, String, String>() {

                    @Override
                    protected void onPreExecute() {
                        mDialog.setMessage("Please wait");
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {


                        try{
                            mediaPlayer.setDataSource(params[0]);
                            mediaPlayer.prepare();
                        }
                        catch (Exception ex)
                        {

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaFileLength = mediaPlayer.getDuration();
                        realtimeLength = mediaFileLength;
                        if(!mediaPlayer.isPlaying())
                        {
                            mediaPlayer.start();
                            btn_play_pause.setImageResource(R.drawable.ic_pause);
                        }
                        else
                        {
                            mediaPlayer.pause();
                            btn_play_pause.setImageResource(R.drawable.ic_play);
                        }

                        updateSeekBar();
                        mDialog.dismiss();
                    }
                };

                mp3Play.execute("https://pagalworld3.org/10908/Suit%20-%20Arjun%20n%20Guru%20Randhawa%20320Kbps.mp3"); // direct link mp3 file

                //musicView.start();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


    }

    private void updateSeekBar() {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100));
        if(mediaPlayer.isPlaying())
        {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                    realtimeLength-=1000; // declare 1 second
                    textView.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(realtimeLength),
                            TimeUnit.MILLISECONDS.toSeconds(realtimeLength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimeLength))));

                }

            };
            handler.postDelayed(updater,1000); // 1 second
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play_pause.setImageResource(R.drawable.ic_play);
        //musicView.stopNotesFall();

    }
}
