package com.ccjeng.iwish.realm.repository.impl;

import android.util.Log;

import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.realm.repository.IItemRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class ItemRepository implements IItemRepository {

    private static final String TAG = ItemRepository.class.getSimpleName();

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
    public void updateItemById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Item i = realm.where(Item.class).equalTo(RealmTable.ID, id).findFirst();
        i.setName(name);
        realm.copyToRealmOrUpdate(i);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getItemById(String id, onGetItemByIdCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        Item item = realm.where(Item.class).equalTo(RealmTable.ID, id).findFirst();

        if (callback != null)
            callback.onSuccess(item);
    }

    @Override
    public void getAllItems(onGetAllItemsCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Item> results = realm.where(Item.class).findAll();
        results.sort(RealmTable.Item.ORDER);

        List<Item> list = realm.copyFromRealm(results);

        if (callback != null)
            callback.onSuccess(list);
    }

    @Override
    public void saveOrder(List<Item> itemList, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);

        for(int i =0 ; i < itemList.size() ; i++) {

            String id = itemList.get(i).getId();
            Log.d(TAG, i + " - " + itemList.get(i).getName());

            realm.beginTransaction();
            Item item = realm.where(Item.class).equalTo(RealmTable.ID, id).findFirst();
            item.setOrder(i);
            realm.copyToRealmOrUpdate(item);
            realm.commitTransaction();
        }

        if (callback != null)
            callback.onSuccess();
    }
}
