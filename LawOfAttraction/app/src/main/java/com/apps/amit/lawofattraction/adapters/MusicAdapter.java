package com.apps.amit.lawofattraction.adapters;

/*
 * Created by amit on 11/3/18.
 */

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.amit.lawofattraction.MusicPlayActivity;
import com.apps.amit.lawofattraction.utils.MusicUtils;
import com.apps.amit.lawofattraction.R;
import com.bumptech.glide.Glide;
import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context context;
    private List<MusicUtils> personUtils;


   public MusicAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list, parent, false);
        context = parent.getContext();
        ViewHolder viewHolder ;
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));
        try {
            MusicUtils pu = personUtils.get(position);
            holder.pName.setText(String.valueOf(pu.getPersonFirstName()+":"));
            holder.pJobProfile.setText(String.valueOf("\t\t\t"+"\""+pu.getPersonLastName()+"\""));
            holder.ptaskSharestxt.setText(String.valueOf(" :\t "+pu.getShares()));
            holder.ptaskViewstxt.setText(String.valueOf(" :\t "+pu.getViews()));

            Glide.with(context).load(pu.getJobProfile()).thumbnail(0.1f).fitCenter().into(holder.img);
            Glide.with(context).load(R.drawable.viewicon).thumbnail(0.1f).fitCenter().into(holder.img1);
            Glide.with(context).load(R.drawable.shareicon).thumbnail(0.1f).fitCenter().into(holder.img3);

        }
        catch (Exception e) {

            Log.e(e.getMessage(),e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView pName;
        private ImageView img1;
        private ImageView img3;
        private TextView pJobProfile;
        private TextView ptaskSharestxt,ptaskViewstxt;
        private ImageView img;

        private ViewHolder(View itemView) {
            super(itemView);

            img1 =  itemView.findViewById(R.id.viewIconID);
            img3 =  itemView.findViewById(R.id.shareIconID);

            pName =  itemView.findViewById(R.id.pNametxt);
            pJobProfile =  itemView.findViewById(R.id.pJobProfiletxt);
            ptaskSharestxt =  itemView.findViewById(R.id.taskSharestxt);
            ptaskViewstxt =  itemView.findViewById(R.id.taskViewstxt);
            img =  itemView.findViewById(R.id.taskImg);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MusicUtils cpu = (MusicUtils) view.getTag();

                    Intent i = new Intent (view.getContext(), MusicPlayActivity.class);
                    i.putExtra("taskTitle", cpu.getPersonFirstName());       //name
                    i.putExtra("taskSubtitle",cpu.getPersonLastName());     //body
                    i.putExtra("takImg",cpu.getimgUrl());                   //web url  music
                    i.putExtra("taskID",cpu.getid());                       //id
                    i.putExtra("taskBody",cpu.getbody());                   //body
                    i.putExtra("taskLikes",cpu.getJobProfile());       //imgurl    pic
                    i.putExtra("taskShares",cpu.getShares());           //shares
                    i.putExtra("taskViews",cpu.getViews());             //views
                    i.putExtra("musicDuration",cpu.getDuration());
                    view.getContext().startActivity(i);

                }
            });

        }
    }

}
