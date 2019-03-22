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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.client.MediaBrowserHelper;
import com.apps.amit.lawofattraction.service.MusicService;
import com.apps.amit.lawofattraction.service.contentcatalogs.MusicLibrary;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mCoverPic;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mMediaControlsImage;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mIsPlaying) {
            Toast.makeText(this, "playing", Toast.LENGTH_SHORT).show();
            MusicLibrary.clearList();

        }
        else {

            Toast.makeText(this, "Not playing", Toast.LENGTH_SHORT).show();
        }


    }

    private MediaSeekBar mSeekBarAudio;

    private MediaBrowserHelper mMediaBrowserHelper;

    public static String musicID = "";
    public static String musicTitle = "";
    public static String musicBody = "";
    public static String musicImageURL = "";
    public static String musicURL = "";
    public static int musicViews ;
    public static int musicShares ;


    private boolean mIsPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleTextView = findViewById(R.id.song_title);
        mArtistTextView = findViewById(R.id.song_artist);
        mCoverPic = findViewById(R.id.imageView8);
        mMediaControlsImage = findViewById(R.id.media_controls);
        mSeekBarAudio = findViewById(R.id.seekbar_audio);

        final ClickListener clickListener = new ClickListener();
        //findViewById(R.id.button_previous).setOnClickListener(clickListener);
        findViewById(R.id.button_play).setOnClickListener(clickListener);
       // findViewById(R.id.button_next).setOnClickListener(clickListener);

        mMediaBrowserHelper = new MediaBrowserConnection(this);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());

        Intent result = getIntent();

        if (mIsPlaying) {

            mMediaBrowserHelper.getTransportControls().pause();
            mMediaControlsImage.setImageResource(R.drawable.ic_media_with_play);

        }

        if(result.getExtras()!=null) {

            musicTitle = result.getExtras().getString("taskTitle");    //Title of the Music
            musicBody = result.getExtras().getString("taskSubtitle");    //Body of the Music
            musicURL = result.getExtras().getString("takImg");     //url of music
            musicID  = result.getExtras().getString("taskID");        //id of music
            musicImageURL  = result.getExtras().getString("taskLikes");     // image url
            musicShares =  result.getExtras().getInt("taskShares");      //share count
            musicViews =  result.getExtras().getInt("taskViews");       //view count

            MusicLibrary.addToList();


        }

        Glide.with(getApplicationContext()).load(musicImageURL).thumbnail(0.1f).into(mCoverPic);


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
                    } else {
                        mMediaBrowserHelper.getTransportControls().play();
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
            mMediaControlsImage.setPressed(mIsPlaying);
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
