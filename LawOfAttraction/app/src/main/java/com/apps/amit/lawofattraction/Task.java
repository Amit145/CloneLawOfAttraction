package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Task extends AppCompatActivity {


    TextView intText;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    List<TaskUtils> personUtilsList;
    SwipeRefreshLayout mSwipeRefreshLayout1;
    Resources resources;
    String taskURL = "";
    LinearLayout parent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
        interstitial.setAdListener(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        interstitial.setAdListener(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        interstitial.setAdListener(null);
        this.finish();
    }


    RequestQueue rq;
    private InterstitialAd interstitial;

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        AdRequest adRequest = new AdRequest.Builder().build();

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");


        taskURL = "http://www.innovativelabs.xyz/Scripts/"+value1+"_taskData.php";

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(getApplicationContext());
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {



            @Override
            public void onAdLoaded() {
                // Call displayInterstitial() function

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

                        Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
                    }

                    if(cal.getTime().after(c1.getTime()))
                    {
                        //Toast.makeText(getApplicationContext(),cal.getTime()+" "+c1.getTime() , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        displayInterstitial();
                    }

                }

            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                interstitial.setAdListener(null);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {

                interstitial.setAdListener(null);
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                interstitial.setAdListener(null);
                // Code to be executed when when the interstitial ad is closed.
            }


        });



        parent = findViewById(R.id.taskParent);

        intText = findViewById(R.id.internetText);

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        mSwipeRefreshLayout1 =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout1.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout1.setRefreshing(true);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout1.setRefreshing(false);
                        Intent i = new Intent(getApplicationContext(), Task.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Task.this.finish();
                        startActivity(i);
                    }
                },2000);
            }
        });


        if(netInfo!=null && netInfo.isConnected()) {
            rq = Volley.newRequestQueue(getApplicationContext());


            recyclerView =  findViewById(R.id.recyclerView1);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);
            try {

                intText.setVisibility(View.INVISIBLE);
            } catch (Exception e) {

                Toast.makeText(this,  resources.getString(R.string.nameError4), Toast.LENGTH_SHORT).show();

            }

            personUtilsList = new ArrayList<>();
            sendRequest(taskURL);

        } else

            {

                intText.setVisibility(View.VISIBLE);

                Toast.makeText(this,  resources.getString(R.string.noInternet_txt), Toast.LENGTH_SHORT).show();

            }

    }


    public void sendRequest(String taskURL){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( taskURL,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    TaskUtils personUtils = new TaskUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setPersonFirstName(jsonObject.getString("name"));
                        personUtils.setPersonLastName(jsonObject.getString("body"));
                        personUtils.setJobProfile(jsonObject.getString("likes"));
                        personUtils.setimgUrl(jsonObject.getString("imgUrl"));
                        personUtils.setid(jsonObject.getInt("id"));
                        personUtils.setShares(jsonObject.getString("shares"));
                        personUtils.setViews(jsonObject.getString("views"));
                        personUtils.setbody(jsonObject.getString("body"));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);


                }

                mAdapter = new TaskAdapter(getApplicationContext(),personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
            }
        });

        rq.add(jsonArrayRequest);


    }



}
