package com.apps.amit.lawofattraction.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apps.amit.lawofattraction.MyStoryReplyActivity;
import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.utils.MyStoryUtils;
import java.util.List;
public class MyStoryAdapter extends RecyclerView.Adapter<MyStoryAdapter.ViewHolder> {




    Context context;
    private List<MyStoryUtils> personUtils;

    public MyStoryAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public MyStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item3, parent, false);
        return new MyStoryAdapter.ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(personUtils.get(position));

        MyStoryUtils pu = personUtils.get(position);

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



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyStoryUtils cpu = (MyStoryUtils) view.getTag();


                    Intent i = new Intent (view.getContext(), MyStoryReplyActivity.class);
                    i.putExtra("NameKey", cpu.getPersonFirstName());
                    i.putExtra("StoryKey",cpu.getPersonLastName());
                    i.putExtra("DateKey",cpu.getJobProfile());
                    i.putExtra("idKey",cpu.getcount());
                    i.putExtra("Utoken",cpu.gettoken());
                    view.getContext().startActivity(i);


                }
            });





        }
    }
}
