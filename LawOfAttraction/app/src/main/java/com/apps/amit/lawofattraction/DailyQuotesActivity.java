package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class DailyQuotesActivity extends AppCompatActivity {

    TextView quote;
    TextView like;
    TextView share;
    TextView qlike;
    TextView views;
    TextView qshare;
    TextView qViews;
    TextView net;
    TextView def;
    TextView allq;
    TextView title;
    RequestQueue requestQueue;
    Button b1;
    int dummy = 99;
    AlertDialog alert;
    Resources resources;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout l1;
    LinearLayout l2;
    LinearLayout l3;
    LinearLayout activity;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    boolean flag = false;
    String userName;
    String userComment;
    String timee;
    TextView usrName;
    TextView usrQuote;
    EditText edt, edt1;
    String quoted;
    String quoteViews;
    String liked;
    String shared;
    ConnectivityManager connMngr;
    NetworkInfo netInfo;
    String value = "en";
    List < String > colorList = new ArrayList < > ();
    public static final String QUOTE_PREFIX = "Quote";
    private InterstitialAd interstitialAd;
    public static final String TAG = DailyQuotesActivity.class.getSimpleName();
    ProgressBar progressBar;
    AnimationDrawable animationDrawable;

    @Override
    protected void onDestroy() {

        super.onDestroy();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (interstitialAd.isAdInvalidated()) {
            return;
        }

        if (Boolean.TRUE.equals(flag) && interstitialAd.isAdLoaded()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    interstitialAd.show();
                }
            }, 1000);
        }

        Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(art1);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);

        l1 = findViewById(R.id.quotbox);
        l2 = findViewById(R.id.quotcnt);
        l3 = findViewById(R.id.quotcnt1);
        activity = findViewById(R.id.quoteLayout);

        title = findViewById(R.id.qtext);
        b1 = findViewById(R.id.quoteSend);
        net = findViewById(R.id.netin);
        def = findViewById(R.id.def);
        allq = findViewById(R.id.allquo);
        progressBar = findViewById(R.id.progressBar2);

        animationDrawable = (AnimationDrawable) activity.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        colorList.add("#262A36");

        ButterKnife.bind(this);

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        title.setText(resources.getString(R.string.activityQuote_text1));
        allq.setText(resources.getString(R.string.activityQuote_text5));
        def.setText(resources.getString(R.string.activityQuote_text2));
        b1.setText(resources.getString(R.string.activityQuote_text4));
        net.setText(resources.getString(R.string.activityQuote_text3));


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
                        Intent i = new Intent(getApplicationContext(), DailyQuotesActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        DailyQuotesActivity.this.finish();
                        startActivity(i);
                    }
                }, 2000);
            }
        });

        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMngr != null && connMngr.getActiveNetworkInfo() != null) {

            netInfo = connMngr.getActiveNetworkInfo();
        }

        if (netInfo != null && netInfo.isConnected()) {
            quote = findViewById(R.id.quotetxt);
            like = findViewById(R.id.quotelike);
            views = findViewById(R.id.quoteView);
            share = findViewById(R.id.quoteshare);
            qlike = findViewById(R.id.qlike);
            qViews = findViewById(R.id.qViews);
            qshare = findViewById(R.id.qshare);
            qlike.setText(resources.getString(R.string.LIKE_text));
            qshare.setText(resources.getString(R.string.SHARE_text));

            qViews.setEnabled(false);

            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            b1.setVisibility(View.VISIBLE);
            def.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            requestQueue = Volley.newRequestQueue(this);

            allq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    view.startAnimation(buttonClick);
                    Intent art1 = new Intent(getApplicationContext(), AllQuotesActivity.class);
                    startActivity(art1);
                }
            });

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(DailyQuotesActivity.this);
                    LayoutInflater inflater = DailyQuotesActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.quotdialog_options, null);
                    builder.setCancelable(false);
                    builder.setMessage(resources.getString(R.string.activityQuote_share));

                    builder.setView(dialogView);
                    alert = builder.create();
                    alert.show();

                    edt = dialogView.findViewById(R.id.username);
                    edt1 = dialogView.findViewById(R.id.comments);

                    usrName = dialogView.findViewById(R.id.name);
                    usrQuote = dialogView.findViewById(R.id.quote);

                    Button submit = dialogView.findViewById(R.id.btnsubmit);
                    Button cancel = dialogView.findViewById(R.id.btncancel);

                    b1.setText(resources.getString(R.string.activityQuote_text4));
                    usrName.setText(resources.getString(R.string.subUniverse_dialogName));
                    usrQuote.setText(resources.getString(R.string.activityQuote_quoteText));
                    submit.setText(resources.getString(R.string.subUniverse_dialogSubmit));
                    cancel.setText(resources.getString(R.string.subUniverse_dialogCancel));

                    SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
                    String naam = sp1.getString("userName", "");

                    if ((naam != null && naam.equalsIgnoreCase(""))) {
                        edt.setText("");

                    } else {

                        edt.setText(naam);

                    }

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(buttonClick);

                            if (netInfo != null && netInfo.isConnected()) {
                                GetDataFromEditText(value);
                            } else {
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
        } else {
            //Not connected to Internet
            l1.setVisibility(View.INVISIBLE);
            l2.setVisibility(View.INVISIBLE);
            l3.setVisibility(View.INVISIBLE);

            b1.setVisibility(View.INVISIBLE);
            allq.setVisibility(View.INVISIBLE);
            def.setVisibility(View.INVISIBLE);
            net.setText(resources.getString(R.string.refresh_text));

            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
        }

        interstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), getString(R.string.facebook_interstitial_id));
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener((new DailyQuotesActivity.InterstitialListener())).build());


    }

    private class InterstitialListener implements InterstitialAdExtendedListener {

        @Override
        public void onInterstitialActivityDestroyed() {

            Log.d(TAG, "Interstitial ad onInterstitialActivityDestroyed!");
        }

        @Override
        public void onInterstitialDisplayed(Ad ad) {

            Log.d(TAG, "Interstitial ad InterstitialDisplayed!");
        }

        @Override
        public void onInterstitialDismissed(Ad ad) {

            Log.d(TAG, "Interstitial ad InterstitialDismissed!");
        }

        @Override
        public void onError(Ad ad, AdError adError) {

            Log.d(TAG, "Interstitial ad onError! "+adError.getErrorMessage());
        }

        @Override
        public void onAdLoaded(Ad ad) {

            Log.d(TAG, "Interstitial ad Loaded!");
            flag=Boolean.TRUE;
        }

        @Override
        public void onAdClicked(Ad ad) {

            Log.d(TAG, "Interstitial ad AdClicked!");
        }

        @Override
        public void onLoggingImpression(Ad ad) {

            Log.d(TAG, "Interstitial ad LoggingImpression!");
        }

        @Override
        public void onRewardedAdCompleted() {
            Log.d(TAG, "Interstitial ad RewardedAdCompleted!");
        }

        @Override
        public void onRewardedAdServerSucceeded() {

            Log.d(TAG, "Interstitial ad RewardedAdServerSucceeded!");
        }

        @Override
        public void onRewardedAdServerFailed() {

            Log.d(TAG, "Interstitial ad RewardedAdServerFailed!");
        }
    }


    public void GetDataFromEditText(String value) {

        userName = edt.getText().toString();
        userComment = edt1.getText().toString();
        timee = DateFormat.getDateTimeInstance().format(new Date());
        SharedPreferences timerEnable = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putString("userName", userName);
        editor.apply();

        checkEditText(userName, userComment, value);

    }

    public void checkEditText(String checkName, String checkDesc, String value) {

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();
        if (netInfo != null && netInfo.isConnected()) {

            if (TextUtils.isEmpty(checkName)) {

                edt.setError(resources.getString(R.string.enterName));

            } else if (TextUtils.isEmpty(checkDesc)) {

                edt1.setError(resources.getString(R.string.activityQuote_quote));

            } else if (edt.getText().length() >= 25) {
                edt.setError(resources.getString(R.string.nameError));

            } else if (edt.getText().length() < 3) {

                edt.setError(resources.getString(R.string.nameError1));

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


            } else if (edt1.length() > 131) {

                edt1.setError(resources.getString(R.string.activityQuote_quoteLimit));

            } else {

                sendUserQuoteToServer(userName, userComment, timee);
            }
        } else {

            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();


        }
    }

    private void sendUserQuoteToServer(final String userName, final String userComment, final String timee) {

        String url = "http://www.innovativelabs.xyz/insertQuote.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {

                        alert.dismiss();
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.activityQuote_quoteThank), Toast.LENGTH_LONG).show();
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
            protected Map < String, String > getParams() {
                Map < String, String > params = new HashMap < > ();
                params.put("Name", userName);
                params.put(QUOTE_PREFIX, userComment);
                params.put("Time", timee);

                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    private void getSqlDetails() {

        ButterKnife.bind(this);

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        String url = "http://www.innovativelabs.xyz/showQuotes.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.INVISIBLE);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                quoted = jsonobject.getString(QUOTE_PREFIX);
                                liked = jsonobject.getString("Likes");
                                shared = jsonobject.getString("Shares");
                                quoteViews = jsonobject.getString("Views");

                                String quotedText = " \" " + quoted + " \"";
                                quote.setText(quotedText);
                                like.setText(liked);
                                share.setText(shared);
                                views.setText(quoteViews);

                                //updateViews
                                updateViewsToServer(quoted,quoteViews);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError), Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        requestQueue.add(stringRequest);
    }



    public void quolike(View view) {

        ButterKnife.bind(this);

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        int n = 0;

        try {
            n = Integer.parseInt(liked) + 1;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError), Toast.LENGTH_LONG).show();
        }

        like.setText(String.valueOf(n));
        view.startAnimation(buttonClick);
        qlike.setText(resources.getString(R.string.LIKED_text));
        qlike.setEnabled(false);

        sendLikesToServer(liked, quoted);

    }

    private void updateViewsToServer(final String quote, final String views) {

        String url = "http://www.innovativelabs.xyz/insertQuoteViews.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("uResponse", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("sResponse", ""+error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map < String, String > getParams() {
                Map < String, String > params = new HashMap < > ();


                int n = 0;

                try {
                    n = Integer.parseInt(views) + 1;
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), resources.getString(R.string.nwError), Toast.LENGTH_LONG).show();
                }

                params.put("Views", String.valueOf(n));
                params.put(QUOTE_PREFIX, quote);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    private void sendLikesToServer(final String liked, final String quoted) {

        String url = "http://www.innovativelabs.xyz/insertLikes.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener < String > () {
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
            protected Map < String, String > getParams() {
                Map < String, String > params = new HashMap < > ();
                params.put("Likes", liked);
                params.put(QUOTE_PREFIX, quoted);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void quoshare(View view) {

        ButterKnife.bind(this);

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        resources = context.getResources();

        view.startAnimation(buttonClick);
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, quoted + " \n ---------------------------\n " + resources.getString(R.string.activityQuote_quoteshare) + " https://play.google.com/store/apps/details?id=com.apps.amit.lawofattraction");
        try {
            startActivity(Intent.createChooser(intentShare, resources.getString(R.string.chooseToShare)));
            sendSharesToServer(shared, quoted);
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getMessage());
        }
    }

    private void sendSharesToServer(final String shares, final String quote) {

        String url = "http://www.innovativelabs.xyz/insertShares.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener < String > () {
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
            protected Map < String, String > getParams() {
                Map < String, String > params = new HashMap < > ();
                params.put("Shares", shares);
                params.put(QUOTE_PREFIX, quote);

                return params;
            }
        };
        requestQueue.add(postRequest);

    }
}