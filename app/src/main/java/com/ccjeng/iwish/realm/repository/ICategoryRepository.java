package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Category;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface ICategoryRepository extends IBaseRepository {


    interface onGetAllCategoryCallback {
        void onSuccess(RealmResults<Category> categories);
        void onError(String message);
    }

    interface onGetCategoryByIdCallback {
        void onSuccess(Category category);
        void onError(String message);
    }


    void addCategory(Category category, onSaveCallback callback);

    void deleteCategoryById(String Id, onDeleteCallback callback);

    void deleteCategoryByPosition(int position, onDeleteCallback callback);

    void getAllCategories(onGetAllCategoryCallback callback);

    void getCategoryById(String id, onGetCategoryByIdCallback callback);
}
