package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Daily;
import com.ccjeng.iwish.presenter.IDailyPresenter;
import com.ccjeng.iwish.realm.repository.IDailyRepository;
import com.ccjeng.iwish.realm.repository.impl.DailyRepository;
import com.ccjeng.iwish.view.activity.DailyActivity;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/8.
 */
public class DailyPresenter implements IDailyPresenter {

    private DailyActivity view;
    private IDailyRepository repository;

    private IDailyRepository.onGetDailyCallback onGetDailyCallback;
    private IDailyRepository.onDeleteCallback onDeleteCallback;
    private IDailyRepository.onSaveCallback onSaveCallback;
    private IDailyRepository.onUpdateCallback onUpdateCallback;


    public DailyPresenter(DailyActivity view) {
        this.view = view;
        repository = new DailyRepository();
    }

    @Override
    public void addDaily(Daily daily) {
        repository.addDaily(daily, onSaveCallback);
    }

    @Override
    public void deleteDailyByPosition(int position) {
        repository.deleteDailyByPosition(position, onDeleteCallback);
    }

    @Override
    public void deleteDailyById(String id) {
        repository.deleteDailyById(id, onDeleteCallback);
    }

    @Override
    public void updateDailyById(String id, String name) {
        repository.updateDailyById(id, name, onUpdateCallback);
    }

    @Override
    public void getAllDaily() {
        repository.getAllDaily(onGetDailyCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onGetDailyCallback = new IDailyRepository.onGetDailyCallback(){

            @Override
            public void onSuccess(RealmResults<Daily> daily) {
                view.showData(daily);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onSaveCallback = new IDailyRepository.onSaveCallback(){

            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new IDailyRepository.onUpdateCallback(){
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new IDailyRepository.onDeleteCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.deleted);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

    }

    @Override
    public void unSubscribeCallbacks() {
        onGetDailyCallback = null;
        onDeleteCallback = null;
        onSaveCallback = null;
        onUpdateCallback = null;
    }
}
