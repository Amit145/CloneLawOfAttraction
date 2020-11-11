package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.amit.lawofattraction.sqlitedatabase.ActivityTrackerDatabaseHandler;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.FTPUpload;
import com.apps.amit.lawofattraction.utils.ManifestationTrackerUtils;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.apps.amit.lawofattraction.SetReminderActivity.NOTIFICATION_ENABLE;

public class LoginActivity extends AppCompatActivity {

    ImageView appLogo;
    Button fbButtonSignIn;
    Button googleSignOut;
    TextView titleText;
    TextView skipLoginText;
    SignInButton signInButton;

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
            final TextView syncStatusText = findViewById(R.id.syncStatusText);
            Button syncButton = findViewById(R.id.syncButton);

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
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            final File[] files = directory.listFiles();

            String value = sharedpreferences.getString("personPhoto", "");
            Uri uri = Uri.parse(value);

            Glide.with(getApplicationContext()).load(uri).into(userProfilePic);
            userProfileName.setText(sharedpreferences.getString("personName", ""));
            userProfileEmail.setText(sharedpreferences.getString("personEmail", ""));
            audioWishCount.setText(String.valueOf(files.length));
            audioWishName.setText("Audio Wishes");
            privateWishCount.setText(String.valueOf(getAllWishes.size()));
            privateWishName.setText("Private Wishes");
            affirmationCount.setText(String.valueOf(sp.getInt("counter", 0)));
            affirmationName.setText("Affirmations");
            manifestType.setText(sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", ""));
            manifestTypeName.setText("Manifestation");
            setTimeCount.setText(String.valueOf(timerValue.getInt("your_int_key", 60)));
            setTimeName.setText("Count Time");
            notificationReminderStatus.setText(String.valueOf(timerEnable.getInt(NOTIFICATION_ENABLE,1)));
            notificationReminderName.setText("Daily Notifications");
            loggedInAccountText.setText("Logged in using as: "+sharedpreferences.getString("personName", ""));

            if(sharedpreferences.contains("syncDate")) {
                syncStatusText.setText("Last Sync at: "+sharedpreferences.getString("syncDate", ""));
            } else {
                syncStatusText.setText("");
            }

            syncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String temp = null;
                    Toast.makeText(getApplicationContext(),"Synchronizing Data",Toast.LENGTH_LONG).show();

                    try {
                        temp = createJsonFile();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FTPUpload uploadFTP = new FTPUpload(files, sharedpreferences.getString("personId", ""), temp, getApplicationContext());
                    uploadFTP.execute();


                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("syncDate", DateFormat.getDateTimeInstance().format(new Date()));
                    editor.apply();

                    syncStatusText.setText("Last Sync at: "+sharedpreferences.getString("syncDate", ""));
                }
            });


        } else {

            setContentView(R.layout.activity_login);

            appLogo = findViewById(R.id.appLogo);
            googleSignOut = findViewById(R.id.googleSignOut);
            signInButton = findViewById(R.id.googleSignInButton);
            fbButtonSignIn = findViewById(R.id.facebookSignInButton);
            titleText = findViewById(R.id.signInTextView);
            skipLoginText = findViewById(R.id.SkipTextView);


            Glide.with(getApplicationContext()).load(R.drawable.lawimg).thumbnail(0.1f).fitCenter().into(appLogo);

            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            signInButton.setSize(SignInButton.SIZE_STANDARD);

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getApplicationContext(),"Google",Toast.LENGTH_LONG).show();
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });

            googleSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(),"U signed out",Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("personName", "");
                            editor.putString("personId", "");
                            editor.putString("personEmail", "");
                            editor.putString("personPhoto", "");
                            editor.apply();

                            signInButton.setVisibility(View.VISIBLE);
                            googleSignOut.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });

            fbButtonSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getApplicationContext(),"Facebook",Toast.LENGTH_LONG).show();
                }
            });

            skipLoginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getApplicationContext(),"Skipped",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private String createJsonFile() throws JSONException {

        File root = android.os.Environment.getExternalStorageDirectory();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return root.getAbsolutePath() + "/LawOfAttraction/JSON/"+sharedpreferences.getString("personId", "")+".json";
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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

                // Signed in successfully, show authenticated UI.
               // SynchronizeData synchronizeData = new SynchronizeData(this);

                //Send Wishes to Server
                //synchronizeData.getAllPrivateWishes(account.getId(), personName, personEmail);
                //String returnValue = synchronizeData.getAffirmationStatus();


                //Load wishes from Server
                //synchronizeData.loadAllPrivateWishes(account.getId());

                updateUI(account);
            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Toast.makeText(getApplicationContext(),"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
            updateUI(null);
        }
    }

    //Change UI according to user data.
    public void updateUI(GoogleSignInAccount account){

        /*
        if(account != null){

            signInButton.setVisibility(View.INVISIBLE);
            googleSignOut.setVisibility(View.VISIBLE);

            //Toast.makeText(this,"U Signed In successfully "+personName,Toast.LENGTH_LONG).show();


        }else {

            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
            signInButton.setVisibility(View.VISIBLE);
            googleSignOut.setVisibility(View.INVISIBLE);
        }

        */
    }
}