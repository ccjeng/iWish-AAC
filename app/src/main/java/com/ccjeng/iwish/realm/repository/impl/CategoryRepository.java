package com.ccjeng.iwish.realm.repository.impl;

import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.realm.repository.IBaseRepository;
import com.ccjeng.iwish.realm.repository.ICategoryRepository;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class CategoryRepository implements ICategoryRepository {

    @Override
    public void addCategory(Category category, onSaveCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Category c = realm.createObject(Category.class);
        c.setId(Utils.getUniqueID());
        c.setName(category.getName());
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteCategoryById(String Id, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        Category c = realm.where(Category.class).equalTo(RealmTable.ID, Id).findFirst();
        c.removeFromRealm();
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void deleteCategoryByPosition(int position, onDeleteCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        realm.beginTransaction();
        RealmQuery<Category> query = realm.where(Category.class);
        RealmResults<Category> results = query.findAll();
        results.remove(position);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void updateCategoryById(String id, String name, onUpdateCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        Category c = realm.where(Category.class).equalTo(RealmTable.ID, id).findFirst();
        c.setName(name);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(c);
        realm.commitTransaction();

        if (callback != null)
            callback.onSuccess();
    }

    @Override
    public void getAllCategories(onGetAllCategoryCallback callback) {
        Realm realm = Realm.getInstance(BaseApplication.realmConfiguration);
        RealmQuery<Category> query = realm.where(Category.class);
        RealmResults<Category> results = query.findAll();

        if (callback != null)
            callback.onSuccess(results);
    }

    @Override
    public void getCategoryById(String id, onGetCategoryByIdCallback callback) {
        Realm realm =Realm.getInstance(BaseApplication.realmConfiguration);
        Category result = realm.where(Category.class).equalTo(RealmTable.ID, id).findFirst();

        if (callback != null)
            callback.onSuccess(result);
    }
}
