package com.ccjeng.iwish.realm.repository;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface IBaseRepository {

    interface onSaveCallback {
        void onSuccess();
        void onError(String message);
    }

    interface onDeleteCallback {
        void onSuccess();
        void onError(String message);
    }

}
