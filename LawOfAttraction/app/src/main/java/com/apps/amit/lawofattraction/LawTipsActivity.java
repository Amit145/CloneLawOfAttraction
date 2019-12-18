package com.apps.amit.lawofattraction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import com.apps.amit.lawofattraction.helper.LocaleHelper;

public class LawTipsActivity extends AppCompatActivity {


    LinearLayout l1,inter;
    ImageView b1;
    WebView mywebview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Resources resources;
    String urlTips;

    ProgressBar bar;

    ConnectivityManager connMngr;
    NetworkInfo netInfo;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mywebview.destroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        try {
            if((keyCode == KeyEvent.KEYCODE_BACK) && mywebview.canGoBack())
            {
                mywebview.goBack();
                return  true;
            }
        } catch (NullPointerException e) {

            Log.e(e.getMessage(),e.getMessage());

        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lawtips);

            connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

                netInfo = connMngr.getActiveNetworkInfo();
            }

            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
            resources = context.getResources();

             urlTips = "http://innovativelabs.xyz/Scripts/"+value1+"_tips.php";


            mywebview =  findViewById(R.id.webView1);
            l1 = findViewById(R.id.layoutshare);

            inter =  findViewById(R.id.internet);
            bar =  findViewById(R.id.progressBar);


            mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                    android.R.color.holo_green_dark,
                    android.R.color.holo_orange_dark,
                    android.R.color.holo_blue_dark);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    Toast.makeText(getApplicationContext()," Loading..... ", Toast.LENGTH_SHORT).show();
                    ( new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Intent i = new Intent(getApplicationContext(), LawTipsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            LawTipsActivity.this.finish();
                            startActivity(i);
                        }
                    },2000);
                }
            });

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(urlTips);
            }
            else
            {




                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Not connected to Internet", Toast.LENGTH_LONG).show();
            }








        }catch (OutOfMemoryError e)
        {

            Log.e(e.getMessage(),e.getMessage());
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
                        Toast.makeText(getApplicationContext(), "Your Internet is not working fine... Please check your Internet", Toast.LENGTH_SHORT).show();
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
                    share.putExtra(Intent.EXTRA_TEXT, "Learn how to enhance your manifestation's using the Law of Attraction App.Download Now from Playstore\n-------------------------\nhttps://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                    try {
                        startActivity(Intent.createChooser(share,"Choose Where to share"));

                    } catch (android.content.ActivityNotFoundException ex) {

                        Log.e(ex.getMessage(),ex.getMessage());
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
            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(urlTips);
            }
            else
            {


                view.loadUrl(url);
                bar.setVisibility(View.INVISIBLE);
                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Not connected to Internet", Toast.LENGTH_LONG).show();
            }

            return true;
        }
    }


}
