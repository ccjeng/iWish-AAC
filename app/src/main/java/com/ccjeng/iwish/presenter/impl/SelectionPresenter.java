package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Selection;
import com.ccjeng.iwish.presenter.ISelectionPresenter;
import com.ccjeng.iwish.realm.repository.ISelectionRepository;
import com.ccjeng.iwish.realm.repository.impl.SelectionRepository;
import com.ccjeng.iwish.view.activity.SelectionActivity;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public class SelectionPresenter implements ISelectionPresenter {

    private SelectionActivity view;
    private ISelectionRepository repository;

    private ISelectionRepository.onSaveCallback onSaveCallback;
    private ISelectionRepository.onDeleteCallback onDeleteCallback;
    private ISelectionRepository.onGetSelectionCallback onGetSelectionCallback;
    private ISelectionRepository.onUpdateCallback onUpdateCallback;

    public SelectionPresenter(SelectionActivity view) {
        this.view = view;
        this.repository = new SelectionRepository();
    }

    @Override
    public void addSelection(Selection selection) {
        repository.addSelection(selection, onSaveCallback);
    }

    @Override
    public void deleteSelectionById(String id) {
        repository.deleteSelectionById(id, onDeleteCallback);
    }

    @Override
    public void updateSelectionById(String id, String name) {
        repository.updateSelectionById(id, name, onUpdateCallback);
    }

    @Override
    public void getAllSelection() {
        repository.getAllSelection(onGetSelectionCallback);
    }

    @Override
    public void saveOrder(List<Selection> list) {
        repository.saveOrder(list, onSaveCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onGetSelectionCallback = new ISelectionRepository.onGetSelectionCallback() {
            @Override
            public void onSuccess(List<Selection> selections) {
                view.showData(selections);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onSaveCallback = new ISelectionRepository.onSaveCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new ISelectionRepository.onUpdateCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new ISelectionRepository.onDeleteCallback() {
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
        onGetSelectionCallback = null;
        onSaveCallback = null;
        onDeleteCallback = null;
        onUpdateCallback = null;
    }
}
