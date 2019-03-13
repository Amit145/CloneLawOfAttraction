package com.apps.amit.lawofattraction;

/*
 * Created by amit on 11/3/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context context;
    private List<storyUtils> personUtils;

    public StoryAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item6, parent, false);
        context = parent.getContext();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));
        try {
            storyUtils pu = personUtils.get(position);


            holder.pName.setText(String.valueOf(pu.getPersonFirstName()+":"));
            holder.pJobProfile.setText(String.valueOf("\t\t\t"+"\""+pu.getPersonLastName()+"\""));
            holder.ptaskSharestxt.setText(String.valueOf(" :\t "+pu.getShares()));
            holder.ptaskViewstxt.setText(String.valueOf(" :\t "+pu.getViews()));

            Glide.with(context).load(pu.getimgUrl()).thumbnail(0.1f).fitCenter().into(holder.img);
            Glide.with(context).load(R.drawable.viewicon).thumbnail(0.1f).fitCenter().into(holder.img1);
            Glide.with(context).load(R.drawable.shareicon).thumbnail(0.1f).fitCenter().into(holder.img2);

        }
        catch (Exception e) {

            System.gc();
        }

    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView pName;
         TextView pJobProfile;
         TextView ptaskSharestxt,ptaskViewstxt;
         ImageView img,img1,img2;

        public ViewHolder(View itemView) {
            super(itemView);

            pName =  itemView.findViewById(R.id.pNametxt);
            pJobProfile =  itemView.findViewById(R.id.pJobProfiletxt);
            ptaskSharestxt =  itemView.findViewById(R.id.taskSharestxt);
            ptaskViewstxt =  itemView.findViewById(R.id.taskViewstxt);
            img =  itemView.findViewById(R.id.taskImg);
            img1 =  itemView.findViewById(R.id.imgViews);
            img2=  itemView.findViewById(R.id.imgShares);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    storyUtils cpu = (storyUtils) view.getTag();

                    Intent i = new Intent (view.getContext(), Story.class);
                    i.putExtra("storyTitle", cpu.getPersonFirstName());
                    i.putExtra("storySubtitle",cpu.getPersonLastName());
                    i.putExtra("storyImg",cpu.getimgUrl());
                    i.putExtra("storyID",cpu.getid());
                    i.putExtra("storyBody",cpu.getbody());
                    i.putExtra("storyLikes",cpu.getJobProfile());
                    i.putExtra("storyShares",cpu.getShares());
                    i.putExtra("storyViews",cpu.getViews());
                    i.putExtra("storyViews",cpu.getViews());
                    i.putExtra("storyLink",cpu.getstoryLink());

                    view.getContext().startActivity(i);

                }
            });

        }
    }

}
