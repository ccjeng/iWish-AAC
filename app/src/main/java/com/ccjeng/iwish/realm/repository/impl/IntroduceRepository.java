package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Introduce;
import com.ccjeng.iwish.realm.repository.IIntroduceRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/4/22.
 */
public class IntroduceRepository implements IIntroduceRepository {

    @Override
    public void addIntroduce(Introduce introduce, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Introduce i = realm.createObject(Introduce.class);
        i.setId(Utils.getUniqueID());
        i.setName(introduce.getName());
        i.setOrder(introduce.getOrder());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteIntroduceById(String Id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Introduce i = realm.where(Introduce.class).equalTo(RealmTable.ID, Id).findFirst();
        i.deleteFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateIntroduceById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Introduce i = realm.where(Introduce.class).equalTo(RealmTable.ID, id).findFirst();
        i.setName(name);
        realm.copyToRealmOrUpdate(i);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllIntroduce(onGetIntroduceCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Introduce> results = realm.where(Introduce.class).findAll();
        results = results.sort(RealmTable.Introduce.ORDER);

        List<Introduce> list = realm.copyFromRealm(results);

        if (callback != null)
            callback.onSuccess(list);
    }

    @Override
    public void saveOrder(List<Introduce> list, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);

        for(int i =0 ; i < list.size() ; i++) {

            String id = list.get(i).getId();

            realm.beginTransaction();
            Introduce intro = realm.where(Introduce.class).equalTo(RealmTable.ID, id).findFirst();
            intro.setOrder(i);
            realm.copyToRealmOrUpdate(intro);
            realm.commitTransaction();
        }

        if (callback != null)
            callback.onSuccess();
    }
}
