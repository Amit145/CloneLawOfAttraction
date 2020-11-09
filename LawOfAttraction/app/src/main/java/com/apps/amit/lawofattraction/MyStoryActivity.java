package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.adapters.MyStoryAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.utils.MyStoryUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class MyStoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    EditText edt, edt1;
    TextView nameText,usrExp;
    AlertDialog alert;
    RequestQueue requestQueue;
    RecyclerView.LayoutManager layoutManager;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<MyStoryUtils> personUtilsList;
    Resources resources;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    String token;
    boolean flag = false;
    LinearLayout layoutExperience;
    String value = "en";
    ProgressBar progressBar;

    RequestQueue rq;
    public static final String TAG = MyStoryActivity.class.getSimpleName();

    String requestUrl = "http://innovativelabs.xyz/storyShow.php";


    LinearLayout l1;
    Button butSub;
    TextView txtInt;

    private InterstitialAd interstitialAd;


    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (interstitialAd.isAdInvalidated()) {
            return;
        }

        if (Boolean.TRUE.equals(flag) && interstitialAd.isAdLoaded()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    interstitialAd.show();
                }
            }, 1000);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiences);

        SharedPreferences firebaseToken = getSharedPreferences("FirebaseToken", MyStoryActivity.MODE_PRIVATE);
        token = firebaseToken.getString("userToken", "");

        l1 =  findViewById(R.id.linSto);
        butSub =  findViewById(R.id.butSub);
        txtInt =  findViewById(R.id.txtInt);
        progressBar = findViewById(R.id.progressBar2);
        layoutExperience = findViewById(R.id.layoutExperiences);
        requestQueue = Volley.newRequestQueue(this);



        Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    l1.setBackground(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });


        ButterKnife.bind(this);

        //get user selected language from shared preferences
        //Store selected language in a Variable called value

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        butSub.setText(resources.getString(R.string.myStory_buttonText));
        txtInt.setText(resources.getString(R.string.noInternet_txt));

        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);

                        Intent i = new Intent(getApplicationContext(), MyStoryActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyStoryActivity.this.finish();
                        startActivity(i);

                    }
                }, 2000);
            }
        });


        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        if(netInfo!=null && netInfo.isConnected())
        {
            txtInt.setVisibility(View.INVISIBLE);
            l1.setVisibility(View.VISIBLE);
            butSub.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);


            rq = Volley.newRequestQueue(this);

            recyclerView =  findViewById(R.id.recylcerView2);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);

            personUtilsList = new ArrayList<>();

            sendRequest();

            interstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), getString(R.string.facebook_interstitial_id));
            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener((new MyStoryActivity.InterstitialListener())).build());

        }
        else {

            txtInt.setVisibility(View.VISIBLE);
            l1.setVisibility(View.INVISIBLE);
            butSub.setVisibility(View.INVISIBLE);
        }
    }


    private class InterstitialListener implements InterstitialAdExtendedListener {

        @Override
        public void onInterstitialActivityDestroyed() {

            Log.d(TAG, "Interstitial ad onInterstitialActivityDestroyed!");
        }

        @Override
        public void onInterstitialDisplayed(Ad ad) {

            Log.d(TAG, "Interstitial ad InterstitialDisplayed!");
        }

        @Override
        public void onInterstitialDismissed(Ad ad) {

            Log.d(TAG, "Interstitial ad InterstitialDismissed!");
        }

        @Override
        public void onError(Ad ad, AdError adError) {

            Log.d(TAG, "Interstitial ad onError!");
        }

        @Override
        public void onAdLoaded(Ad ad) {

            Log.d(TAG, "Interstitial ad Loaded!");
            flag=Boolean.TRUE;
        }

        @Override
        public void onAdClicked(Ad ad) {

            Log.d(TAG, "Interstitial ad AdClicked!");
        }

        @Override
        public void onLoggingImpression(Ad ad) {

            Log.d(TAG, "Interstitial ad LoggingImpression!");
        }

        @Override
        public void onRewardedAdCompleted() {
            Log.d(TAG, "Interstitial ad RewardedAdCompleted!");
        }

        @Override
        public void onRewardedAdServerSucceeded() {

            Log.d(TAG, "Interstitial ad RewardedAdServerSucceeded!");
        }

        @Override
        public void onRewardedAdServerFailed() {

            Log.d(TAG, "Interstitial ad RewardedAdServerFailed!");
        }
    }

    public void sendRequest() {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(requestUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.INVISIBLE);

                for (int i = 0; i < response.length(); i++) {

                    MyStoryUtils personUtils = new MyStoryUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setPersonFirstName(jsonObject.getString("name"));
                        personUtils.setPersonLastName(jsonObject.getString("story"));
                        personUtils.setJobProfile(jsonObject.getString("date"));
                        personUtils.setcount(jsonObject.getString("id"));
                        personUtils.settoken(jsonObject.getString("token"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);


                }

                mAdapter = new MyStoryAdapter(MyStoryActivity.this, personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(error.getMessage(),""+error.getMessage());
                Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

            }
        });

        rq.add(jsonArrayRequest);

    }

    public void update(View view) {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        //Store selected language in a Variable called value

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        view.startAnimation(buttonClick);
        AlertDialog.Builder builder = new AlertDialog.Builder(MyStoryActivity.this);
        final View dialogView = View.inflate(getApplicationContext(),R.layout.exp_options, null);
        builder.setCancelable(false);
        builder.setMessage(resources.getString(R.string.myStory_dialogTitle)+"  \n ------------------------------------------------\n "+resources.getString(R.string.myStory_warn));

        builder.setView(dialogView);
        alert = builder.create();
        alert.show();


        nameText =  dialogView.findViewById(R.id.name);
        usrExp =  dialogView.findViewById(R.id.experience);
        edt =  dialogView.findViewById(R.id.username);
        edt1 =  dialogView.findViewById(R.id.comments);

        Button submit =  dialogView.findViewById(R.id.btnsubmit);
        Button cancel =  dialogView.findViewById(R.id.btncancel);

        nameText.setText(resources.getString(R.string.subUniverse_dialogName));
        usrExp.setText(resources.getString(R.string.myStory_dialogStory));
        submit.setText(resources.getString(R.string.subUniverse_dialogSubmit));
        cancel.setText(resources.getString(R.string.subUniverse_dialogCancel));

        SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        String naam = sp1.getString("userName","");

        if((naam!=null && naam.equalsIgnoreCase("")))
        {
            edt.setText("");

        }

        else{

            edt.setText(naam);


        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                GetDataFromEditText(value);

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

    public void GetDataFromEditText(String value){

        String userName = edt.getText().toString();
        String userStory = edt1.getText().toString();
        String date =  DateFormat.getDateTimeInstance().format(new Date());

        SharedPreferences timerEnable = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", userName);
        editor.apply();

        checkEditText(userName,userStory,date,value);

    }

    public void  checkEditText(String userName,String userStory,String date, String value) {

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();


        if (TextUtils.isEmpty(userName)) {

            edt.setError(resources.getString(R.string.enterName));

        } else if (!userName.matches("[a-zA-Z ]+")) {

            edt.setError(resources.getString(R.string.nameError3));



        } else if (edt.getText().length()>=25 )
        {
            edt.setError(resources.getString(R.string.nameError));

        }

        else if (edt.getText().length()<3 )
        {

            edt.setError(resources.getString(R.string.nameError1));

        }

        else if(edt.getText().toString().toLowerCase().contains("axwound") ||
                edt.getText().toString().toLowerCase().contains("anus") ||
                edt.getText().toString().toLowerCase().contains("arsehole") ||
                edt.getText().toString().toLowerCase().contains("cock") ||
                edt.getText().toString().toLowerCase().contains("fuck") ||
                userName.toLowerCase().contains("hole") ||
                userName.toLowerCase().contains("boner") ||
                userName.toLowerCase().contains("blowjob") ||
                userName.toLowerCase().contains("blow job") ||
                userName.toLowerCase().contains("bitch") ||
                userName.toLowerCase().contains("tit") ||
                userName.toLowerCase().contains("bastard") ||
                userName.toLowerCase().contains("camel toe") ||
                userName.toLowerCase().contains("choad") ||
                userName.toLowerCase().contains("chode") ||
                userName.toLowerCase().contains("sucker") ||
                userName.toLowerCase().contains("coochie") ||
                userName.toLowerCase().contains("coochy") ||
                userName.toLowerCase().contains("cooter") ||
                userName.toLowerCase().contains("cum") ||
                userName.toLowerCase().contains("slut") ||
                userName.toLowerCase().contains("cunnie") ||
                userName.toLowerCase().contains("cunnilingus") ||
                userName.toLowerCase().contains("cunt") ||
                userName.toLowerCase().contains("dick") ||
                userName.toLowerCase().contains("dildo") ||
                userName.toLowerCase().contains("doochbag") ||
                userName.toLowerCase().contains("douche") ||
                userName.toLowerCase().contains("dyke") ||
                userName.toLowerCase().contains("fag") ||
                userName.toLowerCase().contains("fellatio") ||
                userName.toLowerCase().contains("feltch") ||
                userName.toLowerCase().contains("butt") ||
                userName.toLowerCase().contains("gay") ||
                userName.toLowerCase().contains("genital") ||
                userName.toLowerCase().contains("penis") ||
                userName.toLowerCase().contains("handjob") ||
                userName.toLowerCase().contains("hoe") ||
                userName.toLowerCase().contains("sex") ||
                userName.toLowerCase().contains("jizz") ||
                userName.toLowerCase().contains("kooch") ||
                userName.toLowerCase().contains("kootch") ||
                userName.toLowerCase().contains("kunt") ||
                userName.toLowerCase().contains("lesbo") ||
                userName.toLowerCase().contains("lesbian") ||
                userName.toLowerCase().contains("muff") ||
                userName.toLowerCase().contains("vagina") ||
                userName.toLowerCase().contains("pussy") ||
                userName.toLowerCase().contains("pussies") ||
                userName.toLowerCase().contains("poon") ||
                userName.toLowerCase().contains("piss") ||
                userName.toLowerCase().contains("arse") ||
                userName.toLowerCase().contains("erection") ||
                userName.toLowerCase().contains("rimjob") ||
                userName.toLowerCase().contains("scrote") ||
                userName.toLowerCase().contains("snatch") ||
                userName.toLowerCase().contains("whore") ||
                userName.toLowerCase().contains("wank") ||
                userName.toLowerCase().contains("tard") ||
                userName.toLowerCase().contains("testicle") ||
                userName.toLowerCase().contains("twat") ||
                userName.toLowerCase().contains("boob") ||
                userName.toLowerCase().contains("wiener") ||
                userName.toLowerCase().contains("spank") ||
                userName.toLowerCase().contains("http") ||
                userName.toLowerCase().contains("www") ||
                userName.toLowerCase().contains("ass") ||
                userName.toLowerCase().contains("porn") ||
                userName.toLowerCase().contains("breast") ||
                userName.toLowerCase().contains("gand"))
        {

            edt.setText("");
            edt.setError(resources.getString(R.string.nameError2));

        }

        else if (TextUtils.isEmpty(userStory)) {

            edt1.setError(resources.getString(R.string.myStory_exp));

        }
        else {
            SendQuoteToServer(userName, userStory, date,token);
        }

    }

    public void SendQuoteToServer(final String Name,final String Story, final String Time,final String Token){

        String url = "http://www.innovativelabs.xyz/insert_story.php";
        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {

                        alert.dismiss();

                        displayInterstitial();

                        try {
                            personUtilsList.clear();
                            mAdapter.notifyDataSetChanged();
                            sendRequest();

                        } catch (NullPointerException e) {

                            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e(error.getMessage(), ""+error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map< String, String > getParams() {
                Map < String, String > params = new HashMap< >();

                params.put("Name", Name);
                params.put("Story", Story);
                params.put("Time", Time);
                params.put("FSToken", Token);

                return params;
            }
        };

        requestQueue.add(postRequest);


    }

}