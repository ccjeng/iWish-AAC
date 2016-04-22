package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Introduce;
import com.ccjeng.iwish.presenter.IIntroducePresenter;
import com.ccjeng.iwish.realm.repository.IIntroduceRepository;
import com.ccjeng.iwish.realm.repository.impl.IntroduceRepository;
import com.ccjeng.iwish.view.activity.IntroduceActivity;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public class IntroducePresenter implements IIntroducePresenter {

    private IntroduceActivity view;
    private IIntroduceRepository repository;

    private IIntroduceRepository.onSaveCallback onSaveCallback;
    private IIntroduceRepository.onDeleteCallback onDeleteCallback;
    private IIntroduceRepository.onGetIntroduceCallback onGetIntroduceCallback;
    private IIntroduceRepository.onUpdateCallback onUpdateCallback;

    public IntroducePresenter(IntroduceActivity view) {
        this.view = view;
        repository = new IntroduceRepository();
    }
    @Override
    public void addIntroduce(Introduce introduce) {
        repository.addIntroduce(introduce, onSaveCallback);
    }

    @Override
    public void deleteIntroduceById(String id) {
        repository.deleteIntroduceById(id, onDeleteCallback);
    }

    @Override
    public void updateIntroduceById(String id, String name) {
        repository.updateIntroduceById(id, name, onUpdateCallback);
    }

    @Override
    public void getAllIntroduce() {
        repository.getAllIntroduce(onGetIntroduceCallback);
    }

    @Override
    public void saveOrder(List<Introduce> list) {
        repository.saveOrder(list, onSaveCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onGetIntroduceCallback = new IIntroduceRepository.onGetIntroduceCallback() {
            @Override
            public void onSuccess(List<Introduce> introduces) {
                view.showData(introduces);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onSaveCallback = new IIntroduceRepository.onSaveCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new IIntroduceRepository.onDeleteCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.deleted);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new IIntroduceRepository.onUpdateCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveCallback = null;
        onDeleteCallback = null;
        onGetIntroduceCallback = null;
        onUpdateCallback = null;
    }
}
