package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.EditText;
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
import com.google.firebase.iid.FirebaseInstanceId;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class experiences extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    EditText edt, edt1;
    TextView nameText,usrExp;
    public AlertDialog alert;
    RecyclerView.LayoutManager layoutManager;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<DBUtils3> personUtilsList;
    Resources resources;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    String token;
    String value = "en";

    RequestQueue rq;

    String request_url = "http://innovativelabs.xyz/storyShow.php";


    LinearLayout l1;
    Button butSub;
    TextView txtInt;

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
        setContentView(R.layout.activity_experiences);

        token = FirebaseInstanceId.getInstance().getToken();

        l1 =  findViewById(R.id.linSto);
        butSub =  findViewById(R.id.butSub);
        txtInt =  findViewById(R.id.txtInt);


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
                       // Toast.makeText(getApplicationContext(),cal.getTime()+" "+c1.getTime() , Toast.LENGTH_SHORT).show();
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

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

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

                        Intent i = new Intent(getApplicationContext(), experiences.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        experiences.this.finish();
                        startActivity(i);
//                        personUtilsList.clear();
                        //                       mAdapter.notifyDataSetChanged();
                        //sendRequest();
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


            rq = Volley.newRequestQueue(this);

            recyclerView =  findViewById(R.id.recylcerView2);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);

            personUtilsList = new ArrayList<>();

            sendRequest();

        }
        else {

            txtInt.setVisibility(View.VISIBLE);
            l1.setVisibility(View.INVISIBLE);
            butSub.setVisibility(View.INVISIBLE);
        }
    }


    public void sendRequest() {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(request_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    DBUtils3 personUtils = new DBUtils3();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setPersonFirstName(jsonObject.getString("name"));
                        personUtils.setPersonLastName(jsonObject.getString("story"));
                        personUtils.setJobProfile(jsonObject.getString("date"));
                        personUtils.setcount(jsonObject.getString("id"));
                        personUtils.settoken(jsonObject.getString("token"));
                        // personUtils.setcount(jsonObject.getString("id"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);
                    //Collections.reverse(personUtilsList);


                }

                mAdapter = new uexperiences(experiences.this, personUtilsList);

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

    public void update(View view) {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();

        view.startAnimation(buttonClick);
        AlertDialog.Builder builder = new AlertDialog.Builder(experiences.this);
        //LayoutInflater inflater = experiences.this.getLayoutInflater();
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
        //edt4 = ) dialogView.findViewById(R.id.share);



        Button submit =  dialogView.findViewById(R.id.btnsubmit);
        Button cancel =  dialogView.findViewById(R.id.btncancel);

        nameText.setText(resources.getString(R.string.subUniverse_dialogName));
        usrExp.setText(resources.getString(R.string.myStory_dialogStory));
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
            //Toast.makeText(getApplicationContext(), "Name set: " +naam, Toast.LENGTH_LONG).show();


        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                GetDataFromEditText(value);


                //Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_LONG).show();

                // sendRequest();
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

        String Name = edt.getText().toString();
        String Story = edt1.getText().toString();
        //String share = edt4.getText().toString();
        String date =  DateFormat.getDateTimeInstance().format(new Date());

        SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", Name);
        editor.apply();

        checkEditText(Name,Story,date,value);

    }

    public void  checkEditText(String Name,String Story,String date, String value) {

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();


        if (TextUtils.isEmpty(Name)) {

            edt.setError(resources.getString(R.string.enterName));


            //edt1.setError("Please Enter Your Wish");
            //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

        } else if (!Name.matches("[a-zA-Z ]+")) {

            edt.setError(resources.getString(R.string.nameError3));



        } else if (edt.getText().length()>=25 )
        {
            edt.setError(resources.getString(R.string.nameError));
            //Toast.makeText(getApplicationContext(), "Name Cannot be more than 25 characters", Toast.LENGTH_LONG).show();

        }

        else if (edt.getText().length()<3 )
        {

            edt.setError(resources.getString(R.string.nameError1));
            // Toast.makeText(getApplicationContext(), "Name Cannot be less than 3 characters", Toast.LENGTH_LONG).show();

        }

        else if(edt.getText().toString().toLowerCase().contains("axwound") ||
                edt.getText().toString().toLowerCase().contains("anus") ||
                edt.getText().toString().toLowerCase().contains("arsehole") ||
                edt.getText().toString().toLowerCase().contains("cock") ||
                edt.getText().toString().toLowerCase().contains("fuck") ||
                Name.toLowerCase().contains("hole") ||
                Name.toLowerCase().contains("boner") ||
                Name.toLowerCase().contains("blowjob") ||
                Name.toLowerCase().contains("blow job") ||
                Name.toLowerCase().contains("bitch") ||
                Name.toLowerCase().contains("tit") ||
                Name.toLowerCase().contains("bastard") ||
                Name.toLowerCase().contains("camel toe") ||
                Name.toLowerCase().contains("choad") ||
                Name.toLowerCase().contains("chode") ||
                Name.toLowerCase().contains("sucker") ||
                Name.toLowerCase().contains("coochie") ||
                Name.toLowerCase().contains("coochy") ||
                Name.toLowerCase().contains("cooter") ||
                Name.toLowerCase().contains("cum") ||
                Name.toLowerCase().contains("slut") ||
                Name.toLowerCase().contains("cunnie") ||
                Name.toLowerCase().contains("cunnilingus") ||
                Name.toLowerCase().contains("cunt") ||
                Name.toLowerCase().contains("dick") ||
                Name.toLowerCase().contains("dildo") ||
                Name.toLowerCase().contains("doochbag") ||
                Name.toLowerCase().contains("douche") ||
                Name.toLowerCase().contains("dyke") ||
                Name.toLowerCase().contains("fag") ||
                Name.toLowerCase().contains("fellatio") ||
                Name.toLowerCase().contains("feltch") ||
                Name.toLowerCase().contains("butt") ||
                Name.toLowerCase().contains("gay") ||
                Name.toLowerCase().contains("genital") ||
                Name.toLowerCase().contains("penis") ||
                Name.toLowerCase().contains("handjob") ||
                Name.toLowerCase().contains("hoe") ||
                Name.toLowerCase().contains("sex") ||
                Name.toLowerCase().contains("jizz") ||
                Name.toLowerCase().contains("kooch") ||
                Name.toLowerCase().contains("kootch") ||
                Name.toLowerCase().contains("kunt") ||
                Name.toLowerCase().contains("lesbo") ||
                Name.toLowerCase().contains("lesbian") ||
                Name.toLowerCase().contains("muff") ||
                Name.toLowerCase().contains("vagina") ||
                Name.toLowerCase().contains("pussy") ||
                Name.toLowerCase().contains("pussies") ||
                Name.toLowerCase().contains("poon") ||
                Name.toLowerCase().contains("piss") ||
                Name.toLowerCase().contains("arse") ||
                Name.toLowerCase().contains("erection") ||
                Name.toLowerCase().contains("rimjob") ||
                Name.toLowerCase().contains("scrote") ||
                Name.toLowerCase().contains("snatch") ||
                Name.toLowerCase().contains("whore") ||
                Name.toLowerCase().contains("wank") ||
                Name.toLowerCase().contains("tard") ||
                Name.toLowerCase().contains("testicle") ||
                Name.toLowerCase().contains("twat") ||
                Name.toLowerCase().contains("boob") ||
                Name.toLowerCase().contains("wiener") ||
                Name.toLowerCase().contains("spank") ||
                Name.toLowerCase().contains("http") ||
                Name.toLowerCase().contains("www") ||
                Name.toLowerCase().contains("ass") ||
                Name.toLowerCase().contains("porn") ||
                Name.toLowerCase().contains("breast") ||
                Name.toLowerCase().contains("gand"))
        {

            edt.setText("");
            edt.setError(resources.getString(R.string.nameError2));

        }

        else if (TextUtils.isEmpty(Story)) {

            //edt.setError("Please Enter Your Name");
            edt1.setError(resources.getString(R.string.myStory_exp));
            //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

        }
        else {
            SendQuoteToServer(Name, Story, date,token);
        }

    }

    public void SendQuoteToServer(final String Name,final String Story, final String Time,final String Token){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("Name", Name));
                nameValuePairs.add(new BasicNameValuePair("Story", Story));
                nameValuePairs.add(new BasicNameValuePair("Time", Time));
                nameValuePairs.add(new BasicNameValuePair("FSToken", Token));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insert_story.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    // HttpEntity entity = response.getEntity();

                    response.getEntity();

                    alert.dismiss();


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                try {
                    personUtilsList.clear();
                    mAdapter.notifyDataSetChanged();
                    sendRequest();

                } catch (NullPointerException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute( Name,Story,Time,Token);
        alert.dismiss();

    }

}