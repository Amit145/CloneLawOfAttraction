package com.apps.amit.lawofattraction.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.AffirmationActivity;
import com.apps.amit.lawofattraction.Exercise1Activity;
import com.apps.amit.lawofattraction.SelectManifestationTypeActivity;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizeData {

    public static final String DAY_COUNTER = "counter";
    WishDataBaseHandler db;
    RequestQueue requestQueue;
    Context fcontext;
    String userPrivateWish = "http://www.innovativelabs.xyz/insert_newUserPrivateWish.php";
    String loadPrivateWish = "http://innovativelabs.xyz/getPrivateWish.php";

    public SynchronizeData(Context context) {
        db = new WishDataBaseHandler(context);
        requestQueue = Volley.newRequestQueue(context);
        fcontext = context;
    }

    public void getAllPrivateWishes(String personId, String personName, String email) {

        SharedPreferences sharedPreferences = fcontext.getSharedPreferences("Affirmation_Counter", AffirmationActivity.MODE_PRIVATE);
        SharedPreferences sharedPreferencesManifestationType = fcontext.getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);

        int day = 0;
        String time = "";
        String manifestationTypeValue = "";

        if(sharedPreferences.contains(DAY_COUNTER)) {
            day = sharedPreferences.getInt(DAY_COUNTER, -1);
        }

        if(sharedPreferences.contains("Time")) {
            time = sharedPreferences.getString("Time", "");
        }

        if(sharedPreferencesManifestationType.contains("MANIFESTATION_TYPE_VALUE")) {
            manifestationTypeValue = sharedPreferencesManifestationType.getString("MANIFESTATION_TYPE_VALUE", "");
        }

        List < PrivateWishesUtils > wishList = db.getAllWishes();
        for (PrivateWishesUtils ws: wishList) {
            sendPrivateWishToServer(personId, ws.getUserName(), ws.getUserWish(), ws.getUserDate(), day, time, manifestationTypeValue, personName, email);
        }
    }

    public void loadAllPrivateWishes(final String personId) {
        String url = loadPrivateWish;
        final SharedPreferences sharedPreferences = fcontext.getSharedPreferences("Affirmation_Counter", AffirmationActivity.MODE_PRIVATE);
        final SharedPreferences sharedPreferencesManifestationType = fcontext.getSharedPreferences("MANIFESTATION_TYPE", Exercise1Activity.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SharedPreferences.Editor editor1 = sharedPreferencesManifestationType.edit();



        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener < String > () {

            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONArray jsonArr = new JSONArray(response);

                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        db.addWish(new PrivateWishesUtils(jsonObject.getString("userName"), jsonObject.getString("userWish"), jsonObject.getString("wishDate")));

                        if(sharedPreferences.contains(DAY_COUNTER) && sharedPreferences.contains("Time")) {
                           int temp =  Integer.parseInt(jsonObject.getString("affirmationCount"));
                           String temp1 =  jsonObject.getString("affirmationDate");
                           editor.putInt(DAY_COUNTER, temp);
                           editor.putString("Time", temp1);
                           editor.apply();

                        } else {
                            int temp =  Integer.parseInt(jsonObject.getString("affirmationCount"));
                            String temp1 =  jsonObject.getString("affirmationDate");
                            editor.putInt(DAY_COUNTER, temp);
                            editor.putString("Time", temp1);
                            editor.apply();
                        }

                        if(sharedPreferencesManifestationType.contains("MANIFESTATION_TYPE_VALUE")) {
                            String temp = jsonObject.getString("manifestValue");
                            editor1.putString("MANIFESTATION_TYPE_VALUE", temp);
                            editor1.apply();
                        } else {
                            String temp = jsonObject.getString("manifestValue");
                            editor1.putString("MANIFESTATION_TYPE_VALUE", temp);
                            editor1.apply();
                        }
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error.Response", "" + error.getMessage());
                }
        }) {
            @Override
            protected Map < String, String > getParams() {
                Map < String, String > params = new HashMap < >();
                params.put("id", personId);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void sendPrivateWishToServer(final String personId, final String name, final String comment, final String date, final int day, final String time, final String manifestationValue, final String personName , final String email) {

        String url = userPrivateWish;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener < String > () {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                    Log.e("Error.Response", "" + error.getMessage());

                }
        }) {
            @Override
                protected Map < String, String > getParams() {

                    Map < String, String > params = new HashMap < >();
                    params.put("id", personId);
                    params.put("personName", personName);
                    params.put("personEmail", email);
                    params.put("userName", name);
                    params.put("userWish", comment);
                    params.put("wishDate", date);
                    params.put("affirmationCount", String.valueOf(day));
                    params.put("affirmationDate", time);
                    params.put("manifestValue", manifestationValue);

                    return params;
                }
        };
        requestQueue.add(postRequest);
    }
}