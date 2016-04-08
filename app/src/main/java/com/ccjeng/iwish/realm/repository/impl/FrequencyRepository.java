package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Daily;
import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.realm.repository.IBaseRepository;
import com.ccjeng.iwish.realm.repository.IFrequencyRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public class FrequencyRepository implements IFrequencyRepository {
    @Override
    public void addFrequency(Frequency frequency, onSaveCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Frequency f = realm.createObject(Frequency.class);
        f.setId(Utils.getUniqueID());
        f.setName(frequency.getName());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteFrequencyById(String Id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Frequency f = realm.where(Frequency.class).equalTo(RealmTable.ID, Id).findFirst();
        f.removeFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteFrequencyByPosition(int position, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmQuery<Frequency> query = realm.where(Frequency.class);
        RealmResults<Frequency> results = query.findAll();
        results.remove(position);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateFrequencyById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Frequency f = realm.where(Frequency.class).equalTo(RealmTable.ID, id).findFirst();
        f.setName(name);
        realm.copyToRealmOrUpdate(f);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllFrequency(onGetFrequencyCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmQuery<Frequency> query = realm.where(Frequency.class);
        RealmResults<Frequency> results = query.findAll();

        if (callback != null)
            callback.onSuccess(results);
    }
}
