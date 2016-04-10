package com.ccjeng.iwish.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.view.base.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

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
            // create a backup file
            exportRealmFile = new File(exportRealmPATH, exportRealmFileName);

            // if backup file already exists, delete it
            exportRealmFile.delete();

            // copy current realm to backup file
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

        String restoreFilePath = context.getExternalFilesDir(null) + "/"+FileName;

        Log.d(TAG, "oldFilePath = " + restoreFilePath);

        if(fileExists(exportRealmPATH.toString()) != false){

            copyBundledRealmFile(restoreFilePath, FileName);

            Toast.makeText(context, "Data restore is done", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Data restore is done");

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


    public void FrequencyClear(){
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmResults<Frequency> results = realm.where(Frequency.class).findAll();
        results.clear();
        realm.commitTransaction();

        Toast.makeText(context, R.string.deleted, Toast.LENGTH_LONG).show();
    }
}
