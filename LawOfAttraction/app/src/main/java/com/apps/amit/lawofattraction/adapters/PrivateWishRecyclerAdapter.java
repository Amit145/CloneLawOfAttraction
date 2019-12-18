package com.apps.amit.lawofattraction.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;

import java.util.List;

public class PrivateWishRecyclerAdapter extends RecyclerView.Adapter<PrivateWishRecyclerAdapter.ViewHolder> {

    List<PrivateWishesUtils> listItems;
    Context context;
    String date;

    public PrivateWishRecyclerAdapter(List<PrivateWishesUtils> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.private_wish_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PrivateWishesUtils pu = listItems.get(position);

        holder.pName.setText(String.valueOf(pu.getUserName()+":"));
        holder.pJobProfile.setText(String.valueOf("\t\t\t"+"\""+pu.getUserWish()+"\""));
        holder.pTime.setText(String.valueOf(" - "+pu.getUserDate()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pName;
        TextView pTime;
        TextView pJobProfile;

        public ViewHolder(View itemView) {
            super(itemView);

            pName =  itemView.findViewById(R.id.pNametxt);
            pJobProfile =  itemView.findViewById(R.id.pJobProfiletxt);
            pTime =  itemView.findViewById(R.id.ptime);
        }
    }
}
