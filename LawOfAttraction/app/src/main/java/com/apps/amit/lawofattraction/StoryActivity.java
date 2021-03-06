package com.apps.amit.lawofattraction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;
import java.util.Map;

public class StoryActivity extends AppCompatActivity {

    LinearLayout l1,inter;
    ImageView b1;
    boolean flag = false;
    int dummy = 99;
    RequestQueue requestQueue;
    WebView mywebview;
    Resources resources = null;
    String storyLink;
    String views;
    String shares;
    int viewcount;
    int storyid;
    int shareInt;
    ProgressBar bar;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    private InterstitialAd interstitialAd;
    public static final String TAG = StoryActivity.class.getSimpleName();


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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mywebview.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if((keyCode == KeyEvent.KEYCODE_BACK) && mywebview.canGoBack())
        {
            mywebview.goBack();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_story);

            connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

                netInfo = connMngr.getActiveNetworkInfo();
            }

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
            resources = context.getResources();

            requestQueue = Volley.newRequestQueue(this);

            mywebview =  findViewById(R.id.webView1);
            l1 = findViewById(R.id.layoutshare);

            inter =  findViewById(R.id.internet);
            bar =  findViewById(R.id.progressBar);


            if(netInfo!=null && netInfo.isConnected()) {

                Intent result = getIntent();

                if(result.getExtras()!=null)
                {
                    storyLink = result.getExtras().getString("storyLink");
                    shares  = result.getExtras().getString("storyShares");
                    views  = result.getExtras().getString("storyViews");
                    storyid =  result.getExtras().getInt("storyID");

                }

                mywebview.setVisibility(View.VISIBLE);
                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(storyLink);

                try {
                    viewcount = Integer.parseInt(views) + 1;
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
                }
                views = String.valueOf(viewcount);

                sendViewsToServer(views,String.valueOf(storyid));

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
            else
            {

                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }


        }catch (Exception e)
        {
           Log.e(e.getMessage(),String.valueOf(e));

        }

    }

    class MyAppWebViewClient extends WebViewClient{

        boolean timeout;
        private MyAppWebViewClient() {
            timeout = true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            inter.setVisibility(View.INVISIBLE);

            Runnable run = new Runnable()  {
                @Override
                public void run() {

                    if(timeout) {

                        bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.nwError, Toast.LENGTH_SHORT).show();
                        mywebview.setVisibility(View.INVISIBLE);
                        l1.setVisibility(View.INVISIBLE);
                        inter.setVisibility(View.VISIBLE);

                    }
                }
            };

            Handler myHandler = new Handler(Looper.myLooper());
            myHandler.postDelayed(run, 20000);

        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.INVISIBLE);
            timeout = false;
            b1 =  findViewById(R.id.share);
            l1.setVisibility(View.VISIBLE);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.storyShare)+"\n-------------------------\nhttps://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                    try {
                        startActivity(Intent.createChooser(share,resources.getString(R.string.chooseToShare)));

                            shareInt = Integer.parseInt(shares) + 1;

                        shares = String.valueOf(shareInt);

                        sendSharesToServer(shares,String.valueOf(storyid));


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError) , Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            bar.setVisibility(View.INVISIBLE);
            mywebview.setVisibility(View.INVISIBLE);
            l1.setVisibility(View.INVISIBLE);
            inter.setVisibility(View.VISIBLE);

        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {

                mywebview.setVisibility(View.VISIBLE);
                inter.setVisibility(View.INVISIBLE);
                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(storyLink);
            }
            else
            {
                view.loadUrl(url);
                bar.setVisibility(View.INVISIBLE);
                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }


    public void sendViewsToServer(final String Views,final String id){

        String url = "http://www.innovativelabs.xyz/insertStoryViews.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.e(response, response);
                    }
                },
                new Response.ErrorListener()
                {
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
                params.put("views", Views);
                params.put("id", id);

                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    public void sendSharesToServer(final String Shares,final String id){

        String url = "http://www.innovativelabs.xyz/insertStorySharesViews.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                    Log.e(response,response);
                    }
                },
                new Response.ErrorListener()
                {
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
}
