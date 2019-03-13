package com.apps.amit.lawofattraction;

/*
 * Created by amit on 11/3/18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<PersonUtils> personUtils;

    public CommentsRecyclerAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        PersonUtils pu = personUtils.get(position);

        holder.pName.setText(String.valueOf(pu.getPersonFirstName()+":"));
        holder.pJobProfile.setText(String.valueOf("\t\t\t"+"\""+pu.getPersonLastName()+"\""));
        holder.pTime.setText(String.valueOf(" - "+pu.getJobProfile()));

       // Glide.with(context).load()



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

            pName =  itemView.findViewById(R.id.pNametxt);
            pJobProfile =  itemView.findViewById(R.id.pJobProfiletxt);
            pTime =  itemView.findViewById(R.id.ptime);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences pref = context.getSharedPreferences("UserLang",MODE_PRIVATE);

                    //Store selected language in a Variable called value
                    String value = pref.getString("language","en");

                    context = LocaleHelper.setLocale(context, value);
                    Resources resources = context.getResources();

                    PersonUtils cpu = (PersonUtils) view.getTag();

                    Toast.makeText(view.getContext(), resources.getString(R.string.Home_pray)+cpu.getPersonFirstName(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

}