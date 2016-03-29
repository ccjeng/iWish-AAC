package com.ccjeng.iwish.utils;

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
public class RealmMigration {

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

            try {
                FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
                byte[] data = new byte[1024];

                FileOutputStream outputStream =new FileOutputStream(new File(newFilePath));

                while (inputStream.read(data) != -1) {
                    outputStream.write(data);
                }
                inputStream.close();
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



  /*
        Realm.deleteRealm(BaseApplication.realmConfiguration);
        Realm backupRealm = Realm.getInstance(BaseApplication.realmConfiguration);
        backupRealm.writeCopyTo(pathToRestore);
        backupRealm.close();
        //orgRealm = Realm.getInstance(orgConfig);
*/
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private String dbPath(){

        return realm.getPath();
    }
}
