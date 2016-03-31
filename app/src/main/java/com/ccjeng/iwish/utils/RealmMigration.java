package com.ccjeng.iwish.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ccjeng.iwish.view.base.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

/**
 * Created by andycheng on 2016/3/29.
 */
public class RealmMigration  {

    private final static String TAG = RealmMigration.class.getName();

    private Context context;
    private Realm realm;


    public RealmMigration(Context context) {
        this.realm = Realm.getInstance(BaseApplication.realmConfiguration);
        this.context = context;
    }

    public void backup() {

        File exportRealmFile = null;

        File exportRealmPATH = context.getExternalFilesDir(null);
        String exportRealmFileName = "default.realm";

        Log.d(TAG, "Realm DB Path = "+realm.getPath());

        try {
            // get or create an "export.realm" file
            exportRealmFile = new File(exportRealmPATH, exportRealmFileName);

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String msg =  "File exported to Path: "+ context.getExternalFilesDir(null);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, msg);


        realm.close();

    }

    public void restore() {

        //Restore
        File exportRealmPATH = context.getExternalFilesDir(null);
        String FileName = "default.realm";

        String oldFilePath = context.getExternalFilesDir(null) + "/"+FileName;
        String newFilePath = this.dbPath();

        Log.d(TAG, "oldFilePath = " + oldFilePath);
        Log.d(TAG, "newFilePath = " + newFilePath);


        if(fileExists(exportRealmPATH.toString()) != false){

            copyBundledRealmFile(oldFilePath, FileName);

            String msg =  "Data restore is done";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, msg);

        }

    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }


    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(context.getFilesDir(), outFileName);

            Log.d(TAG, "context.getFilesDir() = " + context.getFilesDir().toString());
            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));


            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dbPath(){

        return realm.getPath();
    }
}
