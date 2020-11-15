package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.amit.lawofattraction.helper.CheckInternetService;
import com.apps.amit.lawofattraction.helper.Constants;
import com.apps.amit.lawofattraction.sqlitedatabase.ActivityTrackerDatabaseHandler;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.ManifestationTrackerUtils;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.apps.amit.lawofattraction.SetReminderActivity.NOTIFICATION_ENABLE;
import static com.apps.amit.lawofattraction.helper.Constants.AFFIRMATION_COUNT;
import static com.apps.amit.lawofattraction.helper.Constants.AFFIRMATION_COUNTER;
import static com.apps.amit.lawofattraction.helper.Constants.AFFIRMATION_DATE;
import static com.apps.amit.lawofattraction.helper.Constants.MANIFESTATION_TYPE;
import static com.apps.amit.lawofattraction.helper.Constants.MANIFESTATION_TYPE_VALUE;
import static com.apps.amit.lawofattraction.helper.Constants.MANIFEST_TYPE;
import static com.apps.amit.lawofattraction.helper.Constants.PERSON_EMAIL;
import static com.apps.amit.lawofattraction.helper.Constants.PERSON_ID;
import static com.apps.amit.lawofattraction.helper.Constants.PERSON_NAME;
import static com.apps.amit.lawofattraction.helper.Constants.PERSON_PHOTO;
import static com.apps.amit.lawofattraction.helper.Constants.REMINDER_STATUS;
import static com.apps.amit.lawofattraction.helper.Constants.SOCIAL_ID;
import static com.apps.amit.lawofattraction.helper.Constants.SYNC_DATE;
import static com.apps.amit.lawofattraction.helper.Constants.TIMER_COUNT;
import static com.apps.amit.lawofattraction.helper.Constants.TIMER_ENABLE;
import static com.apps.amit.lawofattraction.helper.Constants.TIMER_VALUE;

public class LoginActivity extends AppCompatActivity {

    ImageView appLogo;
    TextView titleText;
    LinearLayout profileLinearLayout;
    SignInButton signInButton;
    CallbackManager callbackManager;
    TextView syncStatusText;
    String temp;
    ProgressBar syncBar;
    Button syncButton;
    JSONObject jsonObject;
    boolean flag = false;
    public static final String DAY_COUNTER = "counter";
    private InterstitialAd interstitialAd;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    public void onBackPressed() {
        Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(art1);
    }

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    SharedPreferences sharedpreferences;
    SharedPreferences ftpSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(SOCIAL_ID, Context.MODE_PRIVATE);
        ftpSp = getSharedPreferences("FtpLogin", Context.MODE_PRIVATE);

        if (sharedpreferences.contains(PERSON_ID)) {
            setContentView(R.layout.activity_my_profile);
            profileLinearLayout = findViewById(R.id.profileLinearLayout);

            Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        profileLinearLayout.setBackground(resource);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                /*
                Not Required
                 */
                }
            });

            LinearLayout dashboardLay1 = findViewById(R.id.dashboardLay1);
            LinearLayout dashboardLay2 = findViewById(R.id.dashboardLay2);
            LinearLayout settLinear1 = findViewById(R.id.settLinear1);
            LinearLayout settLinear2 = findViewById(R.id.settLinear2);
            LinearLayout settLinear3 = findViewById(R.id.settLinear3);

            syncBar = findViewById(R.id.syncBar);
            CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
            TextView userProfileName = findViewById(R.id.userProfileName);
            TextView userProfileEmail = findViewById(R.id.userProfileEmail);

            /*
                TextView audioWishCount = findViewById(R.id.audioWishCount);
                TextView audioWishName = findViewById(R.id.audioWishName);
            */

            TextView privateWishCount = findViewById(R.id.privateWishCount);
            TextView privateWishName = findViewById(R.id.privateWishName);
            TextView affirmationCount = findViewById(R.id.affirmationCount);
            TextView affirmationName = findViewById(R.id.affirmationName);
            TextView manifestType = findViewById(R.id.manifestType);
            TextView manifestTypeName = findViewById(R.id.manifestTypeName);
            TextView setTimeCount = findViewById(R.id.setTimeCount);
            TextView setTimeName = findViewById(R.id.setTimeName);
            TextView notificationReminderStatus = findViewById(R.id.notificationReminderStatus);
            TextView notificationReminderName = findViewById(R.id.notificationReminderName);
            TextView loggedInAccountText = findViewById(R.id.loggedInAccountText);
            Button logoutButton = findViewById(R.id.logoutButton);
            syncStatusText = findViewById(R.id.syncStatusText);
            syncButton = findViewById(R.id.syncButton);

            syncBar.setVisibility(View.INVISIBLE);

            WishDataBaseHandler db = new WishDataBaseHandler(getApplicationContext());
            ActivityTrackerDatabaseHandler adb = new ActivityTrackerDatabaseHandler(getApplicationContext());
            List < PrivateWishesUtils > getAllWishes = db.getAllWishes();

            SharedPreferences sp = getSharedPreferences(AFFIRMATION_COUNTER, AffirmationActivity.MODE_PRIVATE);
            SharedPreferences sharedPreferencesManifestationType = getSharedPreferences(MANIFESTATION_TYPE, Exercise1Activity.MODE_PRIVATE);
            SharedPreferences timerValue = getSharedPreferences(REMINDER_STATUS, Exercise2Activity.MODE_PRIVATE);
            SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);

            /*
                List < ManifestationTrackerUtils > getAllLogs = adb.getAllContacts();
                File root = android.os.Environment.getExternalStorageDirectory();
                String path = root.getAbsolutePath() + "/LawOfAttraction/Audios";
                File directory = new File(path);
                final File[] files = directory.listFiles();

                if (files != null) {
                audioWishCount.setText(String.valueOf(files.length));
                }

                audioWishName.setText("Audio Wishes");
             */

            String value = sharedpreferences.getString(PERSON_PHOTO, "");
            if (value.equals("null")) {
                Glide.with(getApplicationContext()).load(R.drawable.lawimg).into(userProfilePic);
                userProfilePic.setBorderWidth(0);
            } else {
                Uri uri = Uri.parse(value);
                Glide.with(getApplicationContext()).load(uri).into(userProfilePic);
            }

            userProfileName.setText(sharedpreferences.getString(PERSON_NAME, ""));
            userProfileEmail.setText(getString(R.string.nav_header_subtitle));

            privateWishCount.setText(String.valueOf(getAllWishes.size()));
            privateWishName.setText("Private Wishes");
            affirmationCount.setText(String.valueOf(sp.getInt(DAY_COUNTER, 0)));
            affirmationName.setText("Affirmation Day");

            if (sharedPreferencesManifestationType.getString(MANIFESTATION_TYPE_VALUE, "").isEmpty()) {
                manifestType.setText("NA");
            } else {
                manifestType.setText(sharedPreferencesManifestationType.getString(MANIFESTATION_TYPE_VALUE, ""));
            }

            manifestTypeName.setText("Manifesting");
            setTimeCount.setText(String.valueOf(timerValue.getInt(TIMER_VALUE, 60)));
            setTimeName.setText("Timer count");

            if (timerEnable.getInt(NOTIFICATION_ENABLE, 1) == 1) {
                notificationReminderStatus.setText("ON");
            } else {
                notificationReminderStatus.setText("OFF");
            }

            notificationReminderName.setText("Reminder");
            loggedInAccountText.setText("Logged in using: \n\n" + sharedpreferences.getString(PERSON_EMAIL, ""));

            if (sharedpreferences.contains(SYNC_DATE)) {
                syncStatusText.setText("Last Sync at: " + sharedpreferences.getString(SYNC_DATE, ""));
            } else {
                syncStatusText.setText("No Sync performed");
            }

            dashboardLay1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent art = new Intent(getApplicationContext(), PrivateWishesActivity.class);
                    startActivity(art);
                }
            });

            dashboardLay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent art = new Intent(getApplicationContext(), AffirmationHome.class);
                    startActivity(art);
                }
            });

            settLinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent art = new Intent(getApplicationContext(), SelectManifestationTypeActivity.class);
                    startActivity(art);
                }
            });

            settLinear2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent art = new Intent(getApplicationContext(), SetTimeActivity.class);
                    startActivity(art);
                }
            });

            settLinear3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent art = new Intent(getApplicationContext(), SetReminderActivity.class);
                    startActivity(art);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {

                CheckInternetService checkInternetService = new CheckInternetService();
                NetworkInfo netInfo = checkInternetService.checkInternetConnection(getApplicationContext());

                if(netInfo!=null && netInfo.isConnected()) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this, new OnCompleteListener < Void > () {@Override
                    public void onComplete(@NonNull Task < Void > task) {

                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(PERSON_ID);
                        editor.remove(SYNC_DATE);
                        editor.putString(PERSON_NAME, "");
                        editor.putString(PERSON_EMAIL, "");
                        editor.putString(PERSON_PHOTO, "");
                        editor.apply();

                        Intent art = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(art);
                    }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
                }
            }
            });

            syncButton.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {

                String val = ftpSp.getString("ftphost","");
                String val1 = ftpSp.getString("ftpuser","");
                String val2 = ftpSp.getString("ftppass","");

                CheckInternetService checkInternetService = new CheckInternetService();
                NetworkInfo netInfo = checkInternetService.checkInternetConnection(getApplicationContext());

                if(netInfo!=null && netInfo.isConnected()) {

                    // Create the InterstitialAd and set the adUnitId.
                    interstitialAd = new InterstitialAd(LoginActivity.this);
                    // Defined in res/values/strings.xml
                    interstitialAd.setAdUnitId(getString(R.string.TestInterstitialAdsBannerGoogle));
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            flag=Boolean.TRUE;
                        }
                    });

                    syncStatusText.setText("Syncing....!");
                    Toast.makeText(getApplicationContext(), "Syncing Data", Toast.LENGTH_LONG).show();

                    try {
                        jsonObject = createJsonFile();
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }

                    syncBar.setVisibility(View.VISIBLE);
                    syncButton.setVisibility(View.INVISIBLE);
                    FTPUpload uploadFTP = new FTPUpload(val, val1, val2);
                    uploadFTP.execute();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(SYNC_DATE, DateFormat.getDateTimeInstance().format(new Date()));
                    editor.apply();

                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
                }
            }
            });
        } else {

            setContentView(R.layout.activity_login);
            profileLinearLayout = findViewById(R.id.profileLinearLayout);

            Glide.with(this).load(R.drawable.starshd).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        profileLinearLayout.setBackground(resource);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                /*
                Not Required
                 */
                }
            });

            appLogo = findViewById(R.id.appLogo);
            signInButton = findViewById(R.id.googleSignInButton);
            titleText = findViewById(R.id.signInTextView);

            Glide.with(getApplicationContext()).load(R.drawable.lawimg).thumbnail(0.1f).fitCenter().into(appLogo);

            // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            signInButton.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View view) {

                CheckInternetService checkInternetService = new CheckInternetService();
                NetworkInfo netInfo = checkInternetService.checkInternetConnection(getApplicationContext());

                if(netInfo!=null && netInfo.isConnected()) {

                    // Create the InterstitialAd and set the adUnitId.
                    interstitialAd = new InterstitialAd(LoginActivity.this);
                    // Defined in res/values/strings.xml
                    interstitialAd.setAdUnitId(getString(R.string.TestInterstitialAdsBannerGoogle));
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    interstitialAd.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdLoaded() {
                            flag=Boolean.TRUE;
                        }
                    });

                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
                }
            }
            });
        }
    }

    private JSONObject createJsonFile() throws JSONException {

        SharedPreferences sharedpreferencesTemp = getSharedPreferences(SOCIAL_ID, Context.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(AFFIRMATION_COUNTER, AffirmationActivity.MODE_PRIVATE);
        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences(MANIFESTATION_TYPE, Exercise1Activity.MODE_PRIVATE);
        SharedPreferences timerValue = getSharedPreferences(REMINDER_STATUS, Exercise2Activity.MODE_PRIVATE);
        SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(PERSON_ID, sharedpreferencesTemp.getString(PERSON_ID, ""));
            jsonObject.put(PERSON_EMAIL, sharedpreferencesTemp.getString(PERSON_EMAIL, ""));
            jsonObject.put(PERSON_NAME, sharedpreferencesTemp.getString(PERSON_NAME, ""));
            jsonObject.put(PERSON_PHOTO, sharedpreferencesTemp.getString(PERSON_PHOTO, ""));
            jsonObject.put(AFFIRMATION_DATE, sp.getString("Time", "NA"));
            jsonObject.put(AFFIRMATION_COUNT, sp.getInt(DAY_COUNTER, 0));
            jsonObject.put(MANIFEST_TYPE, sharedPreferencesManifestationType.getString(MANIFESTATION_TYPE_VALUE, ""));
            jsonObject.put(TIMER_ENABLE, timerEnable.getInt(NOTIFICATION_ENABLE, 1));
            jsonObject.put(TIMER_COUNT, timerValue.getInt(TIMER_VALUE, 60));
            //jsonObject.put("notificationDate", notificationDate);

            WishDataBaseHandler db = new WishDataBaseHandler(getApplicationContext());
            ActivityTrackerDatabaseHandler adb = new ActivityTrackerDatabaseHandler(getApplicationContext());

            List < PrivateWishesUtils > getAllWishes = db.getAllWishes();
            List < ManifestationTrackerUtils > getAllLogs = adb.getAllContacts();

            JSONArray arrayElementOneArray = new JSONArray();
            JSONArray arrayElementTwoArray = new JSONArray();

            for (ManifestationTrackerUtils ws: getAllLogs) {

                JSONObject arrayElementOneArrayElementOne = new JSONObject();

                arrayElementOneArrayElementOne.put("logTitle", ws.getName());
                arrayElementOneArrayElementOne.put("logSubtitle", ws.getPhoneNumber());

                arrayElementOneArray.put(arrayElementOneArrayElementOne);
            }

            for (PrivateWishesUtils ws: getAllWishes) {

                JSONObject arrayElementOneArrayElementOne = new JSONObject();

                arrayElementOneArrayElementOne.put("userName", ws.getUserName());
                arrayElementOneArrayElementOne.put("userWish", ws.getUserWish());
                arrayElementOneArrayElementOne.put("wishDate", ws.getUserDate());

                arrayElementTwoArray.put(arrayElementOneArrayElementOne);
            }

            jsonObject.put("logList", arrayElementOneArray);
            jsonObject.put("pWishList", arrayElementTwoArray);

        } catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static boolean makeDirectories(FTPClient ftpClient, String dirPath)
            throws IOException {

        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir: pathElements) {
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task < GoogleSignInAccount > task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task < GoogleSignInAccount > completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String val = ftpSp.getString("ftphost","");
                String val1 = ftpSp.getString("ftpuser","");
                String val2 = ftpSp.getString("ftppass","");

                SharedPreferences.Editor editor = sharedpreferences.edit();
                String personName = account.getDisplayName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                String personPhoto = String.valueOf(account.getPhotoUrl());
                editor.putString(PERSON_NAME, personName);
                editor.putString(PERSON_ID, personId);
                editor.putString(PERSON_EMAIL, personEmail);
                editor.putString(PERSON_PHOTO, personPhoto);
                editor.apply();

                //Check if user exist
                FTPDownload ftpDownload = new FTPDownload(personId, val, val1, val2);
                ftpDownload.execute();
            }
        } catch(ApiException e) {

            Toast.makeText(getApplicationContext(), "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_LONG).show();
        }
    }

    public class FTPUpload extends AsyncTask < String,
            Void,
            Void > {

        String ftpHostName ;
        String ftpUser ;
        String ftpPass;

        public FTPUpload(String val, String val1, String val2) {
            this.ftpHostName = val;
            this.ftpUser = val1;
            this.ftpPass = val2;
        }

        @Override
        protected Void doInBackground(String...strings) {

            FTPClient con;
            try {
                con = new FTPClient();
                con.connect(ftpHostName);

                if (con.login(ftpUser, ftpPass)) {

                    con.enterLocalPassiveMode(); // important!
                    String jsonFilePath = sharedpreferences.getString("personId", "") + "/JSON";

                    boolean result1 = LoginActivity.makeDirectories(con, jsonFilePath);
                    if (result1) {
                        con.changeWorkingDirectory(jsonFilePath);
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        String str = jsonObject.toString();
                        InputStream is = new ByteArrayInputStream(str.getBytes());
                        con.storeFile(sharedpreferences.getString("personId", "") + ".json", is);
                        is.close();
                    }

                    con.logout();
                    con.disconnect();
                }
            } catch(Exception e) {
                Log.d(Constants.ERROR_TEXT, String.valueOf(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext(), "Completed ! ... Synchronizing Data", Toast.LENGTH_LONG).show();
            syncBar.setVisibility(View.INVISIBLE);
            syncButton.setVisibility(View.VISIBLE);
            syncStatusText.setText("Last Sync at: " + sharedpreferences.getString(SYNC_DATE, ""));

            if (Boolean.TRUE.equals(flag) && interstitialAd.isLoaded()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.show();
                    }
                }, 2000);
            }
        }
    }

    public class FTPDownload extends AsyncTask < String,
            Void,
            Void > {

        AlertDialog alert;
        public String personId;
        String ftpHostName ;
        String ftpUser ;
        String ftpPass;

        public FTPDownload( String personId, String val, String val1, String val2) {
            this.personId = personId;
            this.ftpHostName = val;
            this.ftpUser = val1;
            this.ftpPass = val2;
        }

        @Override
        protected Void doInBackground(String...strings) {

            final String jsonFilePath = personId + "/JSON";
            final String jsonFileName = personId + ".json";
            FTPClient con = new FTPClient();

            Handler handler = new Handler(getApplicationContext().getMainLooper());
            handler.post(new Runnable() {
                public void run() {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Please Wait");
                    builder.setMessage("Loading....");
                    final ProgressBar progressBar = new ProgressBar(LoginActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    progressBar.setLayoutParams(lp);
                    builder.setView(progressBar);
                    alert = builder.create();
                    alert.show();
                }
            });

            try {
                con.connect(ftpHostName);

                if (con.login(ftpUser, ftpPass)) {

                    con.enterLocalPassiveMode();
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    boolean ok = checkDirectoryExists(jsonFilePath, con);

                    if (ok) {

                        final InputStream ok1 = checkFileExists(jsonFileName, con);

                        if (ok1 != null) {

                            InputStreamReader isReader = new InputStreamReader(ok1);
                            BufferedReader reader = new BufferedReader(isReader);
                            StringBuilder sb = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            jsonObject = new JSONObject(sb.toString());

                        }
                    }
                }

                con.logout();
                con.disconnect();

            } catch(Exception e) {
                Log.d(Constants.ERROR_TEXT, String.valueOf(e));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Handler handler = new Handler(getApplicationContext().getMainLooper());
            if (jsonObject != null) {
                handler.post(new Runnable() {
                    public void run() {

                        SharedPreferences sharedpreferences = getSharedPreferences(SOCIAL_ID, Context.MODE_PRIVATE);
                        SharedPreferences sp = getSharedPreferences(AFFIRMATION_COUNTER, Context.MODE_PRIVATE);
                        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences(MANIFESTATION_TYPE, Exercise1Activity.MODE_PRIVATE);
                        SharedPreferences timerValue = getSharedPreferences(REMINDER_STATUS, Exercise2Activity.MODE_PRIVATE);
                        SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);

                        try {
                            SharedPreferences.Editor ed = sharedpreferences.edit();
                            ed.putString(PERSON_ID, jsonObject.getString(PERSON_ID));
                            ed.putString(PERSON_EMAIL, jsonObject.getString(PERSON_EMAIL));
                            ed.putString(PERSON_NAME, jsonObject.getString(PERSON_NAME));
                            ed.putString(PERSON_PHOTO, jsonObject.getString(PERSON_PHOTO));
                            ed.apply();

                            ed = sp.edit();
                            ed.putString("Time", jsonObject.getString(AFFIRMATION_DATE));
                            ed.putInt(DAY_COUNTER, jsonObject.getInt(AFFIRMATION_COUNT));
                            ed.apply();

                            ed = sp.edit();
                            ed.putString("Time", jsonObject.getString(AFFIRMATION_DATE));
                            ed.putInt(DAY_COUNTER, jsonObject.getInt(AFFIRMATION_COUNT));
                            ed.apply();

                            ed = sharedPreferencesManifestationType.edit();
                            ed.putString(MANIFESTATION_TYPE_VALUE, jsonObject.getString(MANIFEST_TYPE));
                            ed.apply();

                            ed = sharedPreferencesManifestationType.edit();
                            ed.putString(MANIFESTATION_TYPE_VALUE, jsonObject.getString(MANIFEST_TYPE));
                            ed.apply();

                            ed = timerEnable.edit();
                            ed.putString(TIMER_ENABLE, jsonObject.getString(TIMER_ENABLE));
                            ed.apply();

                            ed = timerEnable.edit();
                            ed.putString(TIMER_ENABLE, jsonObject.getString(TIMER_ENABLE));
                            ed.apply();

                            ed = timerValue.edit();
                            ed.putInt(TIMER_VALUE, Integer.parseInt(jsonObject.getString(TIMER_COUNT)));
                            ed.apply();

                            ed = timerValue.edit();
                            ed.putInt(TIMER_VALUE, Integer.parseInt(jsonObject.getString(TIMER_COUNT)));
                            ed.apply();

                            JSONArray logs = jsonObject.getJSONArray("logList");
                            JSONArray pWishes = jsonObject.getJSONArray("pWishList");

                            for (int i = 0; i < logs.length(); i++) {
                                JSONObject jsonObject1 = logs.getJSONObject(i);

                                ActivityTrackerDatabaseHandler db = new ActivityTrackerDatabaseHandler(getApplicationContext());
                                db.addContact(new ManifestationTrackerUtils(jsonObject1.getString("logTitle"), jsonObject1.getString("logSubtitle")));
                            }

                            for (int i = 0; i < pWishes.length(); i++) {
                                JSONObject jsonObject1 = pWishes.getJSONObject(i);
                                //Remove Json Duplicates
                                WishDataBaseHandler db = new WishDataBaseHandler(getApplicationContext());

                                db.addWish(new PrivateWishesUtils(jsonObject1.getString("userName"), jsonObject1.getString("userWish"), jsonObject1.getString("wishDate")));
                            }

                            WishDataBaseHandler db = new WishDataBaseHandler(getApplicationContext());
                            db.removeDuplicates();

                            ActivityTrackerDatabaseHandler db1 = new ActivityTrackerDatabaseHandler(getApplicationContext());
                            db1.removeDuplicates();

                        } catch(Exception e) {
                            Log.d(Constants.ERROR_TEXT, String.valueOf(e));
                        }

                        alert.dismiss();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                        //load Ad
                        if (Boolean.TRUE.equals(flag) && interstitialAd.isLoaded()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    interstitialAd.show();
                                }
                            }, 2000);
                        }

                        Intent art = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(art);
                    }
                });
            } else {
                handler.post(new Runnable() {
                    public void run() {

                        alert.dismiss();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        Intent art = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(art);
                    }
                });
            }
        }
    }

    boolean checkDirectoryExists(String dirPath, FTPClient con) throws IOException {
        con.changeWorkingDirectory(dirPath);
        int returnCode = con.getReplyCode();
        return returnCode != 550;
    }

    InputStream checkFileExists(String filePath, FTPClient con) throws IOException {
        InputStream inputStream = con.retrieveFileStream(filePath);
        con.completePendingCommand();
        return inputStream;
    }
}