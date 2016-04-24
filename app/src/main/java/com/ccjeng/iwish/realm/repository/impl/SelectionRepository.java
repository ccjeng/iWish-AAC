package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Selection;
import com.ccjeng.iwish.realm.repository.ISelectionRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/22.
 */
public class SelectionRepository implements ISelectionRepository {

    @Override
    public void addSelection(Selection selection, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Selection s = realm.createObject(Selection.class);
        s.setId(Utils.getUniqueID());
        s.setName(selection.getName());
        s.setOrder(selection.getOrder());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteSelectionById(String Id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Selection s = realm.where(Selection.class).equalTo(RealmTable.ID, Id).findFirst();
        s.deleteFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateSelectionById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Selection s = realm.where(Selection.class).equalTo(RealmTable.ID, id).findFirst();
        s.setName(name);
        realm.copyToRealmOrUpdate(s);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllSelection(onGetSelectionCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Selection> results = realm.where(Selection.class).findAll();
        results = results.sort(RealmTable.Selection.ORDER);

        List<Selection> list = realm.copyFromRealm(results);

        if (callback != null)
            callback.onSuccess(list);
    }

    @Override
    public void saveOrder(List<Selection> list, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);

        for(int i =0 ; i < list.size() ; i++) {

            String id = list.get(i).getId();

            realm.beginTransaction();
            Selection s = realm.where(Selection.class).equalTo(RealmTable.ID, id).findFirst();
            s.setOrder(i);
            realm.copyToRealmOrUpdate(s);
            realm.commitTransaction();
        }

        if (callback != null)
            callback.onSuccess();
    }
}
