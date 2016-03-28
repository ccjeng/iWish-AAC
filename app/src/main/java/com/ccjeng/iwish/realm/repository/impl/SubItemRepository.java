package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.model.SubItem;
import com.ccjeng.iwish.realm.repository.ISubItemRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/27.
 */
public class SubItemRepository implements ISubItemRepository {
    @Override
    public void addSubItem(SubItem item, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        SubItem s = realm.createObject(SubItem.class);
        s.setId(Utils.getUniqueID());
        s.setName(item.getName());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void addSubItemByItemId(SubItem item, String itemId, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();

        SubItem s = realm.createObject(SubItem.class);

        s.setId(Utils.getUniqueID());
        s.setName(item.getName());

        Item i = realm.where(Item.class).equalTo(RealmTable.ID, itemId).findFirst();
        i.getSubItems().add(s);

        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteSubItemById(String id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        SubItem result = realm.where(SubItem.class).equalTo(RealmTable.ID, id).findFirst();
        result.removeFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteSubItemByPosition(int position, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmQuery<SubItem> query = realm.where(SubItem.class);
        RealmResults<SubItem> results = query.findAll();
        results.remove(position);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateSubItemById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        SubItem s = realm.where(SubItem.class).equalTo(RealmTable.ID, id).findFirst();
        s.setName(name);
        realm.copyToRealmOrUpdate(s);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getSubItemsByItemId(String id, onGetSubItemsCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        Item i = realm.where(Item.class).equalTo(RealmTable.ID, id).findFirst();
        RealmList<SubItem> items = i.getSubItems();

        if (callback != null)
            callback.onSuccess(items);
    }

    @Override
    public void getSubItemById(String id, onGetSubItemByIdCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        SubItem s = realm.where(SubItem.class).equalTo(RealmTable.ID, id).findFirst();

        if (callback != null)
            callback.onSuccess(s);
    }
}
