package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Daily;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public interface IDailyRepository extends IBaseRepository {

    interface onGetDailyCallback {
        void onSuccess(RealmResults<Daily> daily);
        void onError(String message);
    }

    void addDaily(Daily daily, onSaveCallback callback);

    void deleteDailyById(String Id, onDeleteCallback callback);

    void deleteDailyByPosition(int position, onDeleteCallback callback);

    void updateDailyById(String id, String name, onUpdateCallback callback);

    void getAllDaily(onGetDailyCallback callback);


}
