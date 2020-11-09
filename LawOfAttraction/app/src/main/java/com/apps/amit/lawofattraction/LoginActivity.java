package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.amit.lawofattraction.helper.SynchronizeData;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
        setContentView(R.layout.activity_login);

        appLogo = findViewById(R.id.appLogo);
        googleSignOut = findViewById(R.id.googleSignOut);
        signInButton = findViewById(R.id.googleSignInButton);
        fbButtonSignIn = findViewById(R.id.facebookSignInButton);
        titleText = findViewById(R.id.signInTextView);
        skipLoginText = findViewById(R.id.SkipTextView);
        sharedpreferences = getSharedPreferences("SocialAccount", Context.MODE_PRIVATE);

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
                SynchronizeData synchronizeData = new SynchronizeData(this);

                //Send Wishes to Server
                synchronizeData.getAllPrivateWishes(account.getId(), personName, personEmail);
                //String returnValue = synchronizeData.getAffirmationStatus();


                //Load wishes from Server
                synchronizeData.loadAllPrivateWishes(account.getId());

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

        if(account != null){

            signInButton.setVisibility(View.INVISIBLE);
            googleSignOut.setVisibility(View.VISIBLE);

            //Toast.makeText(this,"U Signed In successfully "+personName,Toast.LENGTH_LONG).show();


        }else {

            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
            signInButton.setVisibility(View.VISIBLE);
            googleSignOut.setVisibility(View.INVISIBLE);
        }

    }
}