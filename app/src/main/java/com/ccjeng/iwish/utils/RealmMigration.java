package com.ccjeng.iwish.utils;

import android.content.Context;
import android.os.Environment;
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

    private final static String TAG = RealmMigration.class.getSimpleName();

    private Context context;
    private Realm realm;

    private final String backupPATH =  Environment.getExternalStorageDirectory() + File.separator + "iWash/backup";
    private final String restorePATH =  Environment.getExternalStorageDirectory() + File.separator + "iWash/restore";
    private final String realmDataFileName = "default.realm";

    public RealmMigration(Context context) {
        this.realm = Realm.getInstance(BaseApplication.realmConfiguration);
        this.context = context;
    }

    public void backup() {

        File backupRealmFile = null;

        File backupRealmPATH = new File(backupPATH);

        File restoreRealmPATH = new File(restorePATH);

        if (!fileExists(backupRealmPATH.toString())) {
            backupRealmPATH.mkdirs();
        }

        if (!fileExists(restoreRealmPATH.toString())) {
            restoreRealmPATH.mkdirs();
        }


        try {
            // create a backup file
            backupRealmFile = new File(backupRealmPATH, realmDataFileName);

            // if backup file already exists, delete it
            if (backupRealmFile.exists()) {
                backupRealmFile.delete();
            }

            // copy current realm to backup file
            realm.writeCopyTo(backupRealmFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String msg =  "File exported to Path: "+ backupPATH;
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Log.d(TAG, msg);


        realm.close();

    }

    public void restore() {

        String restoreFullPath = restorePATH + "/"+ realmDataFileName;

        Log.d(TAG, "oldFilePath = " + restoreFullPath);

        File restoreRealmPATH = new File(restorePATH);
        if (!fileExists(restoreRealmPATH.toString())) {
            restoreRealmPATH.mkdirs();
        }

        if(fileExists(restoreFullPath.toString())){

            copyBundledRealmFile(restoreFullPath, realmDataFileName);

            Toast.makeText(context, "Data restore is done, please reopen the app", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Backup File does not exist", Toast.LENGTH_LONG).show();

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
        results.deleteAllFromRealm();
        realm.commitTransaction();

        Toast.makeText(context, R.string.deleted, Toast.LENGTH_LONG).show();
    }
}
