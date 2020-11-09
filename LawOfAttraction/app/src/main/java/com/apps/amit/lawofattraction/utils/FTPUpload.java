package com.apps.amit.lawofattraction.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

public class FTPUpload extends AsyncTask<String, Void, Void> {

    public String fileName;
    public String valueId;

    public FTPUpload(String fileName, String valueId) {
        this.fileName = fileName;
        this.valueId = valueId;

    }

    @Override
    protected Void doInBackground(String... files) {
        FTPClient con = null;

        try
        {
            con = new FTPClient();
            //ftp://31.170.161.63
            con.connect("ftp.innovativelabs.xyz");

            if (con.login("u941116359.amitg145", "4aR|i3I4N"))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = fileName;

                FileInputStream in = new FileInputStream(new File(data));
                con.makeDirectory(valueId);
                boolean result = con.storeFile(valueId+"/2.mp3", in);
                in.close();
                if (result) Log.d("upload result", "succeeded");
                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
