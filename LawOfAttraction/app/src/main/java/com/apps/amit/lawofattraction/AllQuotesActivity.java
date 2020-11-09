package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.apps.amit.lawofattraction.adapters.AllQuotesAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.utils.ViewAllQuotesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllQuotesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<ViewAllQuotesUtils> personUtilsList;
    ConnectivityManager connMngr;
    Resources resources;
    LinearLayout mainlayout;
    Context context;
    NetworkInfo netInfo;
    RequestQueue rq;
    String value1 = "en";
    String requestURL = "http://innovativelabs.xyz/DailyQuoteShow.php";
    ProgressBar  progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allquotes);


        progressBar = findViewById(R.id.progressBar2);


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
        context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();

        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext()," Refreshing ", Toast.LENGTH_SHORT).show();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

            netInfo = connMngr.getActiveNetworkInfo();
        }


        if(!(netInfo!=null && netInfo.isConnected()))
        {
            Toast.makeText(getApplicationContext()," Not Internet..... ", Toast.LENGTH_SHORT).show();

        }


            rq = Volley.newRequestQueue(this);
            progressBar.setVisibility(View.VISIBLE);

            recyclerView =  findViewById(R.id.recylcerView2);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);

            personUtilsList = new ArrayList<>();

            sendRequest();


    }


    public void sendRequest(){

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( requestURL,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.INVISIBLE);

                for(int i = 0; i < response.length(); i++){

                    ViewAllQuotesUtils personUtils = new ViewAllQuotesUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setPersonFirstName(jsonObject.getString("Quote"));
                        personUtils.setPersonLastName(jsonObject.getString("Likes"));
                        personUtils.setJobProfile(jsonObject.getString("Shares"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    personUtilsList.add(personUtils);
                }

                mAdapter = new AllQuotesAdapter(AllQuotesActivity.this, personUtilsList);

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
