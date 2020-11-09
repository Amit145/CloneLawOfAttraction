package com.apps.amit.lawofattraction.utils;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Recording {

    String Uri, fileName;
    boolean isPlaying = false;


    public Recording(String uri, String fileName, boolean isPlaying) {
        Uri = uri;
        this.fileName = fileName;
        this.isPlaying = isPlaying;
    }

    public String getUri() {
        return Uri;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileCreationDate() {
        File file = new File(Uri);
        Date lastModDate = new Date(file.lastModified());
        String dateString = DateFormat.getDateTimeInstance().format(lastModDate);
        return dateString;
    }

    public String getFileDurationText() {
        File file = new File(Uri);
        if(file.exists()){
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
            String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(durationStr));
            long seconds = TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(durationStr));
            return String.format("%d:%02d", minutes, seconds);
        }

        return null;
    }

    public long getFileDurationLong() {
        File file = new File(Uri);
        if(file!=null) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
            String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return Long.parseLong(durationStr);
        }

        return 0;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing){
        this.isPlaying = playing;
    }
}