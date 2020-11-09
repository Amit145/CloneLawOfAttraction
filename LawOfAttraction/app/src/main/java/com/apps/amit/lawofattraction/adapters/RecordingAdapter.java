package com.apps.amit.lawofattraction.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.RecordingListActivity;
import com.apps.amit.lawofattraction.utils.Recording;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Manish on 10/8/2017.
 */

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder>{

    private ArrayList<Recording> recordingArrayList;
    private Context context;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int last_index = -1;

    public RecordingAdapter(Context context, ArrayList<Recording> recordingArrayList){
        this.context = context;
        this.recordingArrayList = recordingArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recording_item_layout,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        setUpData(holder,position);
    }

    @Override
    public int getItemCount() {
        return recordingArrayList.size();
    }


    private void setUpData(ViewHolder holder, int position) {

        Recording recording = recordingArrayList.get(position);
        holder.textViewName.setText(recording.getFileName());
        holder.textDate.setText(recording.getFileCreationDate());
        holder.audioDuration.setText(recording.getFileDurationText());

        if( recording.isPlaying() ){

            holder.imageViewPlay.setImageResource(R.drawable.ic_pause);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            }
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.seekUpdation(holder);

        }else{

            holder.imageViewPlay.setImageResource(R.drawable.ic_play);
            holder.audioDuration.setText(recording.getFileDurationText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            }
            holder.seekBar.setVisibility(View.GONE);
        }
        holder.manageSeekBar(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPlay, deleteButtonImageView;
        SeekBar seekBar;
        TextView textViewName;
        TextView textDate,audioDuration;
        private String recordingUri;
        private int lastProgress = 0;
        private Handler mHandler = new Handler();
        ViewHolder holder;


        public ViewHolder(View itemView) {
            super(itemView);

            imageViewPlay = itemView.findViewById(R.id.imageViewPlay);
            deleteButtonImageView = itemView.findViewById(R.id.deleteButtonImageView);
            seekBar = itemView.findViewById(R.id.seekBar);
            textViewName = itemView.findViewById(R.id.textViewRecordingname);
            textDate = itemView.findViewById(R.id.recordingDate);
            audioDuration = itemView.findViewById(R.id.audioDuration);

            imageViewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Recording recording = recordingArrayList.get(position);

                    recordingUri = recording.getUri();

                    if( isPlaying ){
                        stopPlaying();
                        if( position == last_index ){
                            recording.setPlaying(false);
                            stopPlaying();
                            notifyItemChanged(position);

                        }else{
                            markAllPaused();
                            recording.setPlaying(true);
                            notifyItemChanged(position);
                            startPlaying(recording,position);
                            last_index = position;
                        }

                    }else {
                        startPlaying(recording,position);
                        recording.setPlaying(true);
                        seekBar.setMax(mPlayer.getDuration());
                        Log.d("isPlayin","False");
                        notifyItemChanged(position);
                        last_index = position;
                    }

                }

            });

            deleteButtonImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int position = getAdapterPosition();
                    Recording recording = recordingArrayList.get(position);
                    recordingUri = recording.getUri();


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setMessage("Do you want to delete ?");
                    builder.setPositiveButton(context.getString(R.string.Yes_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if user pressed "yes", then he is allowed to exit from application

                            File fdelete = new File(recordingUri);
                            Intent art1 = new Intent(context, RecordingListActivity.class);
                            art1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



                            if( isPlaying ){
                                stopPlaying();
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        notifyDataSetChanged();
                                        context.startActivity(art1);

                                    }
                                }

                            }else {

                                //delete recording
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        notifyDataSetChanged();
                                        context.startActivity(art1);

                                    }
                                }
                            }

                        }
                    });
                    builder.setNegativeButton(context.getString(R.string.No_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if user select "No", just cancel this dialog and continue with app
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            });
        }

        public void manageSeekBar(ViewHolder holder){
            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if( mPlayer!=null && fromUser ){
                        mPlayer.seekTo(progress);
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

        private void markAllPaused() {
            for( int i=0; i < recordingArrayList.size(); i++ ){
                recordingArrayList.get(i).setPlaying(false);
                recordingArrayList.set(i,recordingArrayList.get(i));
            }
            notifyDataSetChanged();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seekUpdation(holder);
            }
        };

        private void seekUpdation(ViewHolder holder) {
            this.holder = holder;
            if(mPlayer != null){
                int mCurrentPosition = mPlayer.getCurrentPosition() ;
                holder.seekBar.setMax(mPlayer.getDuration());
                holder.seekBar.setProgress(mCurrentPosition);
                lastProgress = mCurrentPosition;

            }
            mHandler.postDelayed(runnable, 100);
        }

        private void stopPlaying() {
            try{
                mPlayer.release();
            }catch (Exception e){
                e.printStackTrace();
            }
            mPlayer = null;
            isPlaying = false;
        }

        private void startPlaying(final Recording audio, final int position) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(recordingUri);
                mPlayer.prepare();
                mPlayer.start();

            } catch (IOException e) {
                Log.e("LOG_TAG", "prepare() failed");
            }
            //showing the pause button
            seekBar.setMax(mPlayer.getDuration());
            isPlaying = true;

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audio.setPlaying(false);
                    notifyItemChanged(position);
                }
            });
        }

    }
}