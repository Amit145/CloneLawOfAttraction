package com.apps.amit.lawofattraction.adapters;

/*
 * Created by amit on 17/12/17.
 */


import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.amit.lawofattraction.AffirmationHome;
import com.apps.amit.lawofattraction.CommentsActivity;
import com.apps.amit.lawofattraction.MusicListActivity;
import com.apps.amit.lawofattraction.DailyQuotesActivity;
import com.apps.amit.lawofattraction.R;
import com.apps.amit.lawofattraction.SayThankYouActivity;
import com.apps.amit.lawofattraction.StoryListActivity;

public class MyCustomPagerAdapter extends PagerAdapter{
  private Context context;
  int [] images;
  LayoutInflater layoutInflater;


  public MyCustomPagerAdapter(Context context, int [] images) {
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
            Intent art1 = new Intent(context, AffirmationHome.class);
            context.startActivity(art1);


          }
          else if((position + 1) == 2)
          {
            Intent art1 = new Intent(context, SayThankYouActivity.class);
            context.startActivity(art1);


          }
          else if((position + 1) == 3)
          {
            Intent art1 = new Intent(context, StoryListActivity.class);
            context.startActivity(art1);


          }
          else if((position + 1) == 4)
          {
            Intent art1 = new Intent(context, CommentsActivity.class);
            context.startActivity(art1);


          }

          else if((position + 1) == 5)
          {
            Intent art1 = new Intent(context, MusicListActivity.class);
            context.startActivity(art1);

          }

          else if((position + 1) == 6)
          {
            Intent art1 = new Intent(context, DailyQuotesActivity.class);
            context.startActivity(art1);

          }

        }
      });
    } catch (OutOfMemoryError error)
    {
      Log.e(error.getMessage(),""+error.getMessage());
    }

    return itemView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((LinearLayout) object);
  }
}
