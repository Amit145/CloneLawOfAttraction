/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.client.MediaBrowserHelper;
import com.apps.amit.lawofattraction.service.MusicService;
import com.apps.amit.lawofattraction.service.contentcatalogs.MusicLibrary;
import com.apps.amit.lawofattraction.service.players.MediaPlayerAdapter;
import com.bumptech.glide.Glide;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.apps.amit.lawofattraction.SubTask.UTF_ENCODING;

public class MainActivity extends AppCompatActivity {

    private ImageView mCoverPic;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
   // private ImageView mMediaControlsImage;
    private ImageView shareMusicImage;
    private ImageButton imgButton;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mIsPlaying) {
           // Toast.makeText(this, "playing", Toast.LENGTH_SHORT).show();
            MusicLibrary.clearList();

        }
    }

    private MediaSeekBar mSeekBarAudio;

    private MediaBrowserHelper mMediaBrowserHelper;

    private int musicID ;
    public static String musicTitle = "";
    public static String musicBody = "";
    public static String musicImageURL = "";
    public static String musicURL = "";
    public String musicViews ;
    public String musicShares ;
    public static int musicDuration ;
    int userViews;
    private int viewcount;
    private int sharecount;


    private boolean mIsPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleTextView = findViewById(R.id.song_title);
        mArtistTextView = findViewById(R.id.song_artist);
        mCoverPic = findViewById(R.id.imageView8);
        //mMediaControlsImage = findViewById(R.id.media_controls);
        mSeekBarAudio = findViewById(R.id.seekbar_audio);

        shareMusicImage = findViewById(R.id.taskShareId);

        final ClickListener clickListener = new ClickListener();
        //findViewById(R.id.button_previous).setOnClickListener(clickListener);
        imgButton = findViewById(R.id.button_play);

        imgButton.setOnClickListener(clickListener);
       // findViewById(R.id.button_next).setOnClickListener(clickListener);

        mMediaBrowserHelper = new MediaBrowserConnection(this);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());

        Intent result = getIntent();

        if (mIsPlaying) {

            mMediaBrowserHelper.getTransportControls().pause();
           // mMediaControlsImage.setImageResource(R.drawable.ic_media_with_play);
            //imgButton.setImageResource(R.drawable.ic_play);

        }


        if(result.getExtras()!=null) {

            musicTitle = result.getExtras().getString("taskTitle");    //Title of the Music
            musicBody = result.getExtras().getString("taskSubtitle");    //Body of the Music
            musicURL = result.getExtras().getString("takImg");     //url of music
            musicID  = result.getExtras().getInt("taskID");        //id of music
            musicImageURL  = result.getExtras().getString("taskLikes");     // image url
            musicShares =  result.getExtras().getString("taskShares");      //share count
            musicViews =  result.getExtras().getString("taskViews");       //view count
            musicDuration =  result.getExtras().getInt("musicDuration");

            MusicLibrary.addToList();


        }

        Glide.with(getApplicationContext()).load(musicImageURL).thumbnail(0.1f).into(mCoverPic);

        //Send Views To Server

        userViews = Integer.parseInt(result.getExtras().getString("taskViews"));

        try {
            viewcount = Integer.parseInt(musicViews) + 1;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.nwError) , Toast.LENGTH_LONG).show();
        }
        musicViews = String.valueOf(viewcount);

        Toast.makeText(this, "Views : "+musicViews+" Task ID "+musicID, Toast.LENGTH_SHORT).show();

        SendViewsToServer(musicViews,String.valueOf(musicID));

        shareMusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);



                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                //share.setPackage("com.whatsapp");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.storyShare)+"\n-------------------------\nhttps://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                try {
                    startActivity(Intent.createChooser(share, getString(R.string.chooseToShare)));

                    try {
                        sharecount = Integer.parseInt(musicShares);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.nwError) , Toast.LENGTH_LONG).show();
                    }
                    musicShares = String.valueOf(sharecount);

                    SendSharesToServer(musicShares,String.valueOf(musicID));


                } catch (android.content.ActivityNotFoundException ex) {
                    //Toast.makeText(this, "Error".toString(), Toast.LENGTH_SHORT);
                }





            }
        });


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

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSeekBarAudio.disconnectController();
        mMediaBrowserHelper.onStop();
    }

    /**
     * Convenience class to collect the click listeners together.
     * <p>
     * In a larger app it's better to split the listeners out or to use your favorite
     * library.
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
               // case R.id.button_previous:
                 //   mMediaBrowserHelper.getTransportControls().skipToPrevious();
                  //  break;
                case R.id.button_play:
                    if (mIsPlaying) {
                        mMediaBrowserHelper.getTransportControls().pause();
                        imgButton.setImageResource(R.drawable.ic_play);
                        Toast.makeText(MainActivity.this, "Media Paused", Toast.LENGTH_SHORT).show();
                    } else {
                        mMediaBrowserHelper.getTransportControls().play();
                        imgButton.setImageResource(R.drawable.ic_pause);
                        Toast.makeText(MainActivity.this, "Media Playing", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //case R.id.button_next:
                //    mMediaBrowserHelper.getTransportControls().skipToNext();
                 //   break;
            }
        }
    }

    /**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     */
    private class MediaBrowserConnection extends MediaBrowserHelper {
        private MediaBrowserConnection(Context context) {
            super(context, MusicService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
            mSeekBarAudio.setMediaController(mediaController);
        }

        @Override
        protected void onChildrenLoaded(@NonNull String parentId,
                                        @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            final MediaControllerCompat mediaController = getMediaController();

            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.addQueueItem(mediaItem.getDescription());
            }

            // Call prepare now so pressing play just works.
            mediaController.getTransportControls().prepare();
        }
    }

    /**
     * Implementation of the {@link MediaControllerCompat.Callback} methods we're interested in.
     * <p>
     * Here would also be where one could override
     * {@code onQueueChanged(List<MediaSessionCompat.QueueItem> queue)} to get informed when items
     * are added or removed from the queue. We don't do this here in order to keep the UI
     * simple.
     */
    private class MediaBrowserListener extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null &&
                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
            //mMediaControlsImage.setPressed(mIsPlaying);
            imgButton.setPressed(mIsPlaying);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {
                return;
            }


            mTitleTextView.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            mArtistTextView.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            mArtistTextView.setMaxLines(10);
           // mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
            //        MainActivity.this,
              //      mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
        }
    }
}
