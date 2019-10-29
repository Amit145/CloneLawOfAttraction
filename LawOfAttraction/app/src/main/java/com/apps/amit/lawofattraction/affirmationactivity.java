package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class affirmationactivity extends AppCompatActivity {

    TextView count,affirmationTitle,affirmationSubTitle;
    Affirmations affirmations ;
    Button button;
    int counter = 0 ;
    String s = null;
    String subTitle = null;
    DatabaseHandler db;
    Calendar cal;
    String token = "";
    int displayCount = 0;
    ConnectivityManager connMngr;
    String userInfo = "http://www.innovativelabs.xyz/insert_affirmUser.php";
    NetworkInfo netInfo;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirmationactivity);

        cal = Calendar.getInstance();
        //cal.add(Calendar.DATE,1);
        cal.add(Calendar.MINUTE,1);

        affirmations = new Affirmations();

        Intent result = getIntent();

        if(result.getExtras()!=null) {
            counter = result.getExtras().getInt("counter");
        }

        count = findViewById(R.id.affirmationCount);
        affirmationTitle = findViewById(R.id.affirmationTitle);
        affirmationSubTitle = findViewById(R.id.affirmationSubtitle);
        button = findViewById(R.id.affirmationDoneButton);

        if(counter<affirmations.getList().size()) {
            s = affirmations.getList().get(counter);
            //subTitle= affirmationSubList.getSubList().get(counter);
        } else {
            counter = 0 ;
            s = affirmations.getList().get(counter);
            //subTitle= affirmationSubList.getSubList().get(counter);
        }

        displayCount = counter + 1;
        count.setText(getString(R.string.hashTag)+displayCount);
        affirmationTitle.setText(s);

        affirmationSubTitle.setText(getString(R.string.myAffirmSubtitle));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent intent = new Intent(getApplicationContext(),Affirmations.class);
                intent.putExtra("counter",counter++);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",
                        Locale.getDefault());
                String date = df.format(c);

                db = new DatabaseHandler(getApplicationContext());
                db.addContact(new Contact(date, s));

                SharedPreferences sp = getSharedPreferences("Affirmation_Counter", affirmationactivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("counter",counter);
                editor.putString("Time",String.valueOf(cal.getTime()));
                editor.apply();

                //Toast.makeText(getApplicationContext(),counter,Toast.LENGTH_LONG).show();

                connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){

                    netInfo = connMngr.getActiveNetworkInfo();
                }

                if(netInfo!=null && netInfo.isConnected())
                {
                    if(counter!=affirmations.getList().size()){
                        callNotification(cal.getTimeInMillis());

                        token  = FirebaseInstanceId.getInstance().getToken();
                        SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);

                        String name = sp1.getString("userName","");

                        SendDataToServer(String.valueOf(displayCount),name,s,date,token);
                    }

                }

                if(counter!=affirmations.getList().size()){
                    callNotification(cal.getTimeInMillis());
                }

            }
        });
    }

    private void callNotification(long timeInMillis) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AffirmationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

        if(alarmManager!=null) {
            alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void SendDataToServer(final String day, final String name, final String affirmation, final String date, final String token){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("date", date));
                nameValuePairs.add(new BasicNameValuePair("day", day));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("affirmation", affirmation));
                nameValuePairs.add(new BasicNameValuePair("token", token));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(userInfo);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                    response.getEntity();


                } catch (Exception e) {

                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(day,name,affirmation,date,token);
    }
}
