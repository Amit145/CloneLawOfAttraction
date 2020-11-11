package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.apps.amit.lawofattraction.sqlitedatabase.ActivityTrackerDatabaseHandler;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.ManifestationTrackerUtils;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.apps.amit.lawofattraction.SetReminderActivity.NOTIFICATION_ENABLE;

public class LoginActivity extends AppCompatActivity {

    ImageView appLogo;
    LoginButton fbButtonSignIn;
    TextView titleText;
    TextView skipLoginText;
    SignInButton signInButton;
    CallbackManager callbackManager;
    TextView syncStatusText;
    String temp ;
    ProgressBar syncBar;
    Button syncButton;
    JSONObject jsonObject;

    @Override
    public void onBackPressed() {
        Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(art1);
    }

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences("SocialAccount", Context.MODE_PRIVATE);

        if (sharedpreferences.contains("personId")) {
            setContentView(R.layout.activity_my_profile);

            syncBar = findViewById(R.id.syncBar);
            CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
            TextView userProfileName = findViewById(R.id.userProfileName);
            TextView userProfileEmail = findViewById(R.id.userProfileEmail);
            TextView audioWishCount = findViewById(R.id.audioWishCount);
            TextView audioWishName = findViewById(R.id.audioWishName);
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

            List<PrivateWishesUtils> getAllWishes = db.getAllWishes();
            List<ManifestationTrackerUtils> getAllLogs = adb.getAllContacts();

            SharedPreferences sp = getSharedPreferences("Affirmation_Counter", AffirmationActivity.MODE_PRIVATE);
            SharedPreferences sharedPreferencesManifestationType = getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);
            SharedPreferences timerValue = getSharedPreferences("your_prefs", Exercise2Activity.MODE_PRIVATE);
            SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);

            File root = android.os.Environment.getExternalStorageDirectory();
            String path = root.getAbsolutePath() + "/LawOfAttraction/Audios";
            File directory = new File(path);
            final File[] files = directory.listFiles();

            String value = sharedpreferences.getString("personPhoto", "");
            Uri uri = Uri.parse(value);

            Glide.with(getApplicationContext()).load(uri).into(userProfilePic);
            userProfileName.setText(sharedpreferences.getString("personName", ""));
            userProfileEmail.setText(sharedpreferences.getString("personEmail", ""));

            if(files!=null){
                audioWishCount.setText(String.valueOf(files.length));
            }

            audioWishName.setText("Audio Wishes");
            privateWishCount.setText(String.valueOf(getAllWishes.size()));
            privateWishName.setText("Private Wishes");
            affirmationCount.setText(String.valueOf(sp.getInt("counter", 0)));
            affirmationName.setText("Affirmation Day");

            if(sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "").isEmpty()) {
                manifestType.setText("NA");
            } else {
                manifestType.setText(sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", ""));
            }

            manifestTypeName.setText("Manifesting");
            setTimeCount.setText(String.valueOf(timerValue.getInt("your_int_key", 60)));
            setTimeName.setText("Timer count");

            if(timerEnable.getInt(NOTIFICATION_ENABLE,1)==1) {
                notificationReminderStatus.setText("ON");
            } else {
                notificationReminderStatus.setText("OFF");
            }

            notificationReminderName.setText("Daily Notifications");
            loggedInAccountText.setText("Logged in using as: "+sharedpreferences.getString("personName", ""));

            if(sharedpreferences.contains("syncDate")) {
                syncStatusText.setText("Last Sync at: "+sharedpreferences.getString("syncDate", ""));
            } else {
                syncStatusText.setText("");
            }

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();

                    mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(),"U signed out",Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove("personId");
                            editor.remove("syncDate");
                            editor.putString("personName", "");
                            editor.putString("personEmail", "");
                            editor.putString("personPhoto", "");
                            editor.apply();

                            //load activity
                            Intent art = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(art);
                        }
                    });
                }
            });

            syncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    syncStatusText.setText("Synchronizing....!");
                    Toast.makeText(getApplicationContext(),"Synchronizing Data",Toast.LENGTH_LONG).show();

                    try {
                        jsonObject = createJsonFile();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    syncBar.setVisibility(View.VISIBLE);
                    syncButton.setVisibility(View.INVISIBLE);
                    FTPUpload uploadFTP = new FTPUpload();
                    uploadFTP.execute();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("syncDate", DateFormat.getDateTimeInstance().format(new Date()));
                    editor.apply();
                }
            });
        } else {

            setContentView(R.layout.activity_login);

            appLogo = findViewById(R.id.appLogo);
            signInButton = findViewById(R.id.googleSignInButton);
            fbButtonSignIn = findViewById(R.id.login_button);
            titleText = findViewById(R.id.signInTextView);
            skipLoginText = findViewById(R.id.SkipTextView);

            Glide.with(getApplicationContext()).load(R.drawable.lawimg).thumbnail(0.1f).fitCenter().into(appLogo);

            // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            signInButton.setSize(SignInButton.SIZE_STANDARD);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });

            fbButtonSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Facebook",Toast.LENGTH_LONG).show();
                    //loginUsingFB();
                }
            });

            skipLoginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void loginUsingFB() {

        //keytool -exportcert -alias androiddebugkey -keystore "C:\Users\amitg13\.android\debug.keystore" | "openssl" sha1 -binary | "openssl" base64
        //Bmce+9aHdOoVtE7fS3B07tfj7Bc=

        callbackManager = CallbackManager.Factory.create();

        fbButtonSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loginResult.getAccessToken().getUserId();
                String userProfile = "https://graph.facebook.com/"+loginResult.getAccessToken().getUserId()+"picture?return_ssl_resources=1";

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);

                        Toast.makeText(getApplicationContext(),bFacebookData.toString(),Toast.LENGTH_LONG).show();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(JSONException e) {
           // Log.d(TAG,"Error parsing JSON");
        }
        return null;
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
            jsonObject.put("affirmationDate",sp.getString("Time", "NA"));
            jsonObject.put("affirmationCount", sp.getInt("counter", 0));
            jsonObject.put("manifestType", sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", ""));
            jsonObject.put("timerEnable", timerEnable.getInt(NOTIFICATION_ENABLE,1));
            jsonObject.put("timerCount", timerValue.getInt("your_int_key", 60));
            //jsonObject.put("notificationDate", notificationDate);

            WishDataBaseHandler db = new WishDataBaseHandler(getApplicationContext());
            ActivityTrackerDatabaseHandler adb = new ActivityTrackerDatabaseHandler(getApplicationContext());

            List<PrivateWishesUtils> getAllWishes = db.getAllWishes();
            List<ManifestationTrackerUtils> getAllLogs = adb.getAllContacts();

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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        try {
            ///LawOfAttraction/Audios
            File temp = new File(root.getAbsolutePath() + "/LawOfAttraction/JSON");
            if (!temp.exists()) {
                temp.mkdirs();
            }
            FileWriter file = new FileWriter(root.getAbsolutePath() + "/LawOfAttraction/JSON/"+sharedpreferences.getString("personId", "")+".json");
            file.write(jsonObject.toString());
            file.close();

        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
        }
        */
        return jsonObject;
    }

    public static boolean makeDirectories(FTPClient ftpClient, String dirPath)
            throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir : pathElements) {
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        System.out.println("CREATED directory: " + singleDir);
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        System.out.println("COULD NOT create directory: " + singleDir);
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
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account != null){
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
                Intent art = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(art);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(),"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
        }
    }

    public class FTPUpload extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            FTPClient con = null;
            try {
                con = new FTPClient();
                con.connect("ftp.innovativelabs.xyz");

                if (con.login("u941116359.amitg145", "4aR|i3I4N"))
                {
                    con.enterLocalPassiveMode(); // important!

                    String jsonFilePath = sharedpreferences.getString("personId", "")+"/JSON";

                    boolean result1 = LoginActivity.makeDirectories(con, jsonFilePath);
                    if(result1) {
                        con.changeWorkingDirectory(jsonFilePath);
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        String str = jsonObject.toString();
                        InputStream is = new ByteArrayInputStream(str.getBytes());

                        //File tempFile = new File(temp);
                        //FileInputStream in = new FileInputStream(tempFile);
                        con.storeFile(sharedpreferences.getString("personId", "")+".json", is);
                        is.close();
                    }

                    con.logout();
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext(),"Completed ! ... Synchronizing Data",Toast.LENGTH_LONG).show();
            syncBar.setVisibility(View.INVISIBLE);
            syncButton.setVisibility(View.VISIBLE);
            syncStatusText.setText("Last Sync at: "+sharedpreferences.getString("syncDate", ""));
        }
    }
}