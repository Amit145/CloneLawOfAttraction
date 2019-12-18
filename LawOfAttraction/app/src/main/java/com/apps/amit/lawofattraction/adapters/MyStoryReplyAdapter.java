package com.apps.amit.lawofattraction.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.utils.MyStoryReplyUtils;

import java.util.List;

public class MyStoryReplyAdapter extends RecyclerView.Adapter<MyStoryReplyAdapter.ViewHolder> {




    Context context;
    private List<MyStoryReplyUtils> personUtils;


    public MyStoryReplyAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public MyStoryReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item4, parent, false);
        return new MyStoryReplyAdapter.ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(personUtils.get(position));

        MyStoryReplyUtils pu = personUtils.get(position);

        holder.pName.setText(pu.getPersonFirstName());
        holder.pJobProfile.setText(" \" "+pu.getPersonLastName()+" \" ");
        holder.pTime.setText(pu.getJobProfile());

    }



    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView pName;
         TextView pTime;
         TextView pJobProfile;


        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.pNametxt);
            pJobProfile = (TextView) itemView.findViewById(R.id.pJobProfiletxt);
            pTime = (TextView) itemView.findViewById(R.id.pJobProfiletxt1);







        }
    }
}
