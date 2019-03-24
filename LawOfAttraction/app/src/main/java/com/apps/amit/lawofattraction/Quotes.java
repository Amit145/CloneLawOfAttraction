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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class Quotes extends AppCompatActivity {

    TextView quote,like,share,qlike,qshare,net,def,allq,title;
    RequestQueue requestQueue;
   // private InterstitialAd interstitial;
    Button b1;
     AlertDialog alert;
    Resources resources;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout l1,l2,l3;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    String UserName,UserComment,timee;
    TextView usrName,usrQuote;
    EditText edt,edt1;
    String quoted,liked,shared;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    String value  = "en";
    //SwipeRefreshLayout mSwipeRefreshLayout1;



 @Override
    protected void onDestroy() {
        super.onDestroy();
       
       
        //interstitial.setAdListener(null);
        this.finish();


    }

    @Override
    protected void onStop() {
        super.onStop();
        //interstitial.setAdListener(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //interstitial.setAdListener(null);
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        l1 = findViewById(R.id.quotbox) ;
        l2 = findViewById(R.id.quotcnt) ;
        l3 = findViewById(R.id.quotcnt1) ;

        title=  findViewById(R.id.qtext);
        //subtitle= findViewById(R.id.def);
        b1 =  findViewById(R.id.quoteSend);
        net =  findViewById(R.id.netin);
        def =  findViewById(R.id.def);
        allq =  findViewById(R.id.allquo);



        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //String value = pref.getString("language","en");


        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        title.setText(resources.getString(R.string.activityQuote_text1));
        allq.setText(resources.getString(R.string.activityQuote_text5));
        def.setText(resources.getString(R.string.activityQuote_text2));
        b1.setText(resources.getString(R.string.activityQuote_text4));
        net.setText(resources.getString(R.string.activityQuote_text3));
        //l2.setVisibility(View.INVISIBLE);


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
                        Intent i = new Intent(getApplicationContext(), Quotes.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Quotes.this.finish();
                        startActivity(i);
                    }
                },2000);
            }
        });

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        if(netInfo!=null && netInfo.isConnected())
        {

            quote =  findViewById(R.id.quotetxt);
            like =  findViewById(R.id.quotelike);
            share =  findViewById(R.id.quoteshare);
            qlike =  findViewById(R.id.qlike);
            qshare =  findViewById(R.id.qshare);

            qlike.setText(resources.getString(R.string.LIKE_text));
            qshare.setText(resources.getString(R.string.SHARE_text));

            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);

            //like.setVisibility(View.INVISIBLE);
           // share.setVisibility(View.INVISIBLE);

            b1.setVisibility(View.VISIBLE);
            def.setVisibility(View.VISIBLE);
            requestQueue = Volley.newRequestQueue(this);

            allq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // Toast.makeText(getApplicationContext(), "All Quotes", Toast.LENGTH_LONG).show();

                    view.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), AllQuotes.class);
                    startActivity(art1);
                }
            });

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(Quotes.this);
                    LayoutInflater inflater = Quotes.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.quotdialog_options, null);
                    builder.setCancelable(false);
                    builder.setMessage(resources.getString(R.string.activityQuote_share));

                    builder.setView(dialogView);
                     alert = builder.create();
                    alert.show();

                    edt =  dialogView.findViewById(R.id.username);
                    edt1 =  dialogView.findViewById(R.id.comments);

                    usrName =  dialogView.findViewById(R.id.name);
                    usrQuote =  dialogView.findViewById(R.id.quote);

                    Button submit =  dialogView.findViewById(R.id.btnsubmit);
                    Button cancel =  dialogView.findViewById(R.id.btncancel);

                    b1.setText(resources.getString(R.string.activityQuote_text4));
                    usrName.setText(resources.getString(R.string.subUniverse_dialogName));
                    usrQuote.setText(resources.getString(R.string.activityQuote_quoteText));
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

                            if(netInfo!=null && netInfo.isConnected()) {
                                GetDataFromEditText(value);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
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



            getSqlDetails();
        }
        else
        {
            //Not connected to Internet
            l1.setVisibility(View.INVISIBLE);
            l2.setVisibility(View.INVISIBLE);
            l3.setVisibility(View.INVISIBLE);

            b1.setVisibility(View.INVISIBLE);
            allq.setVisibility(View.INVISIBLE);
            def.setVisibility(View.INVISIBLE);
            net.setText(resources.getString(R.string.refresh_text));
            // img.setVisibility(View.VISIBLE);
            //share.setVisibility(View.INVISIBLE);


            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }


    }

    public void GetDataFromEditText(String value){



        UserName = edt.getText().toString();
        UserComment = edt1.getText().toString();
        timee =  DateFormat.getDateTimeInstance().format(new Date());


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", UserName);
        editor.apply();

        checkEditText(UserName,UserComment,value);

    }

    public void  checkEditText(String checkName,  String checkDesc, String value) {
        //Array list initialize




        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();
        if (netInfo != null && netInfo.isConnected()) {

            //txt.setVisibility(View.INVISIBLE);


            if (TextUtils.isEmpty(checkName)) {

                edt.setError(resources.getString(R.string.enterName));
                //edt1.setError("Please Enter Your Wish");
                //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(checkDesc)) {

                //edt.setError("Please Enter Your Name");
                edt1.setError(resources.getString(R.string.activityQuote_quote));
                //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

            } else if (edt.getText().length() >= 25) {
                edt.setError(resources.getString(R.string.nameError));
                //Toast.makeText(getApplicationContext(), "Name Cannot be more than 25 characters", Toast.LENGTH_LONG).show();

            } else if (edt.getText().length() < 3) {

                edt.setError(resources.getString(R.string.nameError1));
                // Toast.makeText(getApplicationContext(), "Name Cannot be less than 3 characters", Toast.LENGTH_LONG).show();

            } else if (edt.getText().toString().toLowerCase().contains("axwound") ||
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
                    checkName.toLowerCase().contains("gand")) {

                edt.setText("");
                edt.setError(resources.getString(R.string.nameError2));

            } else if (!checkName.matches("[a-zA-Z ]+")) {

                edt.setError(resources.getString(R.string.nameError3));


            } else if (edt1.length()>131) {

                //edt.setError("Please Enter Your Name");
                edt1.setError(resources.getString(R.string.activityQuote_quoteLimit));
                //Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();

            }else {


                SendQuoteToServer(UserName, UserComment, timee);



            }
        } else {

            // img.setVisibility(View.VISIBLE);
           // recyclerView.setVisibility(View.INVISIBLE);


            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();


        }
    }

    private void getSqlDetails() {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();


        String url= "http://www.innovativelabs.xyz/showQuotes.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);



                                 quoted = jsonobject.getString("Quote");
                                liked = jsonobject.getString("Likes");
                                shared = jsonobject.getString("Shares");

                                String quotedText = " \" "+quoted+" \"";
                                quote.setText(quotedText);
                                like.setText(liked);
                                share.setText(shared);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError), Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        requestQueue.add(stringRequest);
    }



    public void quolike(View view) {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();

       // l2.setVisibility(View.VISIBLE);
        //like.setVisibility(View.VISIBLE);
        int n = 0;
        try {
            n = Integer.parseInt(liked) + 1;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
        }

        like.setText(String.valueOf(n));
       // share.setVisibility(View.VISIBLE);
        view.startAnimation(buttonClick);
        qlike.setText(resources.getString(R.string.LIKED_text));
        qlike.setEnabled(false);

        SendLikesToServer(liked,quoted);
            //Toast.makeText(getApplicationContext(), "You liked the quote" , Toast.LENGTH_LONG).show();






        //if(qlike.setText("LIKED");

    }

    public void quoshare(View view) {


        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();


        view.startAnimation(buttonClick);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        //share.setPackage("com.whatsapp");
        share.putExtra(Intent.EXTRA_TEXT, quoted+" \n ---------------------------\n " +resources.getString(R.string.activityQuote_quoteshare)+" https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
        try {
            startActivity(Intent.createChooser(share,resources.getString(R.string.chooseToShare)));
            SendSharesToServer(shared,quoted);

        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(this, "Error".toString(), Toast.LENGTH_SHORT);
        }
    }

   public void SendLikesToServer(final String Likes,final String Quote){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("Likes", Likes));
                nameValuePairs.add(new BasicNameValuePair("Quote", Quote));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertLikes.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);



            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Likes, Quote);
      // getSqlDetails();
    }

    public void SendQuoteToServer(final String Name,final String Quote,final String Time){

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("Name", Name));
                nameValuePairs.add(new BasicNameValuePair("Quote", Quote));
                nameValuePairs.add(new BasicNameValuePair("Time", Time));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertQuote.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);



            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Name, Quote,Time);
        alert.dismiss();
        Toast.makeText(getApplicationContext(),resources.getString(R.string.activityQuote_quoteThank),Toast.LENGTH_LONG).show();
        // getSqlDetails();
    }

    public void SendSharesToServer(final String Shares,final String Quote){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("Shares", Shares));
                nameValuePairs.add(new BasicNameValuePair("Quote", Quote));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertShares.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);



            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Shares, Quote);
        // getSqlDetails();
    }

}
