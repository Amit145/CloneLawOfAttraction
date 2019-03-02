//Package Name of APP on PlayStore
package com.apps.amit.lawofattraction;

//All import statements Required for Current Java File
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import com.bumptech.glide.Glide;

//Main Class
public class AboutApp extends AppCompatActivity {

    ImageView img3;
    TextView title;
    TextView subtitle;
    TextView text0;
    TextView text1;


    // Override methods of Current Activity
    @Override
    protected void onDestroy() {
        super.onDestroy();

        img3=null;
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aboutapp);                                        // Set Content Layout (Activity.xml) for current Activity

        //For Version3.3
        /*
        SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);     //Get user Selected Language from Shared preference

        */
        String value1 = "en";

        //Store selected language in a Variable called value
        Context context = LocaleHelper.setLocale(getApplicationContext(), value1);
        Resources resources = context.getResources();

        //Find all Id's from Activity.xml in Activity.java

        img3 = findViewById(R.id.imageView6);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        text0 = findViewById(R.id.text0);
        text1 = findViewById(R.id.text1);

        //Set Text based on user choosen language, get Language specific text from Resources for specified language

        title.setText(resources.getString(R.string.aboutApp));
        subtitle.setText(resources.getString(R.string.aboutApp1));
        text0.setText(resources.getString(R.string.aboutApp2));
        text1.setText(resources.getString(R.string.aboutApp3));

        Glide.with(getApplicationContext()).load(R.drawable.lawimg).thumbnail(0.1f).fitCenter().into(img3);

    }
}
