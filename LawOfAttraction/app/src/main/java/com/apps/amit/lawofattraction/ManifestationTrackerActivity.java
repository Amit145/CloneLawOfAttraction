package com.apps.amit.lawofattraction;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.apps.amit.lawofattraction.adapters.RecyclerAdapter;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.apps.amit.lawofattraction.sqlitedatabase.ActivityTrackerDatabaseHandler;
import com.apps.amit.lawofattraction.utils.ManifestationTrackerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;


public class ManifestationTrackerActivity extends AppCompatActivity {

    ActivityTrackerDatabaseHandler db;
    Button caltext;
    TextView title;
    TextView subtitle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ManifestationTrackerUtils> lsItem;
    int year;
    int month;
    int day;
    String date;
    static  final int DIALOG_ID=0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        db.close();
        adapter=null;
        recyclerView=null;
        Intent art1 = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(art1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final Calendar cale = Calendar.getInstance();
        year=cale.get(Calendar.YEAR);
        month=cale.get(Calendar.MONTH);
        day=cale.get(Calendar.DAY_OF_MONTH);

        showDialogOnButtonClick();

        caltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });

        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lsItem = new ArrayList<>();

        db= new ActivityTrackerDatabaseHandler(getApplicationContext());

        List<ManifestationTrackerUtils> manifestationTrackerUtils = db.getAllContacts();

        for (ManifestationTrackerUtils cn : manifestationTrackerUtils) {

            ManifestationTrackerUtils c = new ManifestationTrackerUtils(cn.getName(),cn.getPhoneNumber());
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
            return new DatePickerDialog(this,dpickerListener,year,month,day);
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

            //DISPLAY
            Calendar cale1 = Calendar.getInstance();
            cale1.set(year,month,dayOfMonth);
            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",
                    Locale.getDefault());
            date = df.format(cale1.getTime());
            viewByDate();
        }
    };

    public void viewByDate()
    {
        lsItem.clear();
        adapter.notifyDataSetChanged();
        List<ManifestationTrackerUtils> contacts1 = db.getAllContacts();
        for (ManifestationTrackerUtils cn1 : contacts1) {

            if(date.contains(cn1.getName()))
            {
                ManifestationTrackerUtils c = new ManifestationTrackerUtils( cn1.getName(), cn1.getPhoneNumber());
                lsItem.add(c);
                Collections.reverse(lsItem);
            }
        }
    }
}