package com.ccjeng.iwish.view.base;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by andycheng on 2016/3/26.
 */
public class BaseApplication extends Application {

    public static RealmConfiguration realmConfiguration;
    @Override
    public void onCreate() {
        super.onCreate();

        realmConfiguration = new RealmConfiguration
                .Builder(this)
                .build();

    }

}
