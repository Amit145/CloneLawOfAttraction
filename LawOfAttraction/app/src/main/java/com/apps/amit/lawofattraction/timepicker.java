package com.apps.amit.lawofattraction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import java.util.Calendar;
import butterknife.ButterKnife;


public class timepicker extends AppCompatActivity {

    private TimePicker timePicker1;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Button set;
   // private AdView mAdView;
    TextView Title,subtitle;
    public int notID, timervalue;
    RadioButton r1,r2;
    static Intent open;
    static Intent share;

    static PendingIntent p1;
    static PendingIntent p2;
    @Override
    protected void onDestroy() {
        super.onDestroy();

       // mAdView=null;
        timePicker1=null;
        set=null;
        this.finish();


    }

    @Override
    protected void onStop() {
        super.onStop();
       // mAdView=null;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       // mAdView=null;
        timePicker1=null;
        set=null;
        this.finish();
       // mAdView=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);




        Title =  findViewById( R.id.textView8);
        subtitle = findViewById( R.id.textView91);
        timePicker1 =  findViewById(R.id.timePicker);
        set =  findViewById(R.id.set);
        r1 =  findViewById( R.id.radioNotiON);
        r2 =  findViewById( R.id.radioNotiOFF);

        SharedPreferences sp1 = getSharedPreferences("timerNotiEnable", Exercise1.MODE_PRIVATE);
         timervalue = sp1.getInt("timerNotiEnable",1);

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");
        updateViews(value1);

        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        final Resources resources = context.getResources();

        if(timervalue==0)
        {
            r1.setChecked(false);
            r2.setChecked(true);


            timePicker1.setVisibility(View.INVISIBLE);
            set.setVisibility(View.INVISIBLE);
        }
        else   //ON CONDITION
        {
            r2.setChecked(false);
            r1.setChecked(true);

            timePicker1.setVisibility(View.VISIBLE);
            set.setVisibility(View.VISIBLE);


        }
       // txt = (TextView) findViewById(R.id.textView);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp1 = getSharedPreferences("timerNotiEnable", Exercise1.MODE_PRIVATE);
                timervalue = sp1.getInt("timerNotiEnable",1);
                if(timervalue==1) {
                    v.startAnimation(buttonClick);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker1.getCurrentHour(),
                            timePicker1.getCurrentMinute(),
                            0

                    );


                    /* Alarm At 10 pm
                    Calendar daily = Calendar.getInstance();
                    daily.set(Calendar.HOUR_OF_DAY, 22);
                    daily.set(Calendar.MINUTE, 0);
                    daily.set(Calendar.SECOND, 0);
                    daily.set(Calendar.MILLISECOND, 0);

                    //Alarma at 7pm

                    Calendar dailyComments = Calendar.getInstance();
                    dailyComments.set(Calendar.HOUR_OF_DAY, 19);
                    dailyComments.set(Calendar.MINUTE, 0);
                    dailyComments.set(Calendar.SECOND, 0);
                    dailyComments.set(Calendar.MILLISECOND, 0);

                    */


                    startAlarm(calendar.getTimeInMillis());
                    //startAlarm1(daily.getTimeInMillis());
                    //startAlarm2(dailyComments.getTimeInMillis());

                }
                else{

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_enable),Toast.LENGTH_SHORT).show();

                }




            }

            private void startAlarm(long timeInMilli)
            {
                AlarmManager m = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(getApplicationContext(),AlarmReceiver.class);
                open = new Intent(getApplicationContext(),Home.class);
                share = new Intent(getApplicationContext(), Settings.class);
                PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

                if(m!=null) {

                    m.setRepeating(AlarmManager.RTC, timeInMilli, AlarmManager.INTERVAL_DAY, p);
                }

                Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_daily),Toast.LENGTH_SHORT).show();

            }


            /*  Commented Notification at 10 pm and 7 pm

            private void startAlarm1(long timeInMilli)
            {
                AlarmManager m = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(getApplicationContext(), Notify.class);
                //i.putExtra("id",notification_id);

                PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
                m.setRepeating(AlarmManager.RTC, timeInMilli, AlarmManager.INTERVAL_DAY, p);
                //Intent d = new Intent(timepicker.this,Notify.class);

               // Toast.makeText(getApplicationContext(),"Reminder  2 SET",Toast.LENGTH_SHORT).show();

            }

            private void startAlarm2(long timeInMilli)
            {
                AlarmManager mComments = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent iComments = new Intent(getApplicationContext(), CommentsReminder.class);
                //i.putExtra("id",notification_id);

                PendingIntent pComm = PendingIntent.getBroadcast(getApplicationContext(), 0, iComments, 0);
                mComments.setRepeating(AlarmManager.RTC, timeInMilli, AlarmManager.INTERVAL_DAY, pComm);



                //Toast.makeText(getApplicationContext(),"Daily Reminder Set",Toast.LENGTH_SHORT).show();

            }


            */
        });

    }


    public void timerNotiOFF(View view) {

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(this, value1);
        Resources resources = context.getResources();

        notID=0;
        r1.setChecked(false);
        timePicker1.setVisibility(View.INVISIBLE);
        set.setVisibility(View.INVISIBLE);
        SharedPreferences timerEnable = getSharedPreferences("timerNotiEnable", SetTime.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putInt("timerNotiEnable", notID);
        editor.apply();

        Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_disabletext)  , Toast.LENGTH_LONG).show();

    }

    public void timerNotiON(View view) {

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(this, value1);
        Resources resources = context.getResources();

        notID=1;
        r2.setChecked(false);
        timePicker1.setVisibility(View.VISIBLE);
        set.setVisibility(View.VISIBLE);
        SharedPreferences timerEnable = getSharedPreferences("timerNotiEnable", SetTime.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putInt("timerNotiEnable", notID);
        editor.apply();

        Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_enableText) , Toast.LENGTH_LONG).show();

    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();


        //txt1= (TextView) findViewById(R.id.textView3);

        //Story


        Title.setText(resources.getString(R.string.activityReminder_text1));
        subtitle.setText(resources.getString(R.string.activityReminder_text2));
        r1.setText(resources.getString(R.string.on_text));
        r2.setText(resources.getString(R.string.off_text));
        set.setText(resources.getString(R.string.activityReminder_text3));

        //Seconds.setText(resources.getString(R.string.seconds_text));
        //b1.setText(resources.getString(R.string.activityTimer_text1));




    }
}
