package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Daily;
import com.ccjeng.iwish.realm.repository.IDailyRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public class DailyRepository implements IDailyRepository {

    @Override
    public void addDaily(Daily daily, onSaveCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Daily d = realm.createObject(Daily.class);
        d.setId(Utils.getUniqueID());
        d.setName(daily.getName());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteDailyById(String Id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Daily d = realm.where(Daily.class).equalTo(RealmTable.ID, Id).findFirst();
        d.removeFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteDailyByPosition(int position, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmQuery<Daily> query = realm.where(Daily.class);
        RealmResults<Daily> results = query.findAll();
        results.remove(position);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateDailyById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Daily d = realm.where(Daily.class).equalTo(RealmTable.ID, id).findFirst();
        d.setName(name);
        realm.copyToRealmOrUpdate(d);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllDaily(onGetDailyCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmQuery<Daily> query = realm.where(Daily.class);
        RealmResults<Daily> results = query.findAll();

        if (callback != null)
            callback.onSuccess(results);
    }
}
