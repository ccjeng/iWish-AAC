package com.ccjeng.iwish.realm.repository;

import com.android.internal.util.Predicate;
import com.ccjeng.iwish.model.Item;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface IItemRepository extends IBaseRepository {

    interface onGetItemByIdCallback {
        void onSuccess(Item item);
        void onError(String message);
    }

    interface onGetAllItemsCallback {
        void onSuccess(RealmResults<Item> items);
        void onError(String message);
    }

    interface onGetItemsCallback {
        void onSuccess(RealmList<Item> items);
        void onError(String message);
    }

    void addItem(Item item, onSaveCallback callback);

    void deleteItemById(String id, onDeleteCallback callback);

    void deleteItemByPosition(int position, onDeleteCallback callback);

    void updateItemById(String id, String name, onUpdateCallback callback);

    void getItemById(String id, onGetItemByIdCallback callback);

    void getAllItems(onGetAllItemsCallback callback);

}
