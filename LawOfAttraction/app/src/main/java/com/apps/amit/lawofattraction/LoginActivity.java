package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    ImageView appLogo;
    TextView titleText;
    LinearLayout profileLinearLayout;
    TextView skipLoginText;
    SignInButton signInButton;
    CallbackManager callbackManager;
    TextView syncStatusText;
    String temp;
    ProgressBar syncBar;
    Button syncButton;
    JSONObject jsonObject;
    boolean flag = false;
    private InterstitialAd interstitialAd;

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

        sharedpreferences = getSharedPreferences("SocialAccount", Context.MODE_PRIVATE);
        ftpSp = getSharedPreferences("FtpLogin", Context.MODE_PRIVATE);

        if (sharedpreferences.contains("personId")) {
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

            SharedPreferences sp = getSharedPreferences("Affirmation_Counter", AffirmationActivity.MODE_PRIVATE);
            SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);
            SharedPreferences timerValue = getSharedPreferences("your_prefs", Exercise2Activity.MODE_PRIVATE);
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

            String value = sharedpreferences.getString("personPhoto", "");
            if (value.equals("null")) {
                Glide.with(getApplicationContext()).load(R.drawable.lawimg).into(userProfilePic);
                userProfilePic.setBorderWidth(0);
            } else {
                Uri uri = Uri.parse(value);
                Glide.with(getApplicationContext()).load(uri).into(userProfilePic);
            }

            userProfileName.setText(sharedpreferences.getString("personName", ""));
            userProfileEmail.setText(sharedpreferences.getString("personEmail", ""));

            privateWishCount.setText(String.valueOf(getAllWishes.size()));
            privateWishName.setText("Private Wishes");
            affirmationCount.setText(String.valueOf(sp.getInt("counter", 0)));
            affirmationName.setText("Affirmation Day");

            if (sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "").isEmpty()) {
                manifestType.setText("NA");
            } else {
                manifestType.setText(sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", ""));
            }

            manifestTypeName.setText("Manifesting");
            setTimeCount.setText(String.valueOf(timerValue.getInt("your_int_key", 60)));
            setTimeName.setText("Timer count");

            if (timerEnable.getInt(NOTIFICATION_ENABLE, 1) == 1) {
                notificationReminderStatus.setText("ON");
            } else {
                notificationReminderStatus.setText("OFF");
            }

            notificationReminderName.setText("Daily Notifications");
            loggedInAccountText.setText("Logged in as: " + sharedpreferences.getString("personName", ""));

            if (sharedpreferences.contains("syncDate")) {
                syncStatusText.setText("Last Sync at: " + sharedpreferences.getString("syncDate", ""));
            } else {
                syncStatusText.setText("No Sync performed");
            }

            dashboardLay1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent art = new Intent(getApplicationContext(), PrivateWishesActivity.class);
                    startActivity(art);
                }
            });

            dashboardLay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent art = new Intent(getApplicationContext(), AffirmationHome.class);
                    startActivity(art);
                }
            });

            settLinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent art = new Intent(getApplicationContext(), SelectManifestationTypeActivity.class);
                    startActivity(art);
                }
            });

            settLinear2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent art = new Intent(getApplicationContext(), SetTimeActivity.class);
                    startActivity(art);
                }
            });

            settLinear3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent art = new Intent(getApplicationContext(), SetReminderActivity.class);
                    startActivity(art);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = null;
                CheckInternetService checkInternetService = new CheckInternetService();
                NetworkInfo netInfo = checkInternetService.checkInternetConnection(connectivityManager, getApplicationContext());

                if(netInfo!=null && netInfo.isConnected()) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this, new OnCompleteListener < Void > () {@Override
                    public void onComplete(@NonNull Task < Void > task) {

                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove("personId");
                        editor.remove("syncDate");
                        editor.putString("personName", "");
                        editor.putString("personEmail", "");
                        editor.putString("personPhoto", "");
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

                ConnectivityManager connectivityManager = null;
                CheckInternetService checkInternetService = new CheckInternetService();
                NetworkInfo netInfo = checkInternetService.checkInternetConnection(connectivityManager, getApplicationContext());

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
                    editor.putString("syncDate", DateFormat.getDateTimeInstance().format(new Date()));
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
            skipLoginText = findViewById(R.id.SkipTextView);

            Glide.with(getApplicationContext()).load(R.drawable.lawimg).thumbnail(0.1f).fitCenter().into(appLogo);

            // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = null;
                CheckInternetService checkInternetService = new CheckInternetService();
                NetworkInfo netInfo = checkInternetService.checkInternetConnection(connectivityManager, getApplicationContext());

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

            skipLoginText.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View view) {
                finish();
            }
            });
        }
    }

    private JSONObject createJsonFile() throws JSONException {

        SharedPreferences sharedpreferences = getSharedPreferences("SocialAccount", Context.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("Affirmation_Counter", AffirmationActivity.MODE_PRIVATE);
        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);
        SharedPreferences timerValue = getSharedPreferences("your_prefs", Exercise2Activity.MODE_PRIVATE);
        SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("personId", sharedpreferences.getString("personId", ""));
            jsonObject.put("personEmail", sharedpreferences.getString("personEmail", ""));
            jsonObject.put("personName", sharedpreferences.getString("personName", ""));
            jsonObject.put("personPhoto", sharedpreferences.getString("personPhoto", ""));
            jsonObject.put("affirmationDate", sp.getString("Time", "NA"));
            jsonObject.put("affirmationCount", sp.getInt("counter", 0));
            jsonObject.put("manifestType", sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", ""));
            jsonObject.put("timerEnable", timerEnable.getInt(NOTIFICATION_ENABLE, 1));
            jsonObject.put("timerCount", timerValue.getInt("your_int_key", 60));
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
                editor.putString("personName", personName);
                editor.putString("personId", personId);
                editor.putString("personEmail", personEmail);
                editor.putString("personPhoto", personPhoto);
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
                Log.d("ERROR", String.valueOf(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext(), "Completed ! ... Synchronizing Data", Toast.LENGTH_LONG).show();
            syncBar.setVisibility(View.INVISIBLE);
            syncButton.setVisibility(View.VISIBLE);
            syncStatusText.setText("Last Sync at: " + sharedpreferences.getString("syncDate", ""));

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

                        handler.post(new Runnable() {
                            public void run() {

                                Toast.makeText(getApplicationContext(), " Directory exist " + jsonFilePath, Toast.LENGTH_LONG).show();
                            }
                        });

                        final InputStream ok1 = checkFileExists(jsonFileName, con);

                        if (ok1 != null) {

                            InputStreamReader isReader = new InputStreamReader(ok1);
                            BufferedReader reader = new BufferedReader(isReader);
                            StringBuffer sb = new StringBuffer();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            jsonObject = new JSONObject(sb.toString());

                        } else {
                            handler.post(new Runnable() {
                                public void run() {

                                    Toast.makeText(getApplicationContext(), " Both file & directory doesn't exist " + jsonFileName, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), " Diirectory not exist " + jsonFilePath, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                con.logout();
                con.disconnect();

            } catch(Exception e) {
                Log.d("ERROR", String.valueOf(e));
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

                        SharedPreferences sharedpreferences = getSharedPreferences("SocialAccount", Context.MODE_PRIVATE);
                        SharedPreferences sp = getSharedPreferences("Affirmation_Counter", Context.MODE_PRIVATE);
                        SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);
                        SharedPreferences timerValue = getSharedPreferences("your_prefs", Exercise2Activity.MODE_PRIVATE);
                        SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);

                        try {
                            SharedPreferences.Editor ed = sharedpreferences.edit();
                            ed.putString("personId", jsonObject.getString("personId"));
                            ed.putString("personEmail", jsonObject.getString("personEmail"));
                            ed.putString("personName", jsonObject.getString("personName"));
                            ed.putString("personPhoto", jsonObject.getString("personPhoto"));
                            ed.apply();

                            ed = sp.edit();
                            ed.putString("Time", jsonObject.getString("affirmationDate"));
                            ed.putInt("counter", jsonObject.getInt("affirmationCount"));
                            ed.apply();

                            ed = sp.edit();
                            ed.putString("Time", jsonObject.getString("affirmationDate"));
                            ed.putInt("counter", jsonObject.getInt("affirmationCount"));
                            ed.apply();

                            ed = sharedPreferencesManifestationType.edit();
                            ed.putString("MANIFESTATION_TYPE_VALUE", jsonObject.getString("manifestType"));
                            ed.apply();

                            ed = sharedPreferencesManifestationType.edit();
                            ed.putString("MANIFESTATION_TYPE_VALUE", jsonObject.getString("manifestType"));
                            ed.apply();

                            ed = timerEnable.edit();
                            ed.putString("timerEnable", jsonObject.getString("timerEnable"));
                            ed.apply();

                            ed = timerEnable.edit();
                            ed.putString("timerEnable", jsonObject.getString("timerEnable"));
                            ed.apply();

                            ed = timerValue.edit();
                            ed.putInt("your_int_key", Integer.parseInt(jsonObject.getString("timerCount")));
                            ed.apply();

                            ed = timerValue.edit();
                            ed.putInt("your_int_key", Integer.parseInt(jsonObject.getString("timerCount")));
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
                            Log.d("ERROR", String.valueOf(e));
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