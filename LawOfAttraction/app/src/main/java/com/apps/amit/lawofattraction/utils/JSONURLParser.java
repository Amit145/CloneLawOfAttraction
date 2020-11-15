package com.apps.amit.lawofattraction.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONURLParser extends AsyncTask < String, Void, Void >{
    String jsonUrl = "http://www.innovativelabs.xyz/ftp.json";
    JSONObject jsonObject;
    Context applicationContext;

    public JSONURLParser(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected Void doInBackground(String...strings) {
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200){
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            }
            else {
                InputStream inputStream = url.openStream();
                InputStreamReader isReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuilder sb = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                reader.close();
                jsonObject = new JSONObject(sb.toString());
            }
        } catch (Exception e) {
            Log.d("ERROR", String.valueOf(e));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            SharedPreferences sp = applicationContext.getSharedPreferences("FtpLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("ftphost", new String(Base64.decodeBase64(jsonObject.getString("ftp").getBytes())));
            editor.putString("ftpuser", new String(Base64.decodeBase64(jsonObject.getString("user").getBytes())));
            editor.putString("ftppass", new String(Base64.decodeBase64(jsonObject.getString("pass").getBytes())));
            editor.apply();
        } catch (Exception e){
            Log.d("ERRROR", String.valueOf(e));
        }
    }
}
