package com.apps.amit.lawofattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class affirmationactivity extends AppCompatActivity {

    TextView count,affirmationTitle,affirmationSubTitle;
    private Affirmations affirmations ;
    Button button;
    int counter = 0 ;
    String s = null;
    DatabaseHandler db;
    Calendar cal;
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
        } else {
            counter = 0 ;
            s = affirmations.getList().get(counter);
        }

        int displayCount = counter + 1;
        //displayCount = displayCount++;
        count.setText("#"+displayCount);
        affirmationTitle.setText(s);
        affirmationSubTitle.setText(s);

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

                //Toast.makeText(getApplicationContext(), "Counter++ "+counter +" Time "+cal.getTimeInMillis(), Toast.LENGTH_LONG).show();
                SharedPreferences sp = getSharedPreferences("Affirmation_Counter", affirmationactivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("counter",counter);
                editor.putString("Time",String.valueOf(cal.getTime()));
                editor.apply();
            }
        });


    }
}
