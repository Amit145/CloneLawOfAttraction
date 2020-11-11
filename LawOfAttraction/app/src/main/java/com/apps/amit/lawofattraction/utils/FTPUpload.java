package com.apps.amit.lawofattraction.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPUpload extends AsyncTask<String, Void, Void> {

    public File [] fileName;
    public String valueId;
    public String temp;
    Context context;

    public FTPUpload(File[] fileName, String valueId, String temp, Context context) {
        this.fileName = fileName;
        this.valueId = valueId;
        this.temp = temp;
        this.context = context;
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

                String jsonFilePath = valueId+"/JSON";
                String audioFilePath = valueId+"/Audio";

                //Audios
                boolean result = FTPUpload.makeDirectories(con, audioFilePath);
                for (File file: fileName) {

                    if(result) {

                        con.setFileType(FTP.BINARY_FILE_TYPE);
                        FileInputStream in = new FileInputStream(file);
                        con.changeWorkingDirectory(audioFilePath);
                        con.storeFile(file.getName(), in);
                        in.close();
                    }
                    con.changeToParentDirectory();
                }

                //JsonFile

                boolean result1 = FTPUpload.makeDirectories(con, jsonFilePath);
                if(result1) {
                    con.changeWorkingDirectory(jsonFilePath);
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    File tempFile = new File(temp);
                    FileInputStream in = new FileInputStream(tempFile);

                    con.storeFile(tempFile.getName(), in);
                    in.close();
                }

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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(context,"Completed ! ... Synchronizing Data",Toast.LENGTH_LONG).show();
    }

    public static boolean makeDirectories(FTPClient ftpClient, String dirPath)
            throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {
            for (String singleDir : pathElements) {
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        System.out.println("CREATED directory: " + singleDir);
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        System.out.println("COULD NOT create directory: " + singleDir);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
