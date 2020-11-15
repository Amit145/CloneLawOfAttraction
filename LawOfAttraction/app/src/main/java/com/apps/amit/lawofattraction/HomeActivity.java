package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.apps.amit.lawofattraction.adapters.MyCustomPagerAdapter;
import com.apps.amit.lawofattraction.helper.CheckInternetService;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.utils.JSONURLParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ViewPager viewPager;
    LinearLayout mainlayout;
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
    int[] images = new int[6];
    String newToken;
    MyCustomPagerAdapter myCustomPagerAdapter;
    SharedPreferences sharedpreferences;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        img1 = null;
        img2 = null;
        img3 = null;
        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        timer.purge();
        ActivityCompat.finishAffinity(HomeActivity.this);
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
        // automatically handle clicks on the HomeActivity/Up button, so long
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                /*
                Not Required
                */
            }
        });

        CheckInternetService checkInternetService = new CheckInternetService();
        NetworkInfo netInfo = checkInternetService.checkInternetConnection(getApplicationContext());

        if(netInfo!=null && netInfo.isConnected()) {
            JSONURLParser jsonurlParser = new JSONURLParser(getApplicationContext());
            jsonurlParser.execute();
        }

        //internet
        TextView nameText;
        ButterKnife.bind(this);

        images[0] = R.drawable.affirmpager;
        images[1] = R.drawable.p;
        images[2] = R.drawable.p7;
        images[3] = R.drawable.p4;
        images[4] = R.drawable.p1;
        images[5] = R.drawable.p2;

        setTitle(getString(R.string.Home_title));

        setContentView(R.layout.activity_home);
        mainlayout = findViewById(R.id.mainlayout);

        Glide.with(this).load(R.drawable.backgroundabstract).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainlayout.setBackground(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                /*
                Not Required
                 */
            }
        });


        FirebaseMessaging.getInstance().subscribeToTopic("v4.0")   //v3.9
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (task.isSuccessful()) {
                            Log.i(msg, msg);
                        }
                    }
                });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                 newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

                sharedpreferences = getSharedPreferences("FirebaseToken", HomeActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("userToken", newToken);
                editor.apply();
            }
        });

        SharedPreferences nameSp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        String name = nameSp1.getString("userName", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nameText = findViewById(R.id.textView3);
        homeSubTitle = findViewById(R.id.textView2);
        homeMyStories = findViewById(R.id.textView4);
        homeSayToUniverse = findViewById(R.id.textView5);
        homeSettings = findViewById(R.id.textView6);
        homeGetProBannerText = findViewById(R.id.prover);
        button1 = findViewById(R.id.staButton);
        linearBanner = findViewById(R.id.linearBanner);
        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        SharedPreferences socialDetails = getSharedPreferences("SocialAccount", LoginActivity.MODE_PRIVATE);

        if(socialDetails.contains("personName")) {

            String value = socialDetails.getString("personName", "");
            TextView userName   = header.findViewById(R.id.appDrawerTextView);

            if (value!=null && !value.isEmpty()) {
                userName.setText(value);

            } else {
                userName.setText(getString(R.string.nav_header_title));
            }
        }

        if(socialDetails.contains("personPhoto")) {

            String value = socialDetails.getString("personPhoto", "");
            Uri uri = Uri.parse(value);
            ImageView imageView   = header.findViewById(R.id.appDrawerImageView);

            if (value!=null && !value.isEmpty() && !value.equals("null")) {
                Glide.with(this).load(uri).override(200, 200).into(imageView);

            }  else {
                imageView.setImageResource(R.mipmap.ic_launcher_round);
            }
        }

        //if ads enabled
        SharedPreferences pref = getSharedPreferences("AdvsPref", MODE_PRIVATE);
        Calendar dummyDate = Calendar.getInstance();
        dummyDate.set(Calendar.YEAR, 2011);

        //Store selected language in a Variable called value
        String value = pref.getString("adsDate", dummyDate.getTime().toString());

        if (value!=null && !value.isEmpty()) {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

            try {
                cal.setTime(sdf.parse(value)); // all done
            } catch (ParseException e) {
                Log.e("ERROR", String.valueOf(e));
            }
            homeGetProBannerText.setText(getString(R.string.Home_getProBannerText));
        }

        homeSubTitle.setText(getString(R.string.Home_subTitle));
        button1.setText(getString(R.string.Home_startButtonText));
        homeMyStories.setText(getString(R.string.Home_myStories));
        homeSayToUniverse.setText(getString(R.string.Home_sayToUniverse));
        homeSettings.setText(getString(R.string.affirmationsDrawerTitle));

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        if (id == R.id.nav_camera) {

                            Intent i = new Intent(getApplicationContext(), InformationActivity.class);
                            startActivity(i);
                            // Handle the camera action
                        } else if (id == R.id.nav_gallery) {

                            Intent art1 = new Intent(getApplicationContext(), SayThankYouActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_slideshow) {

                            Intent art1 = new Intent(getApplicationContext(), MusicListActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_manage) {

                            Intent art1 = new Intent(getApplicationContext(), DailyQuotesActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_story) {

                            Intent art1 = new Intent(getApplicationContext(), StoryListActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_us) {

                            Intent art1 = new Intent(getApplicationContext(), AboutAppActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_policy) {

                            Intent art1 = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_task) {

                            Intent art1 = new Intent(getApplicationContext(), TaskActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_thanks) {

                            Intent art1 = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_share) {

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                            try {
                                startActivity(Intent.createChooser(share, getString(R.string.chooseToShare)));

                            } catch (android.content.ActivityNotFoundException ex) {

                                Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                            }

                        } else if (id == R.id.nav_send) {

                            Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattraction");
                            Intent rate = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                startActivity(rate);

                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                            }

                        } else if (id == R.id.nav_send1) {

                            Intent art1 = new Intent(getApplicationContext(), UserFeedbackActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.action) {

                            Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattractionpro");
                            Intent rate = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                startActivity(rate);

                            } catch (android.content.ActivityNotFoundException ex) {

                                Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                            }
                        } else if (id == R.id.nav_calendar) {

                            Intent art1 = new Intent(getApplicationContext(), ManifestationTrackerActivity.class);
                            startActivity(art1);

                        } else if (id == R.id.nav_mail) {

                            SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
                            String name = sp1.getString("userName", "");

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.parse("mailto:lawofattractiondaily@innovativelabs.xyz");
                            if (name!=null && !name.equalsIgnoreCase("")) {
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Mail From \" " + name + " \"");

                            } else {

                                intent.putExtra(Intent.EXTRA_SUBJECT, "New Mail");
                            }
                            intent.setData(data);
                            startActivity(intent);
                        }

                        else if (id == R.id.signIn) {

                            Intent art1 = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(art1);
                        }

                        /*
                        else if (id == R.id.audioManifest) {

                            Intent art1 = new Intent(getApplicationContext(), AudioWishHome.class);
                            startActivity(art1);

                        }
                        */

                        drawer = findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        if (name!=null && !(name.equalsIgnoreCase(""))) {
            String[] nameArray = name.split(" ");
            String welcomeUserTxt = getString(R.string.Home_textBelowPager1) + " " + nameArray[0] + getString(R.string.Home_textBelowPager2);
            nameText.setText(welcomeUserTxt);
        } else {
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
        timer.scheduleAtFixedRate(new MyTimerTask(), 3500, 3500);

        Glide.with(getApplicationContext()).load(R.drawable.experience).thumbnail(0.1f).fitCenter().into(img1);

        Glide.with(getApplicationContext()).load(R.drawable.comments).thumbnail(0.1f).fitCenter().into(img2);

        Glide.with(getApplicationContext()).load(R.drawable.affirmationslogonew).thumbnail(0.1f).fitCenter().into(img3);

        if (isFirstTime()) {

            //If First Time then do the following activity
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            final View dialogView = View.inflate(getApplicationContext(),R.layout.whatsnew, null);
            builder.setCancelable(false);
            builder.setMessage("What's New In v4.0");

            builder.setView(dialogView);
            alert = builder.create();
            alert.show();
            Button submit = dialogView.findViewById(R.id.btnsubmit);
            Button cancel = dialogView.findViewById(R.id.btncancel);
            ImageView img = dialogView.findViewById(R.id.storyImage);

            Glide.with(getApplicationContext()).load(R.drawable.synclogo).thumbnail(0.1f).into(img);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), LoginActivity.class);
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
                Intent art1 = new Intent(getApplicationContext(), Exercise1Activity.class);
                startActivity(art1);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(), MyStoryActivity.class);
                startActivity(art1);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(), CommentsActivity.class);
                startActivity(art1);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent art1 = new Intent(getApplicationContext(), AffirmationHome.class);
                startActivity(art1);
            }
        });

        try {
            homeGetProBannerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattractionpro");
                    Intent rate = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(rate);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), R.string.nameError4, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.e("ERROR", String.valueOf(e));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else if (viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(3);
                    } else if (viewPager.getCurrentItem() == 3) {
                        viewPager.setCurrentItem(4);
                    } else if (viewPager.getCurrentItem() == 4) {
                        viewPager.setCurrentItem(5);
                    } else if (viewPager.getCurrentItem() == 5) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    //Function to Check If User Installed app for firs time
    private boolean isFirstTime() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        boolean ranBefore = pref.getBoolean("whatsNewv4.0", false);
        if (!ranBefore) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("whatsNewv4.0", true);
            editor.apply();
        }
        return !ranBefore;
    }
}