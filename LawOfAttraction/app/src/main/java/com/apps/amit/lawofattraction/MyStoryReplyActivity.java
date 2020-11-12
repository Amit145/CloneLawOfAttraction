package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.apps.amit.lawofattraction.adapters.MyStoryReplyAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.utils.MyStoryReplyUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStoryReplyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView textView1;
    TextView textView2;
    Resources resources;
    LinearLayout mainlayout;
    EditText edt, edt1;
    AlertDialog alert;
    String id;
    String uToken;
    boolean flag = false;
    public static final String TAG = MyStoryReplyActivity.class.getSimpleName();

    //free version
    String serverKey ="AAAAZJbjH64:APA91bFz_84cJQm9GCIRB62VoHD0IIqboeLcKOPVHY-ANhSWIgX4wBPXxCn5kgejKRsd0w_LHXgHMhSmztm62lJA64zs3Pe7oAwNBn-O29iZnBJuvMfMg0Ik0FpMCSmKyQvtyvOC-UCt";

    SwipeRefreshLayout mSwipeRefreshLayout;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    List<MyStoryReplyUtils> personUtilsList;
    RequestQueue rq;

    String requestUrl = "http://www.innovativelabs.xyz/show_uComment.php";

    private InterstitialAd interstitialAd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        displayInterstitial();
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitialAd == null || !interstitialAd.isLoaded()) {
            return;
        }


        if (Boolean.TRUE.equals(flag) && interstitialAd.isLoaded()) {
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
        setContentView(R.layout.activity_exp_story);

        Intent result = getIntent();

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        mainlayout = findViewById(R.id.mainlayout);


        Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainlayout.setBackground(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        //Store selected language in a Variable called value
        final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        t1 =  findViewById(R.id.textView10);
        t2 =  findViewById(R.id.textView11);
        t3 =  findViewById(R.id.textView12);
        t4 =  findViewById(R.id.reply);

        t4.setText(resources.getString(R.string.expReply1));


        //check If Intent values are not null
        if(result.getExtras()!=null)
        {
            String name = result.getExtras().getString("NameKey");
            String story = result.getExtras().getString("StoryKey");
            String date = result.getExtras().getString("DateKey");
            id = result.getExtras().getString("idKey");
            uToken = result.getExtras().getString("Utoken");

            t1.setText(name);
            t2.setText(story);
            t3.setText(date);
        }

        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.refresh_text), Toast.LENGTH_SHORT).show();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        personUtilsList.clear();
                        mAdapter.notifyDataSetChanged();
                        sendRequest();
                    }
                }, 2000);
            }
        });

        rq = Volley.newRequestQueue(this);

        recyclerView =  findViewById(R.id.recylcerView4);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        personUtilsList = new ArrayList<>();


        sendRequest();

        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(getString(R.string.TestInterstitialAdsBannerGoogle));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Interstitial ad Loaded!");
                flag=Boolean.TRUE;
            }
        });
    }

    public void sendRequest() {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(requestUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    MyStoryReplyUtils personUtils = new MyStoryReplyUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String val = jsonObject.getString("id");

                        if(val.equalsIgnoreCase(id)) {
                            personUtils.setPersonFirstName(jsonObject.getString("name"));
                            personUtils.setPersonLastName(jsonObject.getString("comm"));
                            personUtils.setJobProfile(jsonObject.getString("time"));

                            personUtilsList.add(personUtils);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter = new MyStoryReplyAdapter(MyStoryReplyActivity.this, personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(error.getMessage(),""+error.getMessage());

            }
        });

        rq.add(jsonArrayRequest);

    }


    public void onReply(View view) {

        view.startAnimation(buttonClick);
        AlertDialog.Builder builder = new AlertDialog.Builder(MyStoryReplyActivity.this);
        final View dialogView = View.inflate(getApplicationContext(),R.layout.exp_options, null);
        builder.setCancelable(false);
        builder.setMessage(resources.getString(R.string.expReply)+"\n ------------------------------------------------\n "+resources.getString(R.string.myStory_warn));

        builder.setView(dialogView);
        alert = builder.create();
        alert.show();


        edt =  dialogView.findViewById(R.id.username);
        edt1 =  dialogView.findViewById(R.id.comments);

        textView1 =  dialogView.findViewById(R.id.name);
        textView2 =  dialogView.findViewById(R.id.experience);

        Button submit =  dialogView.findViewById(R.id.btnsubmit);
        Button cancel =  dialogView.findViewById(R.id.btncancel);

        textView1.setText(resources.getString(R.string.enterName));
        textView2.setText(resources.getString(R.string.expRplyText));
        submit.setText(resources.getString(R.string.subUniverse_dialogSubmit));
        cancel.setText(resources.getString(R.string.subUniverse_dialogCancel));


        SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        String naam = sp1.getString("userName","");

        if((naam!= null && naam.equalsIgnoreCase("")))
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


               GetDataFromEditText();

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

    public void GetDataFromEditText(){



        String userName = edt.getText().toString();
        String userComment = edt1.getText().toString();
        String date =  DateFormat.getDateTimeInstance().format(new Date());


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", userName);
        editor.apply();

        checkEditText(userName,userComment,date,id);

    }

    public void  checkEditText(String userName,String userComment,String date, String id) {



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

        else if (TextUtils.isEmpty(userComment)) {

            edt1.setError(resources.getString(R.string.expReply2));

        }
        else
        {
            SendQuoteToServer(userName,userComment,date,id,uToken,serverKey);



        }
    }
    public void SendQuoteToServer(final String Name,final String Comment, final String Time,final String id,final String TOKEN,final String SERVERKEY){

        String url = "http://www.innovativelabs.xyz/insert_uComment.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        personUtilsList.clear();
                        mAdapter.notifyDataSetChanged();
                        sendRequest();
                        alert.dismiss();
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

                params.put("UName", Name);
                params.put("UComment", Comment);
                params.put("Time", Time);
                params.put("Uid", id);
                params.put("FToken", TOKEN);
                params.put("ServerKey", SERVERKEY);

                return params;
            }
        };

        rq.add(postRequest);

    }
}
