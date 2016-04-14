package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.presenter.IItemPresenter;
import com.ccjeng.iwish.realm.repository.IItemRepository;
import com.ccjeng.iwish.realm.repository.impl.ItemRepository;
import com.ccjeng.iwish.view.activity.ItemActivity;

import java.util.List;

/**
 * Created by andycheng on 2016/3/26.
 */
public class ItemPresenter implements IItemPresenter{

    private ItemActivity view;
    private IItemRepository.onDeleteCallback onDeleteCallback;
    private IItemRepository.onSaveCallback onSaveCallback;
    private IItemRepository.onUpdateCallback onUpdateCallback;
    private IItemRepository.onGetItemByIdCallback onGetItemByIdCallback;
    private IItemRepository.onGetAllItemsCallback onGetAllItemsCallback;

    private IItemRepository itemRepository;

    public ItemPresenter(ItemActivity view) {
        this.view = view;
        itemRepository = new ItemRepository();
    }

    @Override
    public void addItem(Item item) {
        itemRepository.addItem(item, onSaveCallback);
    }

    @Override
    public void deleteItemByPosition(int position) {
        itemRepository.deleteItemByPosition(position, onDeleteCallback);
    }

    @Override
    public void deleteItemById(String id) {
        itemRepository.deleteItemById(id, onDeleteCallback);
    }

    @Override
    public void updateItemById(String id, String name) {
        itemRepository.updateItemById(id, name, onUpdateCallback);
    }

    @Override
    public void getItemById(String id) {
        itemRepository.getItemById(id, onGetItemByIdCallback);
    }


    @Override
    public void getAllItems() {
        itemRepository.getAllItems(onGetAllItemsCallback);
    }

    @Override
    public void saveOrder(List<Item> itemList) {
        itemRepository.saveOrder(itemList, onSaveCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onSaveCallback = new IItemRepository.onSaveCallback() {

            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new IItemRepository.onDeleteCallback() {

            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.deleted);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new IItemRepository.onUpdateCallback(){
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onGetItemByIdCallback = new IItemRepository.onGetItemByIdCallback() {

            @Override
            public void onSuccess(Item item) {
            }

            @Override
            public void onError(String message) {

            }
        };

        onGetAllItemsCallback = new IItemRepository.onGetAllItemsCallback() {

            @Override
            public void onSuccess(List<Item> items) {
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
        onDeleteCallback = null;
        onSaveCallback = null;
        onUpdateCallback = null;
        onGetItemByIdCallback = null;
        onGetAllItemsCallback = null;
    }
}
