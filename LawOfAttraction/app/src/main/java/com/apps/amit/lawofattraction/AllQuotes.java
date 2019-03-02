package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AllQuotes extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<DBUtils2> personUtilsList;
    ConnectivityManager connMngr;
    Resources resources;
    Context context;
    NetworkInfo netInfo;
    RequestQueue rq;
    String value1 = "en";
    String requestURL = "http://innovativelabs.xyz/DailyQuoteShow.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allquotes);



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

                for(int i = 0; i < response.length(); i++){

                    DBUtils2 personUtils = new DBUtils2();

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

                mAdapter = new iquote(AllQuotes.this, personUtilsList);

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
