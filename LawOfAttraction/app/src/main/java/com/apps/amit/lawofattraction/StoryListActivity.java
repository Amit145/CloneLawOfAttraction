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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.adapters.StoryAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.utils.StoryUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StoryListActivity extends AppCompatActivity {

    TextView intText;
    RecyclerView recyclerView;
    RecyclerView.Adapter<StoryAdapter.ViewHolder> mAdapter;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;

    ConnectivityManager connMngr;
    NetworkInfo netInfo;

    List<StoryUtils> personUtilsList;
    SwipeRefreshLayout mSwipeRefreshLayout1;

    LinearLayout parent,mainlayout;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);


        parent = findViewById(R.id.taskParent);

        Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    parent.setBackground(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

                    /*
                    Not required
                     */
            }
        });

        progressBar = findViewById(R.id.progressBar2);

        intText = findViewById(R.id.internetText);

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        final Resources resources = context.getResources();

        String requestUrl = "http://www.innovativelabs.xyz/Scripts/"+value1+"_storyData.php";

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
                Toast.makeText(getApplicationContext(),resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout1.setRefreshing(false);
                        Intent i = new Intent(getApplicationContext(), StoryListActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        StoryListActivity.this.finish();
                        startActivity(i);
                    }
                },2000);
            }
        });


        if(netInfo!=null && netInfo.isConnected()) {
            rq = Volley.newRequestQueue(getApplicationContext());


            recyclerView =  findViewById(R.id.recyclerView1);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView.setLayoutManager(layoutManager);
            intText.setVisibility(View.INVISIBLE);

            personUtilsList = new ArrayList<>();
            sendRequest(requestUrl);

        } else

        {

            intText.setVisibility(View.VISIBLE);

            Toast.makeText(this, resources.getString(R.string.noInternet_txt), Toast.LENGTH_SHORT).show();

        }


    }


    public void sendRequest(String requestUrl){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( requestUrl,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.INVISIBLE);

                for(int i = 0; i < response.length(); i++){

                    StoryUtils personUtils = new StoryUtils();

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
                        personUtils.setstoryLink(jsonObject.getString("storyLink"));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);

                }

                mAdapter = new StoryAdapter(getApplicationContext(), personUtilsList);

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



}
