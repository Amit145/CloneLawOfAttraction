package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class iquote extends RecyclerView.Adapter<iquote.ViewHolder> {




  private Context context;
  private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
  private List<com.apps.amit.lawofattraction.DBUtils2> personUtils;

  public iquote(Context context, List personUtils) {
    this.context = context;
    this.personUtils = personUtils;
  }

  @Override
  public iquote.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item2, parent, false);
    iquote.ViewHolder viewHolder = new iquote.ViewHolder(v);
    return viewHolder;
  }



  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    holder.itemView.setTag(personUtils.get(position));

    DBUtils2 pu = personUtils.get(position);

    holder.pName.setText(" \" "+pu.getPersonFirstName()+" \" ");
    holder.pJobProfile.setText(" ");
    holder.pTime.setText("SHARE ");
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

          DBUtils2 cpu = (DBUtils2) view.getTag();



          view.startAnimation(buttonClick);
          Intent share = new Intent(Intent.ACTION_SEND);
          share.setType("text/plain");
          //share.setPackage("com.whatsapp");
          share.putExtra(Intent.EXTRA_TEXT, " \" "+cpu.getPersonFirstName()+" \" "+"\n ---------------------------\n Get more inspirational Quotes on Law of attraction daily app, Download Now From Play Store "+" https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
          try {
            context.startActivity(Intent.createChooser(share,"Choose Where to share"));

          } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(this, "Error".toString(), Toast.LENGTH_SHORT);
          }

          // Toast.makeText(view.getContext(), "shared"+cpu.getPersonFirstName()+"'s Manifestation ", Toast.LENGTH_SHORT).show();

        }
      });




    }
  }
}
