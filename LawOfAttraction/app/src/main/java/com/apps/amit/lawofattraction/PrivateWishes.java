package com.apps.amit.lawofattraction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.apps.amit.lawofattraction.helper.LocaleHelper;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PrivateWishes extends AppCompatActivity {

    RecyclerView recyclerView;
    List<WishDB> lsItem;
    Button button;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    EditText edt;
    EditText edt1;
    TextView txt;
    Resources resources;
    TextView nameText;
    TextView wishText;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_wishes);

        //SharedPreferences pref = getSharedPreferences("UserLang",MODE_PRIVATE);

        //Store selected language in a Variable called value
        //final String value = pref.getString("language","en");

        Context context = LocaleHelper.setLocale(getApplicationContext(), "en");
        resources = context.getResources();

        recyclerView =  findViewById(R.id.recyclerView);
        button =  findViewById(R.id.sendPrivateWish);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PrivateWishes.this);
                final View dialogView = View.inflate(getApplicationContext(),R.layout.wish_options, null);
                builder.setCancelable(false);
                builder.setMessage(resources.getString(R.string.subUniverse_dialogTitle));

                builder.setView(dialogView);
                final AlertDialog alert = builder.create();
                alert.show();

                nameText =  dialogView.findViewById(R.id.name);
                wishText =  dialogView.findViewById(R.id.wish);
                edt =  dialogView.findViewById(R.id.username);
                edt1 =  dialogView.findViewById(R.id.comments);
                Button submit =  dialogView.findViewById(R.id.btnsubmit);
                Button cancel =  dialogView.findViewById(R.id.btncancel);

                nameText.setText(resources.getString(R.string.subUniverse_dialogName));
                wishText.setText(resources.getString(R.string.subUniverse_dialogWish));
                submit.setText(resources.getString(R.string.subUniverse_dialogSubmit));
                cancel.setText(resources.getString(R.string.subUniverse_dialogCancel));

                SharedPreferences sp1 = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
                String naam = sp1.getString("userName","");

                if((naam.equalsIgnoreCase("")))
                {
                    edt.setText("");

                } else{

                    edt.setText(naam);
                }

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(buttonClick);

                            personalWish(alert);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(buttonClick);
                        alert.dismiss();
                    }
                });
            }
        });

    }

    private void loadData() {

        lsItem = new ArrayList<>();

        WishDataBaseHandler db = new WishDataBaseHandler(this);

        List<WishDB> wishes = db.getAllWishes();

        for (WishDB ws : wishes) {

            WishDB wishDB = new WishDB(ws.getUserName(),ws.getUserWish(),ws.getUserDate());
            lsItem.add(wishDB);
            Collections.reverse(lsItem);
        }

        adapter = new PrivateWishRecyclerAdapter(lsItem,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public void personalWish( AlertDialog alert){

        String userName = edt.getText().toString();
        String userComment = edt1.getText().toString();
        String time =  DateFormat.getDateTimeInstance().format(new Date());

        if (TextUtils.isEmpty(userName)  )
        {

            edt.setError(resources.getString(R.string.enterName));

        }
        else if ( TextUtils.isEmpty(userComment) )
        {

            edt1.setError(resources.getString(R.string.enterWish));

        } else {

            SharedPreferences timerEnable = getSharedPreferences("timerEnable", experiences.MODE_PRIVATE);
            SharedPreferences.Editor editor = timerEnable.edit();
            editor.putString("userName", userName);
            editor.apply();

            WishDataBaseHandler db = new WishDataBaseHandler(this);

            db.addWish(new WishDB(userName, userComment, time));
            alert.dismiss();

            loadData();
        }

    }
}
