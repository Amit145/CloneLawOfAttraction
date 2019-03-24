package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;
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

public class SubTask extends AppCompatActivity {

    TextView title1;
    TextView tbod;
    TextView taskex;
    TextView doneTASK;
    TextView usrName;
    Button taskLike;
    Button taskDone;
    Button expButton1;
    ImageView cover;
    ImageView taskShares;
    private int taskID;
    private int viewcount;
    String likes;
    String shares;
    String name;
    String views;
    String token;
    String naam;
    String date;
    String taskBody1;
    AlertDialog alert;
    EditText edt;
    EditText edt1;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    RequestQueue requestQueue;
    Resources resources;
    static final String UTF_ENCODING = "UTF-8";

    ConnectivityManager connMngr;
    NetworkInfo netInfo;

    private InterstitialAd interstitial;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue=null;
        cover=null;
        taskShares=null;
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



    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task);


        AdRequest adRequest = new AdRequest.Builder().build();

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

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


        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        naam = sp1.getString("userName","");

        if(netInfo==null) {

            SubTask.this.finish();
            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt) , Toast.LENGTH_LONG).show();


        }

        else {

            Intent result = getIntent();

            if(result.getExtras()!=null) {

                name = result.getExtras().getString("taskTitle");
                date = result.getExtras().getString("takImg");
                taskBody1 = result.getExtras().getString("taskBody");
                likes  = result.getExtras().getString("taskLikes");
                shares  = result.getExtras().getString("taskShares");
                views  = result.getExtras().getString("taskViews");
                taskID =  result.getExtras().getInt("taskID");
            }

            taskLike = findViewById(R.id.taskLikeId);
            taskShares = findViewById(R.id.taskShareId);
            taskDone = findViewById(R.id.taskDoneId);
            doneTASK = findViewById(R.id.doneTaskTitle);

            taskLike.setText(resources.getString(R.string.LIKE_text));
            taskDone.setText(resources.getString(R.string.subTAskDoneQue));
            doneTASK.setText(resources.getString(R.string.subTaskHaveYou));

            expButton1 = findViewById(R.id.expButton);

            taskLike.setText(resources.getString(R.string.LIKE_text));
            taskDone.setText(resources.getString(R.string.subTAskDoneQue));
            doneTASK.setText(resources.getString(R.string.subTaskHaveYou));
            expButton1.setText(resources.getString(R.string.myStory_buttonText));


            expButton1.setVisibility(View.INVISIBLE);
            taskShares.setVisibility(View.VISIBLE);


            taskLike.setVisibility(View.INVISIBLE);

            String taskLikeText = String.valueOf(likes)+" : "+resources.getString(R.string.subTaskLikes);
            taskLike.setText(taskLikeText);


         token  = FirebaseInstanceId.getInstance().getToken();

        requestQueue = Volley.newRequestQueue(this);
        title1 = findViewById(R.id.taskTitle);
        cover = findViewById(R.id.taskCover);
        tbod= findViewById(R.id.tbody);

        title1.setText(name);
        tbod.setText(taskBody1);


            Glide.with(getApplicationContext()).load(date).into(cover);



        getSqlDetails();

        try {
            viewcount = Integer.parseInt(views) + 1;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
        }
        views = String.valueOf(viewcount);

        SendViewsToServer(views,String.valueOf(taskID));


        }

    }

    public void ShareExperience(View view)
    {
        view.startAnimation(buttonClick);

        AlertDialog.Builder builder = new AlertDialog.Builder(SubTask.this);
        final View dialogView = View.inflate(getApplicationContext(),R.layout.taskdialog_options, null);
        builder.setCancelable(false);
        builder.setMessage(resources.getString(R.string.subTaskShareExp)+" "+name);

        builder.setView(dialogView);
        alert = builder.create();
        alert.show();

        edt =  dialogView.findViewById(R.id.username);
        edt1 =  dialogView.findViewById(R.id.comments);

        usrName = dialogView.findViewById(R.id.name);
        taskex =  dialogView.findViewById(R.id.taskexp);

        taskex.setText(resources.getString(R.string.subTaskDailogShare));

        Button submit =  dialogView.findViewById(R.id.btnsubmit);
        Button cancel =  dialogView.findViewById(R.id.btncancel);

        usrName.setText(resources.getString(R.string.myStory_dialogName));
        submit.setText(resources.getString(R.string.myStory_dialogSubmit));
        cancel.setText(resources.getString(R.string.myStory_dialogCancel));

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

    public void taskLikes(View view) {

        view.startAnimation(buttonClick);
        int n = 0;
        try {
            n = Integer.parseInt(likes) + 1;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
        }

        ViewGroup.LayoutParams parms  =  taskLike.getLayoutParams();
        parms.height = 150;
        parms.width = 150 ;
        taskLike.setLayoutParams(parms);
        taskLike.setText(String.valueOf(n));
        taskLike.setEnabled(false);
        taskLike.setBackgroundResource(R.drawable.hearticon);
        taskLike.setTextSize(18f);
        taskLike.setTextColor(Color.parseColor("#ffffff"));


        SendLikesToServer(likes, String.valueOf(taskID));


    }


    public void taskShare(View view) {

        view.startAnimation(buttonClick);

        view.startAnimation(buttonClick);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, " \n ---------------------------\n "+resources.getString(R.string.subTaskShare)+"  https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
        try {
            startActivity(Intent.createChooser(share,resources.getString(R.string.chooseToShare)));
            SendSharesToServer(shares, String.valueOf(taskID));

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
        }



    }


    public void taskDone(View view) {

        view.startAnimation(buttonClick);
        doneTASK.setText(resources.getString(R.string.subTaskCongrats));
        ViewGroup.LayoutParams parms  =  taskDone.getLayoutParams();
        parms.height = 150;
        parms.width = 150 ;
        taskDone.setLayoutParams(parms);
        taskDone.setEnabled(false);
        taskDone.setBackgroundResource(R.drawable.greentick);
        taskDone.setText("");
        taskLike.setVisibility(View.VISIBLE);
        expButton1.setVisibility(View.VISIBLE);
        taskShares.setVisibility(View.VISIBLE);
        SendUserDoneToServer(naam,token,String.valueOf(taskID));


    }

    private void getSqlDetails() {

        String url= "http://www.innovativelabs.xyz/showTaskUserStats.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);



                               String userDoneToken = jsonobject.getString("token");
                                String userLikeToken = jsonobject.getString("token1");
                                int userId = jsonobject.getInt("id");

                                if(userDoneToken.equalsIgnoreCase(token) && userId==taskID)
                                {
                                    doneTASK.setText(resources.getString(R.string.subTaskCongrats));
                                    ViewGroup.LayoutParams parms  =  taskDone.getLayoutParams();
                                    parms.height = 150;
                                    parms.width = 150 ;
                                    taskDone.setLayoutParams(parms);
                                    taskDone.setEnabled(false);
                                    taskDone.setBackgroundResource(R.drawable.greentick);
                                    taskDone.setText("");
                                    taskLike.setVisibility(View.VISIBLE);
                                    expButton1.setVisibility(View.VISIBLE);
                                    taskShares.setVisibility(View.VISIBLE);
                                }

                                if(userLikeToken.equalsIgnoreCase(token)&& userId==taskID)
                                {
                                    int n = 0;
                                    try {
                                        n = Integer.parseInt(likes);
                                    } catch (NumberFormatException e) {

                                        Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
                                    }

                                    ViewGroup.LayoutParams parms  =  taskLike.getLayoutParams();
                                    parms.height = 150;
                                    parms.width = 150 ;
                                    taskLike.setLayoutParams(parms);
                                    taskLike.setText(String.valueOf(n));
                                    taskLike.setEnabled(false);
                                    taskLike.setBackgroundResource(R.drawable.hearticon);
                                    taskLike.setTextSize(18f);
                                    taskLike.setTextColor(Color.parseColor("#ffffff"));
                                }
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

                            Toast.makeText(getApplicationContext(), resources.getString(R.string.nameError4), Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        requestQueue.add(stringRequest);
    }

    public void SendLikesToServer(final String Likes,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("likes", Likes));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertTaskLikes.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

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

                SendUserLikesToServer(naam,token,id);
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Likes, id);
    }

    public void SendSharesToServer(final String Shares,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("shares", Shares));
                nameValuePairs.add(new BasicNameValuePair("id", id));


                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertTaskShares.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Shares, id);
    }

    public void SendViewsToServer(final String Views,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("views", Views));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertTaskViews.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Views, id);
    }

    public void SendUserLikesToServer(final String Name,final String Token,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("name", Name));
                nameValuePairs.add(new BasicNameValuePair("token1", Token));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertUserTaskLikes.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                   response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();


                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Name,Token, id);
    }

    public void SendUserDoneToServer(final String Name,final String Token,final String id){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("name", Name));
                nameValuePairs.add(new BasicNameValuePair("token", Token));
                nameValuePairs.add(new BasicNameValuePair("id", id));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insertUserTaskDone.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                     response.getEntity();


                } catch (ClientProtocolException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.myStory_warn), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Name,Token, id);
    }


    public void GetDataFromEditText(){



        String Name = edt.getText().toString();
        String Story = "My experience for task - "+name+": \n\n\t\t"+edt1.getText().toString();
        String date =  DateFormat.getDateTimeInstance().format(new Date());


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", SubTask.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", Name);
        editor.apply();

        checkEditText(Name,Story,date);

    }

    public void  checkEditText(String Name,String Story,String date) {


        if (TextUtils.isEmpty(Name)) {

            edt.setError(resources.getString(R.string.enterName));

        } else if (!Name.matches("[a-zA-Z ]+")) {

            edt.setError(resources.getString(R.string.nameError3));



        } else if (edt.getText().length()>=25 )
        {
            edt.setError(resources.getString(R.string.nameError));

        }

        else if (edt.getText().length()<3 )
        {

            edt.setError(resources.getString(R.string.nameError1));

        }

        else if (TextUtils.isEmpty(edt1.getText().toString())) {

            edt1.setError(resources.getString(R.string.myStory_exp));

        }

        else {

            SendExpToServer(Name, Story, date);
        }

    }





    public void SendExpToServer(final String Name,final String Story, final String Time){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("Name", Name));
                nameValuePairs.add(new BasicNameValuePair("Story", Story));
                nameValuePairs.add(new BasicNameValuePair("Time", Time));



                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost("http://www.innovativelabs.xyz/insert_story.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,UTF_ENCODING));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();

                    alert.dismiss();


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


                Toast.makeText(getApplicationContext(), resources.getString(R.string.done_text), Toast.LENGTH_LONG).show();

                Intent openIntent = new Intent(getApplicationContext(),experiences.class);
                startActivity(openIntent);


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute( Name,Story,Time);
        alert.dismiss();

    }


}
