package com.apps.amit.lawofattraction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;


public class comments extends AppCompatActivity {

    Button story;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Resources resources;
    EditText edt, edt1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String UserName, UserComment, timee;
    RequestQueue requestQueue;
    TextView txt,txt1,nameText,wishText;
    ImageView img;
    ConnectivityManager connMngr;
    CheckBox checkBox;
    NetworkInfo netInfo;
    String DataParseUrl = "http://www.innovativelabs.xyz/insert_data.php";
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    private AdView mAdView69;
    RecyclerView.LayoutManager layoutManager;
    List<PersonUtils> personUtilsList;
    RequestQueue rq;
    String request_url = "http://www.innovativelabs.xyz/jsonData.php";
    private InterstitialAd interstitial;

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue=null;
        story=null;
        txt=null;
        txt1=null;
        this.finish();
        mAdView69=null;
        interstitial.setAdListener(null);
        recyclerView.setLayoutManager(null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdView69=null;
        interstitial.setAdListener(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        interstitial.setAdListener(null);
        mAdView69=null;
        this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        try{

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        story =  findViewById(R.id.story);   //Button

        txt =  findViewById(R.id.refresh);        //refresh message

        txt1 =  findViewById(R.id.feed);          //get pro version

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad

        interstitial = new InterstitialAd(getApplicationContext());

        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {

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

        mAdView69 =  findViewById(R.id.adView69);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView69.loadAd(adRequest1);

        ButterKnife.bind(this);

        //get user selected language from shared preferences

           // SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            //final String value = pref.getString("language","en");

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
                        Intent i = new Intent(getApplicationContext(), comments.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        comments.this.finish();
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

        personUtilsList = new ArrayList<>();

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
            try {

            txt1.setVisibility(View.VISIBLE);

            }
            catch(NullPointerException e) {

            }
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



        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(comments.this);
                final View dialogView = View.inflate(getApplicationContext(),R.layout.dialog_options, null);
                builder.setCancelable(false);
                builder.setMessage(resources.getString(R.string.subUniverse_dialogTitle));

                builder.setView(dialogView);
                final AlertDialog alert = builder.create();
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

                SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
                String naam = sp1.getString("userName","");

                if((naam.equalsIgnoreCase("")))
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



    } catch(NullPointerException e)
        {
            System.gc();
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

        UserName = edt.getText().toString();
        UserComment = edt1.getText().toString();
        timee =  DateFormat.getDateTimeInstance().format(new Date());


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", UserName);
        editor.apply();

        //Function call to verify user wish for any inappropriate content
        checkEditText(UserName,UserComment,value);

    }

    //Function called for Private Wishes
    public void personalWish(String value, AlertDialog alert){

        UserName = edt.getText().toString();
        UserComment = edt1.getText().toString();
        timee =  DateFormat.getDateTimeInstance().format(new Date());

        if (TextUtils.isEmpty(UserName)  )
        {

            edt.setError(resources.getString(R.string.enterName));

        }
        else if ( TextUtils.isEmpty(UserComment) )
        {

            edt1.setError(resources.getString(R.string.enterWish));

        } else {


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", UserName);
        editor.apply();

        //Send Values to SQLite Database

        WishDataBaseHandler db = new WishDataBaseHandler(this);

        db.addWish(new WishDB(UserName,UserComment,timee));
        alert.dismiss();

        Intent openIntent = new Intent(getApplicationContext(),PrivateWishes.class);
        startActivity(openIntent);

        }


    }

    //If Public Send Data Server
    public void SendDataToServer(final String name, final String email, final String website, final String value){



        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("website", website));


                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(DataParseUrl);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                subnetchek(value);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, email, website);
    }


    public void sendRequest(){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( request_url,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    PersonUtils personUtils = new PersonUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setPersonFirstName(jsonObject.getString("name"));
                        personUtils.setPersonLastName(jsonObject.getString("email"));
                        personUtils.setJobProfile(jsonObject.getString("website"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);

                }

                mAdapter = new CommentsRecyclerAdapter(getApplicationContext(), personUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

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
            catch(NullPointerException e) {

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


                SendDataToServer(UserName, UserComment, timee,value);

                Intent i = new Intent(getApplicationContext(), comments.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                comments.this.finish();
                startActivity(i);
                netcheck(value);

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
            catch(NullPointerException e) {

            }
            recyclerView.setVisibility(View.INVISIBLE);


            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();


        }



    }


    public void netcheck(String value)
    {

        Context context = LocaleHelper.setLocale(this, value);
        resources = context.getResources();

        if(netInfo!=null && netInfo.isConnected())
        {
            story.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            try {

                txt1.setVisibility(View.VISIBLE);

            }
            catch(NullPointerException e) {
                //System.out.println("rest of the code...");
                //  Toast.makeText(getApplicationContext()," Please Enter Valid Seconds ",Toast.LENGTH_LONG).show();

            }


        }
        else
        {
            //Not connected to Internet
            Intent i = new Intent(getApplicationContext(), comments.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            comments.this.finish();
            startActivity(i);

            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }
    }

    public void subnetchek(String value)
    {
        Context context = LocaleHelper.setLocale(this, value);
         resources = context.getResources();

        if(netInfo==null)
        {
            //Not connected to Internet
            story.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
            // img.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);


            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }

    }


    public void feed(View view) {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        //SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(this, "en");
         resources = context.getResources();
        try {

              /*
                    view.startAnimation(buttonClick);
                    Uri uri = Uri.parse("market://details?id=com.apps.amit.lawofattractionpro");
                    Intent rate = new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(rate);

                    } catch (android.content.ActivityNotFoundException ex) {
                        //Toast.makeText(this,"Error".toString(),Toast.LENGTH_SHORT);
                    }

            */

              Intent openActivity = new Intent(getApplicationContext(),PrivateWishes.class);
              startActivity(openActivity);

        } catch (NullPointerException e) {

            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
        }


    }
}






