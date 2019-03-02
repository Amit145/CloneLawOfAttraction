package com.apps.amit.lawofattraction;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Music extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Resources resources;
    String musicURL;
    DownloadManager downloadManager;
    final int REQUEST_WRITE_STORAGE_REQUEST_CODE=0;
    String path="";
    LinearLayout l1,inter;
    ImageView b1;
    WebView mywebview;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Cursor cursor;
    ProgressBar bar;

    ConnectivityManager connMngr;
    NetworkInfo netInfo;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mywebview.destroy();
        interstitial.setAdListener(null);
        this.finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mywebview.destroy();
        //this.finish();

        interstitial.setAdListener(null);
    }

    @Override
    protected void onStop() {
        super.onStop();

        interstitial.setAdListener(null);
    }

    private InterstitialAd interstitial;

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
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

             TextView title,subTitle;
             Button saveButton;

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_music);

            connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

                netInfo = connMngr.getActiveNetworkInfo();
            }
            SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

            //Store selected language in a Variable called value
            final String value1 = pref.getString("language","en");

            Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
            resources = context.getResources();

            musicURL = "http://innovativelabs.xyz/Scripts/"+value1+"_music.php";


            saveButton = findViewById(R.id.saveOffButton);
            title = findViewById(R.id.saveOffTitle);
            subTitle = findViewById(R.id.saveoffsubTitle);

            saveButton.setText(resources.getString(R.string.musicButtonSave));
            title.setText(resources.getString(R.string.musicSaveOffTitle));
            subTitle.setText(resources.getString(R.string.musicsaveoffsubTitle));

            //checkFile();

            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/"+".LawOfAttractionAffirms"+"/"+ "1.mp3");
            if(file.exists())
            {
                //Toast.makeText(getApplicationContext(),"fILE PRESENT : ",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Music.this, offlineMusic.class);
                //intent.putExtra("isButtonClicked",true);
                startActivity(intent);

            }
            else
            {
                // Toast.makeText(getApplicationContext(),"fILE Not present : ",Toast.LENGTH_LONG).show();

                requestAppPermissions();


            }


            mywebview =  findViewById(R.id.webView1);
            l1 = findViewById(R.id.layoutshare);

            inter =  findViewById(R.id.internet);
            bar =  findViewById(R.id.progressBar);

            AdRequest adRequest = new AdRequest.Builder().build();

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

            //b1.setVisibility(View.INVISIBLE);

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
                            Intent i = new Intent(getApplicationContext(), Music.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Music.this.finish();
                            startActivity(i);
                        }
                    },2000);
                }
            });

            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                //l1.setVisibility(View.VISIBLE);
                //b1.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(musicURL);
            }
            else
            {




                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                //b1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }








        }catch (OutOfMemoryError e)
        {
           // Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            System.gc();

        } catch (Exception e)
        {
            //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            System.gc();
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
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.musicInternetMsg), Toast.LENGTH_SHORT).show();
                        mywebview.setVisibility(View.INVISIBLE);
                        l1.setVisibility(View.INVISIBLE);
                        inter.setVisibility(View.VISIBLE);
                        //b1.setVisibility(View.INVISIBLE);
                        // Toast.makeText(getApplicationContext(), "Timed Out ...", Toast.LENGTH_SHORT).show();

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
            //b1.setVisibility(View.VISIBLE);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    //share.setPackage("com.whatsapp");
                    share.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.musicShare)+"\n-------------------------\nhttps://play.google.com/store/apps/details?id=com.apps.amit.lawofattractionpro");
                    try {
                        startActivity(Intent.createChooser(share,resources.getString(R.string.chooseToShare)));

                    } catch (android.content.ActivityNotFoundException ex) {
                        //Toast.makeText(this, "Error".toString(), Toast.LENGTH_SHORT);
                    }
                }
            });
            //Toast.makeText(getApplicationContext(), "Not", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            bar.setVisibility(View.INVISIBLE);
           // Toast.makeText(getApplicationContext(), "Your Internet is not working fine... Please check your Internet", Toast.LENGTH_SHORT).show();
            mywebview.setVisibility(View.INVISIBLE);
            l1.setVisibility(View.INVISIBLE);
            inter.setVisibility(View.VISIBLE);

        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(netInfo!=null && netInfo.isConnected() && netInfo.isAvailable()) {


                mywebview.setVisibility(View.VISIBLE);
                //l1.setVisibility(View.VISIBLE);
                //b1.setVisibility(View.VISIBLE);

                inter.setVisibility(View.INVISIBLE);

                mywebview.setWebViewClient(new MyAppWebViewClient());
                mywebview.getSettings().setJavaScriptEnabled(true);
                mywebview.loadUrl(musicURL);
            }
            else
            {


                view.loadUrl(url);

                bar.setVisibility(View.INVISIBLE);
                mywebview.setVisibility(View.INVISIBLE);
                l1.setVisibility(View.INVISIBLE);
                //b1.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
            }

            return true;
        }
    }


    public void saveOffline(View view) {

        view.startAnimation(buttonClick);

        Toast.makeText(this, resources.getString(R.string.musicSaveOff), Toast.LENGTH_SHORT).show();

        startDownload();


    }

    private void startDownload() {

        String url = "http://innovativelabs.xyz/Mp3/1.mp3";

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        final DownloadManager.Request request = new DownloadManager.Request(uri);


        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ ".LawOfAttractionAffirms"+"/");

        boolean success = true;

        if(!folder.exists())
        {
            success = folder.mkdirs();
        }

        if (success) {
            // Do something on success

            //  Toast.makeText(getApplicationContext(), "Already Folder Created....", Toast.LENGTH_LONG).show();
            path = Environment.getExternalStorageDirectory().getPath()+"/"+".LawOfAttractionAffirms"+"/"+ "1.mp3";

            request.setDestinationInExternalPublicDir(".LawOfAttractionAffirms","1.mp3");



        }

//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        final Long reference = downloadManager.enqueue(request);


        final ProgressDialog progressBarDialog= new ProgressDialog(this);
        progressBarDialog.setTitle(resources.getString(R.string.musicSaveOff));

        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setCancelable(false);
        progressBarDialog.setButton(DialogInterface.BUTTON_NEGATIVE, resources.getString(R.string.myStory_dialogCancel),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,
                                int whichButton){

                downloadManager.remove(reference);

                //oast.makeText(this, "Saving Offline", Toast.LENGTH_SHORT).show();

                // Toast.makeText(getBaseContext(),
                //       "OK clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        progressBarDialog.setProgress(0);

        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                while (downloading) try {
                    {

                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(reference); //filter by id which you have receieved when reqesting download from download manager

                        if(manager!=null  ){

                            cursor = manager.query(q);
                        }


                        if( cursor != null && cursor.moveToFirst() ){

                        int bytes_downloaded = cursor.getInt(cursor
                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                        }

                        final int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                progressBarDialog.setProgress( dl_progress);

                                if(dl_progress == 100)
                                {

                                    progressBarDialog.hide();

                                    Toast.makeText(getApplicationContext(),resources.getString(R.string.done_text),Toast.LENGTH_LONG).show();

                                    //.startAnimation(buttonClick);
                                    Intent art1 = new Intent(getApplicationContext(), offlineMusic.class);
                                    //art1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(art1);

                                    //offStatus.setText(" Click Below to open Music in Offline mode");

                                    //offlineButton.setText(" Open Offline Music");




                                }

                            }
                        });

                        // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                        cursor.close();

                        } else {

                            if(cursor!=null) {
                                cursor.close();
                            }
                        }
                    }
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();


                }

            }
        }).start();


        //show the dialog
        progressBarDialog.show();

    }



    // DownloadFileAsync download = new DownloadFileAsync();
    // download.execute(url);


    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

}
