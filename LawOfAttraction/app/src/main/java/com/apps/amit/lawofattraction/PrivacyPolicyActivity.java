package com.apps.amit.lawofattraction;

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
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;

public class PrivacyPolicyActivity extends AppCompatActivity {


    LinearLayout l1,inter;
    ImageView b1;
    TextView text1;
    WebView mywebview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Resources resources;

    public static final String USER_LANGUAGE = "UserLang";
    public static final String LANGUAGE = "language";
    String webUrl = "http://innovativelabs.xyz/privacy_policy.html";
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

        if((keyCode == KeyEvent.KEYCODE_BACK) && mywebview.canGoBack())
        {
            mywebview.goBack();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lawtips);

            connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo =connMngr.getActiveNetworkInfo();


            mywebview = (WebView) findViewById(R.id.webView1);
            l1 = (LinearLayout)findViewById(R.id.layoutshare);



            inter = (LinearLayout) findViewById(R.id.internet);
            bar = (ProgressBar) findViewById(R.id.progressBar);

            SharedPreferences pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value = pref.getString(LANGUAGE,"en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value);
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
                    Toast.makeText(getApplicationContext(),resources.getString(R.string.loading_msg), Toast.LENGTH_SHORT).show();
                    ( new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Intent i = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PrivacyPolicyActivity.this.finish();
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
                mywebview.loadUrl(webUrl);
            }
            else
            {




                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }








        }catch (OutOfMemoryError e)
        {

            Log.e(e.getMessage(),e.getMessage());
        }
    }

    class MyAppWebViewClient extends WebViewClient{

        SharedPreferences pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

        //Store selected language in a Variable called value
         String value = pref.getString(LANGUAGE,"en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        Resources resources = context.getResources();

        boolean timeout;
        public MyAppWebViewClient() {
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
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError), Toast.LENGTH_SHORT).show();
                        mywebview.setVisibility(View.INVISIBLE);
                        l1.setVisibility(View.INVISIBLE);
                        inter.setVisibility(View.VISIBLE);
                        text1 = (TextView) findViewById(R.id.text);
                        text1.setText(resources.getString(R.string.refresh_text));

                    }
                }
            };

            Handler myHandler = new Handler(Looper.myLooper());
            myHandler.postDelayed(run, 20000);

        }




        @Override
        public void onPageFinished(WebView view, String url) {
            pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

            //Store selected language in a Variable called value
            value = pref.getString(LANGUAGE,"en");

            context = LocaleHelper.setLocale(getApplicationContext(), value);
            resources = context.getResources();

            super.onPageFinished(view, url);
            bar.setVisibility(View.INVISIBLE);
            timeout = false;
            b1 = (ImageView) findViewById(R.id.share);
            l1.setVisibility(View.VISIBLE);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.activityQuote_quoteshare)+" https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
                    try {
                        startActivity(Intent.createChooser(share,resources.getString(R.string.chooseToShare)));

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
            text1 = (TextView) findViewById(R.id.text);
            text1.setText(resources.getString(R.string.refresh_text));
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

            //Store selected language in a Variable called value
            value = pref.getString(LANGUAGE,"en");

            context = LocaleHelper.setLocale(getApplicationContext(), value);
            resources = context.getResources();

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(webUrl);
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


}
