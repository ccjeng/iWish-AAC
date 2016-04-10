package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.model.FrequencyList;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public interface IFrequencyRepository extends IBaseRepository {

    interface onGetFrequencyCallback {
        void onSuccess(RealmResults<Frequency> frequencies);
        void onError(String message);
    }

    interface onGetFrequencyDistinctCountCallback {
        void onSuccess(List<FrequencyList> frequencies);
        void onError(String message);
    }


    void deleteFrequencyByName(String name, onDeleteCallback callback);

    void getAllFrequencyDistinctCount(onGetFrequencyDistinctCountCallback callback);

    void getAllFrequency(onGetFrequencyCallback callback);


}
