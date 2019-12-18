package com.apps.amit.lawofattraction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apps.amit.lawofattraction.notifications.DailyReminderReceiver;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import java.util.Calendar;
import butterknife.ButterKnife;


public class SetReminderActivity extends AppCompatActivity {

    private TimePicker timePicker1;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Button set;
    TextView title;
    TextView subtitle;
    int notID;
    int timervalue;
    RadioButton r1,r2;
    static Intent open;
    static Intent share;
    public static PendingIntent p1;
    static PendingIntent p2;
    public static final String NOTIFICATION_ENABLE = "timerNotiEnable";
    public static final String USER_LANGUAGE = "UserLang";
    public static final String LANGUAGE = "language";

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timePicker1=null;
        set=null;
        this.finish();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        timePicker1=null;
        set=null;
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);




        title =  findViewById( R.id.textView8);
        subtitle = findViewById( R.id.textView91);
        timePicker1 =  findViewById(R.id.timePicker);
        set =  findViewById(R.id.set);
        r1 =  findViewById( R.id.radioNotiON);
        r2 =  findViewById( R.id.radioNotiOFF);

        SharedPreferences sp1 = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);
         timervalue = sp1.getInt(NOTIFICATION_ENABLE,1);

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString(LANGUAGE,"en");
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

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp1 = getSharedPreferences(NOTIFICATION_ENABLE, Exercise1Activity.MODE_PRIVATE);
                timervalue = sp1.getInt(NOTIFICATION_ENABLE,1);
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



                    startAlarm(calendar.getTimeInMillis());

                }
                else{

                    Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_enable),Toast.LENGTH_SHORT).show();

                }




            }

            private void startAlarm(long timeInMilli)
            {
                AlarmManager m = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(getApplicationContext(), DailyReminderReceiver.class);
                open = new Intent(getApplicationContext(), HomeActivity.class);
                share = new Intent(getApplicationContext(), SettingsActivity.class);
                PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

                if(m!=null) {

                    m.setRepeating(AlarmManager.RTC, timeInMilli, AlarmManager.INTERVAL_DAY, p);
                }

                Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_daily),Toast.LENGTH_SHORT).show();

            }

        });

    }


    public void timerNotiOFF(View view) {

        SharedPreferences pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString(LANGUAGE,"en");

        Context context = LocaleHelper.setLocale(this, value1);
        Resources resources = context.getResources();

        notID=0;
        r1.setChecked(false);
        timePicker1.setVisibility(View.INVISIBLE);
        set.setVisibility(View.INVISIBLE);
        SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, SetTimeActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putInt(NOTIFICATION_ENABLE, notID);
        editor.apply();

        Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_disabletext)  , Toast.LENGTH_LONG).show();

    }

    public void timerNotiON(View view) {

        SharedPreferences pref = getSharedPreferences(USER_LANGUAGE,MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value1 = pref.getString(LANGUAGE,"en");

        Context context = LocaleHelper.setLocale(this, value1);
        Resources resources = context.getResources();

        notID=1;
        r2.setChecked(false);
        timePicker1.setVisibility(View.VISIBLE);
        set.setVisibility(View.VISIBLE);
        SharedPreferences timerEnable = getSharedPreferences(NOTIFICATION_ENABLE, SetTimeActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerEnable.edit();
        editor.putInt(NOTIFICATION_ENABLE, notID);
        editor.apply();

        Toast.makeText(getApplicationContext(), resources.getString(R.string.noti_enableText) , Toast.LENGTH_LONG).show();

    }

    private void updateViews(String languageCode) {

        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();


        title.setText(resources.getString(R.string.activityReminder_text1));
        subtitle.setText(resources.getString(R.string.activityReminder_text2));
        r1.setText(resources.getString(R.string.on_text));
        r2.setText(resources.getString(R.string.off_text));
        set.setText(resources.getString(R.string.activityReminder_text3));


    }
}
