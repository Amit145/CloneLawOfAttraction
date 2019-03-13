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
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;

import static com.apps.amit.lawofattraction.SubTask.UTF_ENCODING;

public class NewMusic extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    private TextView textView;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private ImageView musicView;
    TextView title1;
    TextView tbod;
    String musicURL;
    String shares;
    String name;
    String views;
    private int viewcount;
    String token;
    String naam;
    Button taskLike;
    Button taskDone;
    Button expButton1;
    ImageView cover;
    ImageView taskShares;
    private int taskID;
    String date;
    TextView doneTASK;
    String taskBody1;

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

       // musicView = findViewById(R.id.musicView);

       // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(musicView);
        //Glide.with(this).load(R.drawable.giphy).into(imageViewTarget);
//        Glide.with(this).load(R.drawable.giphy).asGif().into(musicView);
        //Glide.with(getApplicationContext()).load(R.drawable.giphy).asGif().into(musicView);


        Intent result = getIntent();

        if(result.getExtras()!=null) {

            name = result.getExtras().getString("taskTitle");
            date = result.getExtras().getString("takImg");
            taskBody1 = result.getExtras().getString("taskBody");
            musicURL  = result.getExtras().getString("taskLikes");
            shares  = result.getExtras().getString("taskShares");
            views  = result.getExtras().getString("taskViews");
            taskID =  result.getExtras().getInt("taskID");
        }

        taskLike = findViewById(R.id.taskLikeId);
        taskShares = findViewById(R.id.taskShareId);
        taskDone = findViewById(R.id.taskDoneId);
        doneTASK = findViewById(R.id.doneTaskTitle);

        title1 = findViewById(R.id.taskTitle);
        cover = findViewById(R.id.taskCover);
        tbod= findViewById(R.id.tbody);

        title1.setText(name);
        tbod.setText(taskBody1);

        //Update Views

        try {
            viewcount = Integer.parseInt(views) + 1;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.nwError) , Toast.LENGTH_LONG).show();
        }
        views = String.valueOf(viewcount);

        SendViewsToServer(views,String.valueOf(taskID));


        Glide.with(getApplicationContext()).load(date).into(cover);
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

                mp3Play.execute(musicURL); // direct link mp3 file

                //musicView.start();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


    }

    public void musicShare(View view) {

        view.startAnimation(buttonClick);

        view.startAnimation(buttonClick);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, " \n ---------------------------\n "+getString(R.string.subTaskShare)+"  https://play.google.com/store/apps/details?id=com.apps.amit.lawofattractionpro");
        try {
            startActivity(Intent.createChooser(share,getString(R.string.chooseToShare)));
            SendSharesToServer(shares, String.valueOf(taskID));

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();
        }

    }

    public void SendSharesToServer(final String Shares,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("shares", Shares));
                nameValuePairs.add(new BasicNameValuePair("id", id));


                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertMusicShares.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Shares, id);
    }

    public void SendViewsToServer(final String Views,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("views", Views));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertMusicViews.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Views, id);
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
