package com.apps.amit.lawofattraction;

/**
 * Created by amit on 17/12/17.
 */


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyCustomPagerAdapter extends PagerAdapter{
  private Context context;
  int images[];
  LayoutInflater layoutInflater;


  public MyCustomPagerAdapter(Context context, int images[]) {
    this.context = context;
    this.images = images;
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return images.length;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == ((LinearLayout) object);
  }

  @Override
  public Object instantiateItem(ViewGroup container, final int position) {
    View itemView = layoutInflater.inflate(R.layout.item, container,false);

    try {
      ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
      imageView.setImageResource(images[position]);

      container.addView(itemView);


      // listening to image click
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if((position + 1) == 1)
          {
            Intent art1 = new Intent(context,storyList.class);
            context.startActivity(art1);
          }
          else if((position + 1) == 2)
          {
            Intent art1 = new Intent(context,Task.class);
            context.startActivity(art1);

          }
          else if((position + 1) == 3)
          {
            Intent art1 = new Intent(context,comments.class);
            context.startActivity(art1);

          }
          else if((position + 1) == 4)
          {

            Intent art1 = new Intent(context, MusicList.class);
            context.startActivity(art1);

          }

          else if((position + 1) == 5)
          {

            Intent art1 = new Intent(context, Quotes.class);
            context.startActivity(art1);
          }

          else if((position + 1) == 6)
          {

            Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattractionpro");
            Intent rate = new Intent(Intent.ACTION_VIEW,uri);
            try {
              context.startActivity(rate);

            } catch (android.content.ActivityNotFoundException ex) {
              //Toast.makeText(this,"Error".toString(),Toast.LENGTH_SHORT);
            }

          }

          // else if((position + 1) == 6)
          //{
          //  Intent art1 = new Intent(context,comments.class);
          //context.startActivity(art1);
          // }

          // Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
        }
      });
    } catch (OutOfMemoryError error)
    {
      System.gc();
    }

    return itemView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((LinearLayout) object);
    object = null;
  }
}
