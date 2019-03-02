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


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<TaskUtils> personUtils;


     TaskAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item5, parent, false);
        context = parent.getContext();
        ViewHolder viewHolder ;
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));
        try {
            TaskUtils pu = personUtils.get(position);
            holder.pName.setText(String.valueOf(pu.getPersonFirstName()+":"));
            holder.pJobProfile.setText(String.valueOf("\t\t\t"+"\""+pu.getPersonLastName()+"\""));
            holder.pTime.setText(String.valueOf(" :\t "+pu.getJobProfile()));
            holder.ptaskSharestxt.setText(String.valueOf(" :\t "+pu.getShares()));
            holder.ptaskViewstxt.setText(String.valueOf(" :\t "+pu.getViews()));

            Glide.with(context).load(pu.getimgUrl()).thumbnail(0.1f).fitCenter().into(holder.img);

            Glide.with(context).load(R.drawable.viewicon).thumbnail(0.1f).fitCenter().into(holder.img1);
            Glide.with(context).load(R.drawable.hearticon).thumbnail(0.1f).fitCenter().into(holder.img2);
            Glide.with(context).load(R.drawable.shareicon).thumbnail(0.1f).fitCenter().into(holder.img3);


           // Picasso.with(context).load(pu.getimgUrl()).resize(300,200).into(holder.img);
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

        private TextView pName;
        private ImageView img1;
        private ImageView img2;
        private ImageView img3;
        private TextView pTime;
        private TextView pJobProfile;
        private TextView ptaskSharestxt,ptaskViewstxt;
        public ImageView img;
        //public int taskID;

        private ViewHolder(View itemView) {
            super(itemView);

            img1 =  itemView.findViewById(R.id.viewIconID);
            img2 =  itemView.findViewById(R.id.heartIconID);
            img3 =  itemView.findViewById(R.id.shareIconID);

            pName =  itemView.findViewById(R.id.pNametxt);
            pJobProfile =  itemView.findViewById(R.id.pJobProfiletxt);
            pTime =  itemView.findViewById(R.id.ptime);
            ptaskSharestxt =  itemView.findViewById(R.id.taskSharestxt);
            ptaskViewstxt =  itemView.findViewById(R.id.taskViewstxt);
            img =  itemView.findViewById(R.id.taskImg);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TaskUtils cpu = (TaskUtils) view.getTag();

                    Intent i = new Intent (view.getContext(), SubTask.class);
                    i.putExtra("taskTitle", cpu.getPersonFirstName());
                    i.putExtra("taskSubtitle",cpu.getPersonLastName());
                    i.putExtra("takImg",cpu.getimgUrl());
                    i.putExtra("taskID",cpu.getid());
                    i.putExtra("taskBody",cpu.getbody());
                    i.putExtra("taskLikes",cpu.getJobProfile());
                    i.putExtra("taskShares",cpu.getShares());
                    i.putExtra("taskViews",cpu.getViews());
                    //i.putExtra("idKey",cpu.getcount());
                    view.getContext().startActivity(i);

                    //Toast.makeText(view.getContext(), "You "+cpu.getPersonFirstName()+"'s Manifestation ", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

}
