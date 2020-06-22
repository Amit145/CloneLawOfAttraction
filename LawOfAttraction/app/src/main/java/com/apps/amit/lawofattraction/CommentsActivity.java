package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import com.apps.amit.lawofattraction.adapters.CommentsRecyclerAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import com.apps.amit.lawofattraction.utils.StoryUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

public class CommentsActivity extends AppCompatActivity {

    Button story;
    LinearLayout mainlayout;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Resources resources;
    AlertDialog alert;
    EditText edt, edt1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String userName;
    String userComment;
    String timee;
    RequestQueue requestQueue;
    TextView txt;
    TextView txt1;
    TextView nameText;
    TextView wishText;
    ConnectivityManager connMngr;
    CheckBox checkBox;
    NetworkInfo netInfo;
    String dataParseUrl = "http://www.innovativelabs.xyz/insert_data.php";
    String userPrivateWish = "http://www.innovativelabs.xyz/insert_userPrivateWish.php";
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<StoryUtils> storyUtilsList;
    RequestQueue rq;
    String requestUrl = "http://www.innovativelabs.xyz/jsonData.php";
    private InterstitialAd interstitialAd;
    public static final String USER_NAME = "userName";
    public static final String TIMER_ENABLED ="timerEnable";
    public static final String TAG = CommentsActivity.class.getSimpleName();
    boolean flag = false;
    ProgressBar progressBar;

    @Override
    protected void onDestroy() {

        if (interstitialAd != null) {
            //interstitialAd.destroy();
        }
        super.onDestroy();


        requestQueue=null;
        story=null;
        txt=null;
        txt1=null;
        this.finish();
        recyclerView.setLayoutManager(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

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
        setContentView(R.layout.activity_comments);

        try{

            mainlayout = findViewById(R.id.mainlayout);

            Glide.with(this).load(R.drawable.starshd).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mainlayout.setBackground(drawable);
                    }
                }
            });


            requestQueue = Volley.newRequestQueue(getApplicationContext());

        story =  findViewById(R.id.story);   //Button

        txt =  findViewById(R.id.refresh);        //refresh message

        txt1 =  findViewById(R.id.feed);          //get pro version

            progressBar = findViewById(R.id.progressBar2);

            progressBar.setVisibility(View.VISIBLE);


        ButterKnife.bind(this);
        Context context = LocaleHelper.setLocale(getApplicationContext(), "en");
        resources = context.getResources();

            //pass the language selected to update Language function


        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(),resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        sendRequest();
                        Intent i = new Intent(getApplicationContext(), CommentsActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CommentsActivity.this.finish();
                        startActivity(i);
                    }
                },2000);
            }
        });

        rq = Volley.newRequestQueue(getApplicationContext());

        recyclerView =  findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        storyUtilsList = new ArrayList<>();

        sendRequest();

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

                netInfo = connMngr.getActiveNetworkInfo();
            }

        if(netInfo!=null && netInfo.isConnected())
        {
            story.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            updateViews("en");
            txt1.setVisibility(View.VISIBLE);

            story.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                    final View dialogView = View.inflate(getApplicationContext(),R.layout.dialog_options, null);
                    builder.setCancelable(false);
                    builder.setMessage(resources.getString(R.string.subUniverse_dialogTitle));

                    builder.setView(dialogView);
                    alert = builder.create();
                    alert.show();

                    nameText =  dialogView.findViewById(R.id.name);
                    wishText =  dialogView.findViewById(R.id.wish);
                    edt =  dialogView.findViewById(R.id.username);
                    edt1 =  dialogView.findViewById(R.id.comments);
                    Button submit =  dialogView.findViewById(R.id.btnsubmit);
                    Button cancel =  dialogView.findViewById(R.id.btncancel);
                    checkBox = dialogView.findViewById(R.id.checkBox);

                    nameText.setText(resources.getString(R.string.subUniverse_dialogName));
                    wishText.setText(resources.getString(R.string.subUniverse_dialogWish));
                    submit.setText(resources.getString(R.string.subUniverse_dialogSubmit));
                    cancel.setText(resources.getString(R.string.subUniverse_dialogCancel));

                    SharedPreferences sp1 = getSharedPreferences(TIMER_ENABLED, MyStoryActivity.MODE_PRIVATE);
                    String name = sp1.getString(USER_NAME,"");

                    if((name!=null && name.equalsIgnoreCase("")))
                    {
                        edt.setText("");

                    }

                    else{

                        edt.setText(name);

                    }

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(buttonClick);

                            if(checkBox.isChecked()) {

                                personalWish("en",alert);

                            } else {

                                GetDataFromEditText("en");
                            }
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
            });

            interstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), getString(R.string.facebook_interstitial_id));
            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener((new CommentsActivity.InterstitialListener())).build());

        }
        else
        {
            //Not connected to Internet

            updateViews("en");
            story.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }




    } catch(Exception e)
        {
            Log.e(e.getMessage(),e.getMessage());
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
            flag = Boolean.TRUE;

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

    public void displayInterstitial() {

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
                   try{
                       interstitialAd.show();
                   } catch(Exception e) {
                       Log.e(e.getMessage(),e.getMessage());
                    }
                }
            }, 1000);
        }

    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        resources = context.getResources();

        story.setText(resources.getString(R.string.subUniverse_buttonText));
        txt.setText(resources.getString(R.string.refresh_text));
        txt1.setText(resources.getString(R.string.subUniverse_getPro));

    }

    //Function called for Public Wishes
    public void GetDataFromEditText(String value){

        userName = edt.getText().toString();
        userComment = edt1.getText().toString();
        timee =  DateFormat.getDateTimeInstance().format(new Date());

        SharedPreferences timerEnable = getSharedPreferences(TIMER_ENABLED, MyStoryActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();

        //Function call to verify user wish for any inappropriate content
        if(netInfo.isConnected()) {
            checkEditText(userName, userComment, value);
        } else {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();

        }

    }

    //Function called for Private Wishes
    public void personalWish(String value, AlertDialog alert){

        userName = edt.getText().toString();
        userComment = edt1.getText().toString();
        timee =  DateFormat.getDateTimeInstance().format(new Date());

        if (TextUtils.isEmpty(userName)  )
        {

            edt.setError(resources.getString(R.string.enterName));

        }
        else if ( TextUtils.isEmpty(userComment) )
        {

            edt1.setError(resources.getString(R.string.enterWish));

        } else {


        SharedPreferences timerEnable = getSharedPreferences(TIMER_ENABLED, MyStoryActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();

        //Send Values to SQLite Database

        WishDataBaseHandler db = new WishDataBaseHandler(this);

        db.addWish(new PrivateWishesUtils(userName,userComment,timee));
        alert.dismiss();

            if(netInfo!=null && netInfo.isConnected())
            {
                //send to server
                SendPrivateWishToServer(userName,userComment,timee);
            }

        Intent openIntent = new Intent(getApplicationContext(), PrivateWishesActivity.class);
        startActivity(openIntent);

        }
    }

    //If private
    public void SendPrivateWishToServer(final String name, final String comment, final String date){

        String url = userPrivateWish;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        displayInterstitial();
                        flag = false;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("Error.Response", ""+error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                params.put("name", name);
                params.put("comment", comment);
                params.put("date", date);

                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    //If Public Send Data Server
    public void SendDataToServer(final String name, final String email, final String website, final String value){

            String url = dataParseUrl;
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            alert.dismiss();
                            displayInterstitial();
                            flag = false;

                            try {
                                storyUtilsList.clear();
                                mAdapter.notifyDataSetChanged();
                                sendRequest();

                            } catch (NullPointerException e) {

                                Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.e("Error.Response", ""+error.getMessage());
                            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<>();

                    params.put("name", name);
                    params.put("email", email);
                    params.put("website", website);

                    return params;
                }
            };

            requestQueue.add(postRequest);
    }


    public void sendRequest(){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( requestUrl,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.INVISIBLE);

                for(int i = 0; i < response.length(); i++){

                    StoryUtils storyUtils = new StoryUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        storyUtils.setPersonFirstName(jsonObject.getString("name"));
                        storyUtils.setPersonLastName(jsonObject.getString("email"));
                        storyUtils.setJobProfile(jsonObject.getString("website"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    storyUtilsList.add(storyUtils);
                }

                mAdapter = new CommentsRecyclerAdapter(getApplicationContext(), storyUtilsList);

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


    public void  checkEditText(String checkName,  String checkDesc, String value)
    {
        //Array list initialize
        Context context = LocaleHelper.setLocale(this, value);
        resources = context.getResources();

        if(netInfo!=null && netInfo.isConnected())
        {
            story.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            try {

                txt1.setVisibility(View.VISIBLE);

            }
            catch(Exception e) {
                Log.e(e.getMessage(),e.getMessage());
            }


            if (TextUtils.isEmpty(checkName)  )
            {

                edt.setError(resources.getString(R.string.enterName));

            }
            else if ( TextUtils.isEmpty(checkDesc) )
            {

                edt1.setError(resources.getString(R.string.enterWish));

            }

            else if (edt.getText().length()>=25 )
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
                    checkName.toLowerCase().contains("hole") ||
                    checkName.toLowerCase().contains("boner") ||
                    checkName.toLowerCase().contains("blowjob") ||
                    checkName.toLowerCase().contains("blow job") ||
                    checkName.toLowerCase().contains("bitch") ||
                    checkName.toLowerCase().contains("tit") ||
                    checkName.toLowerCase().contains("bastard") ||
                    checkName.toLowerCase().contains("camel toe") ||
                    checkName.toLowerCase().contains("choad") ||
                    checkName.toLowerCase().contains("chode") ||
                    checkName.toLowerCase().contains("sucker") ||
                    checkName.toLowerCase().contains("coochie") ||
                    checkName.toLowerCase().contains("coochy") ||
                    checkName.toLowerCase().contains("cooter") ||
                    checkName.toLowerCase().contains("cum") ||
                    checkName.toLowerCase().contains("slut") ||
                    checkName.toLowerCase().contains("cunnie") ||
                    checkName.toLowerCase().contains("cunnilingus") ||
                    checkName.toLowerCase().contains("cunt") ||
                    checkName.toLowerCase().contains("dick") ||
                    checkName.toLowerCase().contains("dildo") ||
                    checkName.toLowerCase().contains("doochbag") ||
                    checkName.toLowerCase().contains("douche") ||
                    checkName.toLowerCase().contains("dyke") ||
                    checkName.toLowerCase().contains("fag") ||
                    checkName.toLowerCase().contains("fellatio") ||
                    checkName.toLowerCase().contains("feltch") ||
                    checkName.toLowerCase().contains("butt") ||
                    checkName.toLowerCase().contains("gay") ||
                    checkName.toLowerCase().contains("genital") ||
                    checkName.toLowerCase().contains("penis") ||
                    checkName.toLowerCase().contains("handjob") ||
                    checkName.toLowerCase().contains("hoe") ||
                    checkName.toLowerCase().contains("sex") ||
                    checkName.toLowerCase().contains("jizz") ||
                    checkName.toLowerCase().contains("kooch") ||
                    checkName.toLowerCase().contains("kootch") ||
                    checkName.toLowerCase().contains("kunt") ||
                    checkName.toLowerCase().contains("lesbo") ||
                    checkName.toLowerCase().contains("lesbian") ||
                    checkName.toLowerCase().contains("muff") ||
                    checkName.toLowerCase().contains("vagina") ||
                    checkName.toLowerCase().contains("pussy") ||
                    checkName.toLowerCase().contains("pussies") ||
                    checkName.toLowerCase().contains("poon") ||
                    checkName.toLowerCase().contains("piss") ||
                    checkName.toLowerCase().contains("arse") ||
                    checkName.toLowerCase().contains("erection") ||
                    checkName.toLowerCase().contains("rimjob") ||
                    checkName.toLowerCase().contains("scrote") ||
                    checkName.toLowerCase().contains("snatch") ||
                    checkName.toLowerCase().contains("whore") ||
                    checkName.toLowerCase().contains("wank") ||
                    checkName.toLowerCase().contains("tard") ||
                    checkName.toLowerCase().contains("testicle") ||
                    checkName.toLowerCase().contains("twat") ||
                    checkName.toLowerCase().contains("boob") ||
                    checkName.toLowerCase().contains("wiener") ||
                    checkName.toLowerCase().contains("spank") ||
                    checkName.toLowerCase().contains("http") ||
                    checkName.toLowerCase().contains("www") ||
                    checkName.toLowerCase().contains("ass") ||
                    checkName.toLowerCase().contains("porn") ||
                    checkName.toLowerCase().contains("breast") ||
                    checkName.toLowerCase().contains("gand"))
            {

                edt.setText("");
                edt.setError(resources.getString(R.string.nameError2));

            } else if (!checkName.matches("[a-zA-Z ]+")) {

                edt.setError(resources.getString(R.string.nameError3));

            } else {

                SendDataToServer(userName, userComment, timee, value);

            }
        }
        else
        {
            //Not connected to Internet
            story.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
            try {

                txt1.setVisibility(View.INVISIBLE);

            }
            catch(Exception e) {
                Log.e(e.getMessage(),e.getMessage());
            }
            recyclerView.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }
    }

    public void feed(View view) {

        ButterKnife.bind(this);

        Context context = LocaleHelper.setLocale(this, "en");
        resources = context.getResources();

        try {
              Intent openActivity = new Intent(getApplicationContext(), PrivateWishesActivity.class);
              startActivity(openActivity);

        } catch (NullPointerException e) {

            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
        }
    }
}






