package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;

import java.util.List;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by amit on 10/2/18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Contact> listItems;
    private Context context;
    String date;

    public RecyclerAdapter(List<Contact> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.dates_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = context.getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value = pref.getString("language","en");

        context = LocaleHelper.setLocale(context, value);
        final Resources resources = context.getResources();

        Contact obj = listItems.get(position);
        date = resources.getString(R.string.activityTracker_text4);
        holder.txt1.setText(resources.getString(R.string.activityTracker_text5)+" " +obj.getName());
        holder.txt2.setText(resources.getString(R.string.activity6_text3)+" "+String.valueOf(obj.getPhoneNumber())+" "+resources.getString(R.string.seconds_text));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,date,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1;
        public  TextView txt2;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            txt1 = (TextView) itemView.findViewById(R.id.txt1);
            txt2 = (TextView) itemView.findViewById(R.id.txt2);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
