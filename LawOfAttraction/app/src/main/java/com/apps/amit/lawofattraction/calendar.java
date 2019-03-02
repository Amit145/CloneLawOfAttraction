package com.apps.amit.lawofattraction;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;


public class calendar extends AppCompatActivity {

    DatabaseHandler db;
    Button caltext;
    TextView title,subtitle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Contact> lsItem;
    int year_y,month_m,day_d;
    String date;

    static  final int DIALOG_ID=0;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        //interstitial.setAdListener(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
       // interstitial.setAdListener(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //interstitial.setAdListener(null);
        db.close();
        adapter=null;
        recyclerView=null;
        Intent art1 = new Intent(getApplicationContext(), Home.class);
        startActivity(art1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final Calendar cale = Calendar.getInstance();
        year_y=cale.get(Calendar.YEAR);
        month_m=cale.get(Calendar.MONTH);
        day_d=cale.get(Calendar.DAY_OF_MONTH);

        showDialogOnButtonClick();

        caltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });

        AdView mAdView =  findViewById(R.id.adView8);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);

        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lsItem = new ArrayList<>();


        db= new DatabaseHandler(getApplicationContext());

        // Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();


        for (Contact cn : contacts) {
            //String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();

            Contact c = new Contact(cn.getName(),cn.getPhoneNumber());
            // Toast.makeText(calendar.this,"Value"+log+" ",Toast.LENGTH_LONG).show();


            lsItem.add(c);
            Collections.reverse(lsItem);
        }


        adapter = new RecyclerAdapter(lsItem,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this,dpickerListener,year_y,month_m,day_d);
        return null;
    }
    public void showDialogOnButtonClick() {

        ButterKnife.bind(this);

        //get user selected language from shared preferences

        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), value);
        final Resources resources = context.getResources();

        caltext =  findViewById(R.id.caltrack);
        title =  findViewById(R.id.title);
        subtitle =  findViewById(R.id.subtitle);

        caltext.setText(resources.getString(R.string.activityTracker_text3));
        title.setText(resources.getString(R.string.activityTracker_text1));
        subtitle.setText(resources.getString(R.string.activityTracker_text2));

    }
    private DatePickerDialog.OnDateSetListener dpickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            year_y=year;
            month_m=month;
            day_d=dayOfMonth;

            //DISPLAY
            Calendar cale1 = Calendar.getInstance();
            cale1.set(year_y,month_m,day_d);
            //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",
                    Locale.getDefault());
            date = df.format(cale1.getTime());
            //viewByDate(year_y,month_m,day_d);
            viewByDate();
        }
    };

    public void viewByDate()
    {
        lsItem.clear();
        adapter.notifyDataSetChanged();
        List<Contact> contacts1 = db.getAllContacts();
        for (Contact cn1 : contacts1) {

            if(date.contains(cn1.getName()))
            {
                //lsItem.removeAll(contacts1);
                Contact c = new Contact( cn1.getName(), cn1.getPhoneNumber());
                lsItem.add(c);
                Collections.reverse(lsItem);
            }



        }

    }
}