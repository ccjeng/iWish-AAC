package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.SubItem;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/27.
 */
public interface ISubItemRepository extends IBaseRepository {

    interface onGetSubItemByIdCallback {
        void onSuccess(SubItem item);
        void onError(String message);
    }

    interface onGetSubItemsCallback {
        void onSuccess(RealmList<SubItem> items);
        void onError(String message);
    }

    void addSubItem(SubItem item, onSaveCallback callback);

    void addSubItemByItemId(SubItem item, String itemId, onSaveCallback callback);

    void deleteSubItemById(String id, onDeleteCallback callback);

    void deleteSubItemByPosition(int position, onDeleteCallback callback);

    void updateSubItemById(String id, String name, onUpdateCallback callback);

    void getSubItemsByItemId(String id, onGetSubItemsCallback callback);

    void getSubItemById(String id, onGetSubItemByIdCallback callback);

}
