package com.apps.amit.lawofattraction;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.ButterKnife;



public class Home extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ViewPager viewPager;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    LinearLayout linearBanner;
    TextView homeSubTitle;
    TextView homeMyStories;
    TextView homeSayToUniverse;
    TextView homeSettings;
    TextView homeGetProBannerText;
    AlertDialog alert;
    Button button1;
    DrawerLayout drawer;
    NavigationView navigationView;
    Timer timer;
    int [] images = new int[6];
    MyCustomPagerAdapter myCustomPagerAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        img1=null;
        img2=null;
        img3=null;
        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        timer.purge();
        ActivityCompat.finishAffinity(Home.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView nameText;
        ButterKnife.bind(this);

        images[0] = R.drawable.p7;
        images[1] = R.drawable.p6;
        images[2] = R.drawable.p4;
        images[3] = R.drawable.p1;
        images[4] = R.drawable.p2;
        images[5] = R.drawable.p3;

        setTitle(getString(R.string.Home_title));
        setContentView(R.layout.activity_home);

	    FirebaseMessaging.getInstance().subscribeToTopic("FREE");


        SharedPreferences nameSp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        String naam = nameSp1.getString("userName","");

	    Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        nameText =  findViewById(R.id.textView3);
        homeSubTitle=  findViewById(R.id.textView2);
        homeMyStories=  findViewById(R.id.textView4);
        homeSayToUniverse=  findViewById(R.id.textView5);
        homeSettings=  findViewById(R.id.textView6);
        homeGetProBannerText =  findViewById(R.id.prover);
        button1 =  findViewById(R.id.staButton);
        linearBanner = findViewById(R.id.linearBanner);

        //if ads enabled

        Calendar c1 = Calendar.getInstance();

        SharedPreferences pref = getSharedPreferences("AdvsPref",MODE_PRIVATE);

        Calendar dummyDate = Calendar.getInstance();

        dummyDate.set(Calendar.YEAR, 2011);

        //Store selected language in a Variable called value
        String value = pref.getString("adsDate",dummyDate.getTime().toString());

        if(!value.isEmpty())
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(value));// all done

            } catch (ParseException e) {

                e.printStackTrace();
            }

            if(cal.getTime().after(c1.getTime()))
            {
                //Home_setProBannerText
                homeGetProBannerText.setText(getString(R.string.Home_setProBannerText));
                linearBanner.setBackgroundColor(Color.parseColor("#229954"));
            }
            else {


                homeGetProBannerText.setText(getString(R.string.Home_getProBannerText));
            }

        }

        homeSubTitle.setText(getString(R.string.Home_subTitle));
        button1.setText(getString(R.string.Home_startButtonText));
        homeMyStories.setText(getString(R.string.Home_myStories));
        homeSayToUniverse.setText(getString(R.string.Home_sayToUniverse));
        homeSettings.setText(getString(R.string.Home_settings));


        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle navigation view item clicks here.
                        int id = item.getItemId();

                        if (id == R.id.nav_camera) {

                            Intent i = new Intent(getApplicationContext(), info.class);
                            startActivity(i);

                            // Handle the camera action
                        } else if (id == R.id.nav_gallery) {

                            Intent art1 = new Intent(getApplicationContext(),Lawtips.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_slideshow) {

                            Intent art1 = new Intent(getApplicationContext(), MusicList.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_manage) {

                            Intent art1 = new Intent(getApplicationContext(), Quotes.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_story) {

                            Intent art1 = new Intent(getApplicationContext(),storyList.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_us) {

                            Intent art1 = new Intent(getApplicationContext(), AboutApp.class);
                            startActivity(art1);

                        }  else if (id == R.id.nav_policy) {

                            Intent art1 = new Intent(getApplicationContext(), Policy.class);
                            startActivity(art1);

                        }else if (id == R.id.nav_task) {

                            Intent art1 = new Intent(getApplicationContext(),Task.class);
                            startActivity(art1);

                        }

                        else if (id == R.id.nav_share) {

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                            try {
                                startActivity(Intent.createChooser(share,getString(R.string.chooseToShare)));

                            } catch (android.content.ActivityNotFoundException ex) {

                                Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                            }

                        } else if (id == R.id.nav_send) {

                            Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattraction");
                            Intent rate = new Intent(Intent.ACTION_VIEW,uri);
                            try {
                                startActivity(rate);

                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                            }


                        } else if (id == R.id.nav_send1) {

                            Intent art1 = new Intent(getApplicationContext(),feedback.class);
                            startActivity(art1);
                        }

                        else if (id == R.id.action) {

                            Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattractionpro");
                            Intent rate = new Intent(Intent.ACTION_VIEW,uri);
                            try {
                                startActivity(rate);

                            } catch (android.content.ActivityNotFoundException ex) {

                                Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                            }

                        } else if (id == R.id.nav_calendar) {

                            Intent art1 = new Intent(getApplicationContext(),calendar.class);
                            startActivity(art1);
                        }

                        else if (id == R.id.nav_mail) {

                            SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
                            String naam = sp1.getString("userName","");

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:lawofattractiondaily@innovativelabs.xyz");
                            if(!naam.equalsIgnoreCase(""))
                            {
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Mail From \" " + naam + " \"");

                            } else {

                                intent.putExtra(Intent.EXTRA_SUBJECT, "New Mail");
                            }
                            intent.setData(data);
                            startActivity(intent);
                        }


                        drawer = findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }



                });



        if(!(naam.equalsIgnoreCase("")))
        {
            String [] name = naam.split(" ");
            String welcomeUserTxt = getString(R.string.Home_textBelowPager1)+" "+name[0]+getString(R.string.Home_textBelowPager2);
            nameText.setText(welcomeUserTxt);
        }
        else {

            nameText.setText(getString(R.string.Home_textBelowPager));
        }


        viewPager = findViewById(R.id.pager1);



        img1 = findViewById(R.id.imageView3);
        img2 = findViewById(R.id.imageView4);
        img3 = findViewById(R.id.imageView5);

        myCustomPagerAdapter = new MyCustomPagerAdapter(this, images);
        viewPager.setAdapter(myCustomPagerAdapter);

        //Timer for Pager

        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),3000,3000);

        Glide.with(getApplicationContext()).load(R.drawable.experience).thumbnail(0.1f).fitCenter().into(img1);

        Glide.with(getApplicationContext()).load(R.drawable.comments).thumbnail(0.1f).fitCenter().into(img2);

        Glide.with(getApplicationContext()).load(R.drawable.settings).thumbnail(0.1f).fitCenter().into(img3);


        if(isFirstTime())
        {
            //If First Time then do the following activity

            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            LayoutInflater inflater = Home.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.whatsnew, null);
            builder.setCancelable(false);
            builder.setMessage("What's New In v3.4");

            builder.setView(dialogView);
            alert = builder.create();
            alert.show();
            Button submit =  dialogView.findViewById(R.id.btnsubmit);
            Button cancel =  dialogView.findViewById(R.id.btncancel);
            ImageView img =  dialogView.findViewById(R.id.storyImage);

            Glide.with(getApplicationContext()).load(R.drawable.p1).thumbnail(0.1f).into(img);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);

                    Intent art1 = new Intent(getApplicationContext(),MusicList.class);
                    startActivity(art1);

                    alert.dismiss();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    alert.dismiss();
                }
            });

        }

        //button 1 on click
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(),Exercise1.class);
                startActivity(art1);

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(),experiences.class);
                startActivity(art1);

            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(),comments.class);
                startActivity(art1);

            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(), Settings.class);
                startActivity(art1);

            }
        });


        try {
            homeGetProBannerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.startAnimation(buttonClick);
                    Intent rate = new Intent(getApplicationContext(),NotificationActivity.class);
                    try {
                        startActivity(rate);

                    } catch (android.content.ActivityNotFoundException ex) {

                        Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                    }

                }
            });
        } catch (NullPointerException e) {

            Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    public  class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {

            Home.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0)
                    {
                        viewPager.setCurrentItem(1);

                    } else if(viewPager.getCurrentItem()==1)
                    {
                        viewPager.setCurrentItem(2);

                    }else if(viewPager.getCurrentItem()==2)
                    {
                        viewPager.setCurrentItem(3);

                    }else if(viewPager.getCurrentItem()==3)
                    {
                        viewPager.setCurrentItem(4);

                    }else if(viewPager.getCurrentItem()==4)
                    {
                        viewPager.setCurrentItem(5);

                    }
                    else if(viewPager.getCurrentItem()==5)
                    {
                        viewPager.setCurrentItem(0);

                    }

                    else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    //Function to Check If User Installed app for firs time
    private boolean isFirstTime()
    {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        boolean ranBefore = pref.getBoolean("whatsNew",false);
        if(!ranBefore)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("whatsNew",true);
            editor.apply();
        }
        return !ranBefore;

    }

}
