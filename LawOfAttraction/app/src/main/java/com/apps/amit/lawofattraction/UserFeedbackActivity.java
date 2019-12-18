package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.helper.LocaleHelper;

import java.util.HashMap;
import java.util.Map;

public class UserFeedbackActivity extends AppCompatActivity {

    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton r3;
    RadioButton r4;
    RadioButton r5;
    RadioButton r6;
    RadioButton r7;
    RadioButton r8;
    RadioButton r9;
    RadioButton r10;
    String field1;
    String field2;
    String field3;
    String field4;
    Resources resources;
    NetworkInfo netInfo;
    ConnectivityManager connMngr;
    TextView title;
    TextView usrname;
    TextView feed;
    TextView feed1;
    TextView feed2;
    TextView feed3;
    TextView feed4;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    EditText et1;
    EditText et2;
    Button bt1;
    RequestQueue requestQueue;
    Button bt2;
    String dataFeedbackUrl = "http://www.innovativelabs.xyz/insert_feedback.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        field1="Daily";
        field2="Best";
        field3="Yes";
        field4="Yes";

        radioButton1 =  findViewById(R.id.radioDaily);
        radioButton2 =  findViewById(R.id.radioWeekly);

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

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences sp1 = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
        String userName = sp1.getString("userName","");

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

        radioButton1.setText(resources.getString(R.string.daily));
        radioButton2.setText(resources.getString(R.string.weekly));
        r3 .setText(resources.getString(R.string.monthly));
        r4 . setText(resources.getString(R.string.needImprovement));
        r5 . setText(resources.getString(R.string.good));
        r6 . setText(resources.getString(R.string.best));
        r7 . setText(resources.getString(R.string.Yes_text));
        r8 . setText(resources.getString(R.string.No_text));
        r9 . setText(resources.getString(R.string.Yes_text));
        r10 . setText(resources.getString(R.string.No_text));

        if((userName!=null && userName.equalsIgnoreCase(""))) {
            et1.setText("");
        } else{
            et1.setText(userName);
        }
    }

    public void radioFeedback1(View view) {
        field1="Daily";
        radioButton2.setChecked(false);
        r3.setChecked(false);
    }

    public void radioFeedback11(View view) {
        field1="Weekly";
        radioButton1.setChecked(false);
        r3.setChecked(false);
    }

    public void radioFeedback111(View view) {
        field1="Monthly";
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
    }

    public void radioFeedback2(View view) {
        field2="Need Improvement";
        r5.setChecked(false);
        r6.setChecked(false);
    }

    public void radioFeedback22(View view) {
        field2="Good";
        r4.setChecked(false);
        r6.setChecked(false);
    }

    public void radioFeedback222(View view) {
        field2="Best";
        r4.setChecked(false);
        r5.setChecked(false);
    }

    public void radioFeedback3(View view) {
        field3="Yes";
        r8.setChecked(false);
    }

    public void radioFeedback33(View view) {
        field3="No";
        r7.setChecked(false);
    }

    public void radioFeedback4(View view) {
        field4="Yes";
        r10.setChecked(false);
    }

    public void radioFeedback44(View view) {
        field4="No";
        r9.setChecked(false);
    }

    public void submit(View view) {

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        resources = context.getResources();

        if (TextUtils.isEmpty(et1.getText().toString())  ) {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.enterName), Toast.LENGTH_LONG).show();

        } else {
            view.startAnimation(buttonClick);

            if ((!field2.equalsIgnoreCase("Best") || field3.equalsIgnoreCase("No") || field4.equalsIgnoreCase("No")) && TextUtils.isEmpty(et2.getText().toString())) {

                feed4.setError(" Please let us know how we can improve");
                Toast.makeText(getApplicationContext(), "Please let us know how we can improve ?", Toast.LENGTH_LONG).show();

            } else {

                SharedPreferences timerEnable = getSharedPreferences("timerEnable", MyStoryActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = timerEnable.edit();
                editor.putString("userName", et1.getText().toString());
                editor.apply();

                SendDataToServer(et1.getText().toString(), field1, field2, field3, field4, "FREE: " + et2.getText().toString());

                this.finish();
            }
        }
    }

    public void cancel(View view) {

        view.startAnimation(buttonClick);
       this.finish();
    }

    public void SendDataToServer(final String name, final String daily, final String graphics, final String easy, final String helps, final String suggestions){

        String url = dataFeedbackUrl;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if(connMngr!=null && connMngr.getActiveNetworkInfo() != null){
                            netInfo = connMngr.getActiveNetworkInfo();
                        }

                        if(netInfo!=null && netInfo.isConnected()) {
                            Toast.makeText(getApplicationContext(), resources.getString(R.string.thankYou), Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(), resources.getString(R.string.noInternet_txt), Toast.LENGTH_LONG).show();
                        }
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();

                params.put("name", name);
                params.put("daily", daily);
                params.put("graphics", graphics);
                params.put("easy", easy);
                params.put("helps", helps);
                params.put("suggestions", suggestions);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}
