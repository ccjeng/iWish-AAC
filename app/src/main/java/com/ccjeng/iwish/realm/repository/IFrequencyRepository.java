package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Frequency;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public interface IFrequencyRepository extends IBaseRepository {

    interface onGetFrequencyCallback {
        void onSuccess(RealmResults<Frequency> frequencies);
        void onError(String message);
    }

    void addFrequency(Frequency frequency, onSaveCallback callback);

    void deleteFrequencyById(String Id, onDeleteCallback callback);

    void deleteFrequencyByPosition(int position, onDeleteCallback callback);

    void updateFrequencyById(String id, String name, onUpdateCallback callback);

    void getAllFrequency(onGetFrequencyCallback callback);

}
