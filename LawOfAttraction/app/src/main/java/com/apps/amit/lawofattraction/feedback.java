package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class feedback extends AppCompatActivity {

    public RadioButton r1,r2,r3,r4,r5,r6,r7,r8,r9,r10;
    public String Name,F1,F2,F3,F4;
    Resources resources;
    TextView title,usrname,feed,feed1,feed2,feed3,feed4;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    EditText et1,et2;
    Button bt1,bt2;
    String Datafeedback = "http://www.innovativelabs.xyz/insert_feedback.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        F1="Daily";
        F2="Best";
        F3="Yes";
        F4="Yes";
        r1 =  findViewById(R.id.radioDaily);
        r2 =  findViewById(R.id.radioWeekly);
        r3 =  findViewById(R.id.radioMonthly);
        r4 =  findViewById(R.id.radioWorse);
        r5 =  findViewById(R.id.radioAverage);
        r6 =  findViewById(R.id.radioBest);
        r7 =  findViewById(R.id.radioYes);
        r8 =  findViewById(R.id.radioNo);
        r9 =  findViewById(R.id.radioYes1);
        r10 =  findViewById(R.id.radioNo1);

        et1 =  findViewById(R.id.feedbackName);
        et2 =  findViewById(R.id.feedback4);

        bt1 =  findViewById(R.id.btncancel1);
        bt2 =  findViewById(R.id.btnsubmit1);

        title =  findViewById(R.id.title);
        usrname =  findViewById(R.id.name);
        feed =  findViewById(R.id.feed);
        feed1 =  findViewById(R.id.feed1);
        feed2 =  findViewById(R.id.feed2);
        feed3 =  findViewById(R.id.feed3);
        feed4 =  findViewById(R.id.feed4);


        SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
        String naam = sp1.getString("userName","");

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
         resources = context.getResources();

        title .setText(resources.getString(R.string.feedback));
        usrname.setText(resources.getString(R.string.subUniverse_dialogName));
        feed.setText(resources.getString(R.string.feedback1));
        feed1.setText(resources.getString(R.string.feedback2));
        feed2.setText(resources.getString(R.string.feedback3));
        feed3.setText(resources.getString(R.string.feedback4));
        feed4.setText(resources.getString(R.string.feedback5));

        bt1.setText(resources.getString(R.string.subUniverse_dialogCancel));
        bt2.setText(resources.getString(R.string.subUniverse_dialogSubmit));

        r1.setText(resources.getString(R.string.daily));
        r2.setText(resources.getString(R.string.weekly));
        r3 .setText(resources.getString(R.string.monthly));
        r4 . setText(resources.getString(R.string.needImprovement));
        r5 . setText(resources.getString(R.string.good));
        r6 . setText(resources.getString(R.string.best));
        r7 . setText(resources.getString(R.string.Yes_text));
        r8 . setText(resources.getString(R.string.No_text));
        r9 . setText(resources.getString(R.string.Yes_text));
        r10 . setText(resources.getString(R.string.No_text));

        if((naam.equalsIgnoreCase("")))
        {
            et1.setText("");

        }

        else{

            et1.setText(naam);
            //Toast.makeText(getApplicationContext(), "Name set: " +naam, Toast.LENGTH_LONG).show();


        }

    }

    public void radioFeedback1(View view)
    {
        F1="Daily";
        r2.setChecked(false);
        r3.setChecked(false);


    }
    public void radioFeedback11(View view)
    {
        F1="Weekly";
        r1.setChecked(false);
        r3.setChecked(false);


    }
    public void radioFeedback111(View view)
    {
        F1="Monthly";
        r1.setChecked(false);
        r2.setChecked(false);


    }




    public void radioFeedback2(View view)
    {
        F2="Need Improvement";
        r5.setChecked(false);
        r6.setChecked(false);

    }
    public void radioFeedback22(View view)
    {

        F2="Good";
        r4.setChecked(false);
        r6.setChecked(false);
    }
    public void radioFeedback222(View view)
    {

        F2="Best";
        r4.setChecked(false);
        r5.setChecked(false);
    }

    public void radioFeedback3(View view)
    {

        F3="Yes";
        r8.setChecked(false);

    }

    public void radioFeedback33(View view)
    {
        F3="No";
        r7.setChecked(false);

    }


    public void radioFeedback4(View view) {
        F4="Yes";
        r10.setChecked(false);

    }

    public void radioFeedback44(View view) {
        F4="No";
        r9.setChecked(false);

    }

    public void submit(View view) {

       // SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
       // String naam = sp1.getString("userName","");

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        Resources resources = context.getResources();

        if (TextUtils.isEmpty(et1.getText().toString())  )
        {

            //et1.setError("Please Enter Your Name");

           // et1.setError("Please Enter Your Wish");
            Toast.makeText(getApplicationContext(), resources.getString(R.string.enterName), Toast.LENGTH_LONG).show();

        }

        else {
            view.startAnimation(buttonClick);

            SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
            SharedPreferences.Editor editor = timerEnable.edit();
            editor.putString("userName", et1.getText().toString());
            editor.apply();

            SendDataToServer(et1.getText().toString(),F1,F2,F3,F4,"FREE: "+et2.getText().toString());
            Toast.makeText(this, resources.getString(R.string.thankYou), Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    public void cancel(View view) {

        view.startAnimation(buttonClick);
       this.finish();
    }


    public void SendDataToServer(final String name, final String daily, final String graphics, final String easy, final String helps, final String suggestions){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("daily", daily));
                nameValuePairs.add(new BasicNameValuePair("graphics", graphics));

                nameValuePairs.add(new BasicNameValuePair("easy", easy));
                nameValuePairs.add(new BasicNameValuePair("helps", helps));
                nameValuePairs.add(new BasicNameValuePair("suggestions", suggestions));


                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Datafeedback);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    HttpResponse response = httpClient.execute(httpPost);

                     response.getEntity();


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), getString(R.string.nameError4), Toast.LENGTH_LONG).show();

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, daily, graphics, easy, helps, suggestions);
    }
}
