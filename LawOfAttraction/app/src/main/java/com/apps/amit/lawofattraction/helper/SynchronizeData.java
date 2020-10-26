package com.apps.amit.lawofattraction.helper;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.amit.lawofattraction.sqlitedatabase.WishDataBaseHandler;
import com.apps.amit.lawofattraction.utils.PrivateWishesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizeData {

    WishDataBaseHandler db;
    RequestQueue requestQueue;
    String userPrivateWish = "http://www.innovativelabs.xyz/insert_newUserPrivateWish.php";
    String loadPrivateWish = "http://innovativelabs.xyz/getPrivateWish.php";

    public SynchronizeData(Context context) {
        db = new WishDataBaseHandler(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getAllPrivateWishes(String personId) {
        List < PrivateWishesUtils > wishList = db.getAllWishes();
        for (PrivateWishesUtils ws: wishList) {
            sendPrivateWishToServer(personId, ws.getUserName(), ws.getUserWish(), ws.getUserDate());
        }
    }

    public void loadAllPrivateWishes(final String personId) {
        String url = loadPrivateWish;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener < String > () {

            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONArray jsonArr = new JSONArray(response);

                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        db.addWish(new PrivateWishesUtils(jsonObject.getString("userName"), jsonObject.getString("userWish"), jsonObject.getString("wishDate")));
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

    public void sendPrivateWishToServer(final String personId, final String name, final String comment, final String date) {

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
                    params.put("userName", name);
                    params.put("userWish", comment);
                    params.put("wishDate", date);

                    return params;
                }
        };
        requestQueue.add(postRequest);
    }
}