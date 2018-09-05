package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.model.FrequencyList;
import com.ccjeng.iwish.realm.repository.IFrequencyRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.view.base.BaseApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public class FrequencyRepository implements IFrequencyRepository {

    private static final String TAG = FrequencyRepository.class.getSimpleName();

    @Override
    public void deleteFrequencyByName(String name, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmResults<Frequency> results = realm.where(Frequency.class).equalTo(RealmTable.Frequency.NAME, name).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }


    @Override
    public void getAllFrequency(onGetFrequencyCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Frequency> results = realm.where(Frequency.class).findAll();

        if (callback != null)
            callback.onSuccess(results);
    }


    @Override
    public void getAllFrequencyDistinctCount(onGetFrequencyDistinctCountCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Frequency> results = realm.where(Frequency.class).distinct(RealmTable.Frequency.NAME).findAll();

        List<FrequencyList> list = new ArrayList<FrequencyList>();

        for (int i =0; i < results.size(); i++) {
            String name = results.get(i).getName();
            RealmResults<Frequency> s = realm.where(Frequency.class).equalTo(RealmTable.Frequency.NAME, name).findAll();
            long sum = s.sum(RealmTable.Frequency.COUNT).longValue();

            //Log.d(TAG, name + " - "+ sum);

            FrequencyList frequencyList = new FrequencyList(name, sum);
            list.add(frequencyList);

        }

        Collections.sort(list);

        if (callback != null)
            callback.onSuccess(list);
    }

}
