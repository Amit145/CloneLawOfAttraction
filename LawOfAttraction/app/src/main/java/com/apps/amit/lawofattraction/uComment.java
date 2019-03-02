package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.util.List;

public class uComment extends RecyclerView.Adapter<uComment.ViewHolder> {




    private Context context;
    private List<DBUtils4> personUtils;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    public uComment(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public uComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item4, parent, false);
        uComment.ViewHolder viewHolder = new uComment.ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(personUtils.get(position));

        DBUtils4 pu = personUtils.get(position);

        holder.pName.setText(pu.getPersonFirstName());
        holder.pJobProfile.setText(" \" "+pu.getPersonLastName()+" \" ");
        holder.pTime.setText(pu.getJobProfile());
        //holder.pTime.setText("SHARES: "+pu.getcount());

    }



    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pName;
        public TextView pTime;
        public TextView pJobProfile;


        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pJobProfile = (TextView) itemView.findViewById(R.id.pJobProfiletxt);
            pTime = (TextView) itemView.findViewById(R.id.pJobProfiletxt1);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // Toast.makeText(view.getContext(), "shared"+cpu.getPersonFirstName()+"'s Manifestation ", Toast.LENGTH_SHORT).show();

                }
            });





        }
    }
}
