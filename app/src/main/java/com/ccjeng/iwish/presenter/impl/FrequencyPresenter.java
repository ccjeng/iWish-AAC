package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.model.FrequencyList;
import com.ccjeng.iwish.presenter.IFrequencyPresenter;
import com.ccjeng.iwish.realm.repository.IFrequencyRepository;
import com.ccjeng.iwish.realm.repository.impl.FrequencyRepository;
import com.ccjeng.iwish.view.activity.FrequencyActivity;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/10.
 */
public class FrequencyPresenter implements IFrequencyPresenter {

    private FrequencyActivity view;
    private IFrequencyRepository repository;

    private IFrequencyRepository.onDeleteCallback onDeleteCallback;
    private IFrequencyRepository.onGetFrequencyCallback onGetFrequencyCallback;
    private IFrequencyRepository.onGetFrequencyDistinctCountCallback onGetFrequencyDistinctCountCallback;

    public FrequencyPresenter(FrequencyActivity view) {
        this.view = view;
        this.repository = new FrequencyRepository();
    }


    @Override
    public void deleteFrequencyByName(String name) {
        repository.deleteFrequencyByName(name, onDeleteCallback);
    }

    @Override
    public void getAllFrequencyDistinctCount() {
        repository.getAllFrequencyDistinctCount(onGetFrequencyDistinctCountCallback);
    }

    @Override
    public void getAllFrequency() {
        repository.getAllFrequency(onGetFrequencyCallback);
    }


    @Override
    public void subscribeCallbacks() {

        onGetFrequencyDistinctCountCallback = new IFrequencyRepository.onGetFrequencyDistinctCountCallback() {
            @Override
            public void onSuccess(List<FrequencyList> frequencies) {
                view.showData(frequencies);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onGetFrequencyCallback = new IFrequencyRepository.onGetFrequencyCallback() {
            @Override
            public void onSuccess(RealmResults<Frequency> frequencies) {
                //view.showData(frequencies);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new IFrequencyRepository.onDeleteCallback(){
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
        onDeleteCallback = null;
        onGetFrequencyCallback = null;
        onGetFrequencyDistinctCountCallback = null;
    }
}
