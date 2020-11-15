package com.apps.amit.lawofattraction.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.utils.ViewAllQuotesUtils;

import java.util.List;

public class AllQuotesAdapter extends RecyclerView.Adapter<AllQuotesAdapter.ViewHolder> {




  private Context context;
  private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
  private List<ViewAllQuotesUtils> personUtils;

  public AllQuotesAdapter(Context context, List<ViewAllQuotesUtils> personUtils) {
    this.context = context;
    this.personUtils = personUtils;
  }

  @Override
  public AllQuotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item2, parent, false);
    return new AllQuotesAdapter.ViewHolder(v);
  }



  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    holder.itemView.setTag(personUtils.get(position));

    ViewAllQuotesUtils pu = personUtils.get(position);

    holder.pName.setText(" \" "+pu.getPersonFirstName()+" \" ");
    holder.pJobProfile.setText(" ");
    holder.pTime.setText("SHARE ");

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

          ViewAllQuotesUtils cpu = (ViewAllQuotesUtils) view.getTag();



          view.startAnimation(buttonClick);
          Intent share = new Intent(Intent.ACTION_SEND);
          share.setType("text/plain");
          share.putExtra(Intent.EXTRA_TEXT, " \" "+cpu.getPersonFirstName()+" \" "+"\n ---------------------------\n Get more inspirational DailyQuotesActivity on Law of attraction daily app, Download Now From Play Store "+" https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
          try {
            context.startActivity(Intent.createChooser(share,"Choose Where to share"));

          } catch (Exception e) {

            Log.e(e.getMessage(),e.getMessage());
          }

        }
      });




    }
  }
}
