package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Story;
import com.ccjeng.iwish.realm.repository.IStoryRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/7/1.
 */
public class StoryRepository implements IStoryRepository{

    @Override
    public void addStory(Story story, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Story s = realm.createObject(Story.class);
        s.setId(Utils.getUniqueID());
        s.setName(story.getName());
        s.setOrder(story.getOrder());
        s.setDatetime();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteStoryById(String id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Story s = realm.where(Story.class).equalTo(RealmTable.ID, id).findFirst();
        s.deleteFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateStoryById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Story s = realm.where(Story.class).equalTo(RealmTable.ID, id).findFirst();
        s.setName(name);
        s.setDatetime();
        realm.copyToRealmOrUpdate(s);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllStory(onGetStoryCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmResults<Story> results = realm.where(Story.class).findAll();
        results = results.sort(RealmTable.Story.ORDER);

        List<Story> list = realm.copyFromRealm(results);

        if (callback != null)
            callback.onSuccess(list);
    }

    @Override
    public void saveOrder(List<Story> list, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);

        for(int i =0 ; i < list.size() ; i++) {

            String id = list.get(i).getId();

            realm.beginTransaction();
            Story s = realm.where(Story.class).equalTo(RealmTable.ID, id).findFirst();
            s.setOrder(i);
            realm.copyToRealmOrUpdate(s);
            realm.commitTransaction();
        }

        if (callback != null)
            callback.onSuccess();
    }
}
