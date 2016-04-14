package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Category;

import java.util.List;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface ICategoryRepository extends IBaseRepository {


    interface onGetAllCategoryCallback {
        void onSuccess(List<Category> categories);
        void onError(String message);
    }

    interface onGetCategoryByIdCallback {
        void onSuccess(Category category);
        void onError(String message);
    }


    void addCategory(Category category, onSaveCallback callback);

    void deleteCategoryById(String Id, onDeleteCallback callback);

    void deleteCategoryByPosition(int position, onDeleteCallback callback);

    void updateCategoryById(String id, String name, onUpdateCallback callback);

    void getAllCategories(onGetAllCategoryCallback callback);

    void getCategoryById(String id, onGetCategoryByIdCallback callback);

    void saveOrder(List<Category> categoryList, onSaveCallback callback);

}
