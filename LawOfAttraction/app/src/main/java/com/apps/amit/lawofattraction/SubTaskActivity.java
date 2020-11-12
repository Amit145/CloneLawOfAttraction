package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubTaskActivity extends AppCompatActivity {

    TextView title1;
    TextView tbod;
    TextView taskex;
    TextView doneTASK;
    TextView usrName;
    Button taskLike;
    Button taskDone;
    boolean flag = false;
    int dummy = 99;
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
    LinearLayout mainlayout;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    RequestQueue requestQueue;
    Resources resources;
    static final String UTF_ENCODING = "UTF-8";

    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    public static final String TAG = SubTaskActivity.class.getSimpleName();

    private InterstitialAd interstitialAd;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue=null;
        cover=null;
        taskShares=null;
        this.finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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

        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task);


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


        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();


        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }

        SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        naam = sp1.getString("userName","");

        if(netInfo==null) {

            SubTaskActivity.this.finish();
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

            SharedPreferences firebaseToken = getSharedPreferences("FirebaseToken", SubTaskActivity.MODE_PRIVATE);
            token = firebaseToken.getString("userToken", "");

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
    }


    public void ShareExperience(View view)
    {
        view.startAnimation(buttonClick);

        AlertDialog.Builder builder = new AlertDialog.Builder(SubTaskActivity.this);
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

                            Log.e(resources.getString(R.string.nameError4)," "+error.getMessage());
                        }
                    }
                }

        );

        requestQueue.add(stringRequest);
    }

    public void SendLikesToServer(final String Likes,final String id){

        String url = "http://www.innovativelabs.xyz/insertTaskLikes.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(response, response);
                        SendUserLikesToServer(naam,token,id);
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                params.put("likes", Likes);
                params.put("id", id);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void SendSharesToServer(final String Shares,final String id){

        String url = "http://www.innovativelabs.xyz/insertTaskShares.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(response, response);
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                params.put("shares", Shares);
                params.put("id", id);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void SendViewsToServer(final String Views,final String id){

        String url = "http://www.innovativelabs.xyz/insertTaskViews.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(response, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e( error.getMessage(), ""+error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                params.put("views", Views);
                params.put("id", id);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void SendUserLikesToServer(final String Name,final String Token,final String id){

        String url = "http://www.innovativelabs.xyz/insertUserTaskLikes.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(response, response);
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                params.put("name", Name);
                params.put("token1", Token);
                params.put("id", id);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void SendUserDoneToServer(final String Name,final String Token,final String id){

        String url = "http://www.innovativelabs.xyz/insertUserTaskDone.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
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

                params.put("name", Name);
                params.put("token", Token);
                params.put("id", id);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }


    public void GetDataFromEditText(){

        String userName = edt.getText().toString();
        String storyName = "My experience for task - "+name+": \n\n\t\t"+edt1.getText().toString();
        String currentDate =  DateFormat.getDateTimeInstance().format(new Date());


        SharedPreferences timerEnable = getSharedPreferences("timerEnable", SubTaskActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", userName);
        editor.apply();

        checkEditText(userName,storyName,currentDate,token);

    }

    public void  checkEditText(String userName, String storyName, String currentDate, String token) {


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

        else if (TextUtils.isEmpty(edt1.getText().toString())) {

            edt1.setError(resources.getString(R.string.myStory_exp));

        }

        else {

            SendExpToServer(userName, storyName, currentDate, token);
        }

    }





    public void SendExpToServer(final String Name, final String Story, final String Time, final String token){

        String url = "http://www.innovativelabs.xyz/insert_story.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        Toast.makeText(getApplicationContext(), resources.getString(R.string.done_text), Toast.LENGTH_LONG).show();

                        Intent openIntent = new Intent(getApplicationContext(), MyStoryActivity.class);
                        startActivity(openIntent);
                    }
                },
                new Response.ErrorListener() {
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

                params.put("Name", Name);
                params.put("Story", Story);
                params.put("Time", Time);
                params.put("FSToken",token);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}
