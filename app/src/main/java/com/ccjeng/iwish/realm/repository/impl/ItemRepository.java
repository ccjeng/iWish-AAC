package com.ccjeng.iwish.realm.repository.impl;

import android.util.Log;

import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.realm.repository.IBaseRepository;
import com.ccjeng.iwish.realm.repository.IItemRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class ItemRepository implements IItemRepository {

    @Override
    public void addItem(Item item, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Item i = realm.createObject(Item.class);
        i.setId(Utils.getUniqueID());
        i.setName(item.getName());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void addItemByCategoryId(Item item, String categoryId, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();

        Item i = realm.createObject(Item.class);

        i.setId(Utils.getUniqueID());
        i.setName(item.getName());

        Category c = realm.where(Category.class).equalTo(RealmTable.ID, categoryId).findFirst();
        c.getItems().add(i);

        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteItemById(String id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Item result = realm.where(Item.class).equalTo(RealmTable.ID, id).findFirst();
        result.removeFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteItemByPosition(int position, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmQuery<Item> query = realm.where(Item.class);
        RealmResults<Item> results = query.findAll();
        results.remove(position);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllItems(onGetAllItemsCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Item> results = realm.where(Item.class).findAll();

        if (callback != null)
            callback.onSuccess(results);
    }

    @Override
    public void getItemsByCategoryId(String id, onGetItemsCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        Category c = realm.where(Category.class).equalTo(RealmTable.ID, id).findFirst();
        RealmList<Item> items = c.getItems();

        if (callback != null)
            callback.onSuccess(items);
    }

    @Override
    public void getItemById(String id, onGetItemByIdCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        Item item = realm.where(Item.class).equalTo(RealmTable.ID, id).findFirst();

        if (callback != null)
            callback.onSuccess(item);
    }
}
