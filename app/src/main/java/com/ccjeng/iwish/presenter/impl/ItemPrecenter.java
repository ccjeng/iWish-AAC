package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.presenter.IItemPresenter;
import com.ccjeng.iwish.realm.repository.ICategoryRepository;
import com.ccjeng.iwish.realm.repository.IItemRepository;
import com.ccjeng.iwish.realm.repository.impl.CategoryRepository;
import com.ccjeng.iwish.realm.repository.impl.ItemRepository;
import com.ccjeng.iwish.view.ItemActivity;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class ItemPrecenter implements IItemPresenter{

    private ItemActivity view;
    private IItemRepository.onDeleteCallback onDeleteCallback;
    private IItemRepository.onSaveCallback onSaveCallback;
    private IItemRepository.onUpdateCallback onUpdateCallback;
    private IItemRepository.onGetAllItemsCallback onGetAllItemsCallback;
    private IItemRepository.onGetItemByIdCallback onGetItemByIdCallback;
    private IItemRepository.onGetItemsCallback onGetItemsCallback;

    private IItemRepository itemRepository;

    public ItemPrecenter(ItemActivity view) {
        this.view = view;
        itemRepository = new ItemRepository();
    }

    @Override
    public void addItem(Item item) {
        itemRepository.addItem(item, onSaveCallback);
    }

    @Override
    public void addItemByCategoryId(Item item, String categoryId) {
        itemRepository.addItemByCategoryId(item, categoryId, onSaveCallback);
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
    public void getAllItemsByCategoryId(String categoryId) {
        itemRepository.getItemsByCategoryId(categoryId, onGetItemsCallback);
    }

    @Override
    public void getAllItems() {
        itemRepository.getAllItems(onGetAllItemsCallback);
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

        onGetAllItemsCallback = new IItemRepository.onGetAllItemsCallback() {
            @Override
            public void onSuccess(RealmResults<Item> items) {

            }

            @Override
            public void onError(String message) {

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

        onGetItemsCallback = new IItemRepository.onGetItemsCallback() {

            @Override
            public void onSuccess(RealmList<Item> items) {
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
        onGetAllItemsCallback = null;
        onGetItemByIdCallback = null;
        onGetItemsCallback = null;
    }
}
