package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.SubItem;
import com.ccjeng.iwish.presenter.ISubItemPresenter;
import com.ccjeng.iwish.realm.repository.IBaseRepository;
import com.ccjeng.iwish.realm.repository.ISubItemRepository;
import com.ccjeng.iwish.realm.repository.impl.SubItemRepository;
import com.ccjeng.iwish.view.SubItemActivity;

import io.realm.RealmList;

/**
 * Created by andycheng on 2016/3/28.
 */
public class SubItemPresenter implements ISubItemPresenter {

    private SubItemActivity view;
    private ISubItemRepository.onSaveCallback onSaveCallback;
    private ISubItemRepository.onUpdateCallback onUpdateCallback;
    private ISubItemRepository.onDeleteCallback onDeleteCallback;
    private ISubItemRepository.onGetSubItemByIdCallback onGetSubItemByIdCallback;
    private ISubItemRepository.onGetSubItemsCallback onGetSubItemsCallback;

    private ISubItemRepository subItemRepository;

    public SubItemPresenter(SubItemActivity view) {
        this.view = view;
        subItemRepository = new SubItemRepository();
    }

    @Override
    public void addSubItem(SubItem subItem) {
        subItemRepository.addSubItem(subItem, onSaveCallback);
    }

    @Override
    public void addSubItemByItemId(SubItem subItem, String itemId) {
        subItemRepository.addSubItemByItemId(subItem, itemId, onSaveCallback);
    }

    @Override
    public void deleteSubItemByPosition(int position) {
        subItemRepository.deleteSubItemByPosition(position, onDeleteCallback);
    }

    @Override
    public void deleteSubItemById(String id) {
        subItemRepository.deleteSubItemById(id, onDeleteCallback);
    }

    @Override
    public void updateSubItemById(String id, String name) {
        subItemRepository.updateSubItemById(id, name, onUpdateCallback);
    }

    @Override
    public void getSubItemById(String id) {
        subItemRepository.getSubItemById(id, onGetSubItemByIdCallback);
    }

    @Override
    public void getAllSubItemsByItemId(String itemId) {
        subItemRepository.getSubItemsByItemId(itemId, onGetSubItemsCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveCallback = new ISubItemRepository.onSaveCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new ISubItemRepository.onDeleteCallback() {

            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.deleted);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new ISubItemRepository.onUpdateCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }
        };

        onGetSubItemByIdCallback = new ISubItemRepository.onGetSubItemByIdCallback() {
            @Override
            public void onSuccess(SubItem subItem) {
            }

            @Override
            public void onError(String message) {

            }
        };

        onGetSubItemsCallback = new ISubItemRepository.onGetSubItemsCallback() {

            @Override
            public void onSuccess(RealmList<SubItem> items) {
                view.showData(items);
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
        onUpdateCallback = null;
        onGetSubItemByIdCallback = null;
        onGetSubItemsCallback = null;
    }
}
