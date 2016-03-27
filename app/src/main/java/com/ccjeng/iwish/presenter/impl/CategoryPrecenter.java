package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.presenter.ICategoryPresenter;
import com.ccjeng.iwish.realm.repository.ICategoryRepository;
import com.ccjeng.iwish.realm.repository.impl.CategoryRepository;
import com.ccjeng.iwish.view.MainActivity;

import io.realm.RealmResults;

/**
 * Created by andycheng on 2016/3/26.
 */
public class CategoryPrecenter implements ICategoryPresenter {

    private MainActivity view;
    private ICategoryRepository repository;

    private ICategoryRepository.onGetAllCategoryCallback getAllCategoryCallback;
    private ICategoryRepository.onSaveCallback onSaveCallback;
    private ICategoryRepository.onGetCategoryByIdCallback onGetCategoryByIdCallback;
    private ICategoryRepository.onDeleteCallback onDeleteCallback;

    public CategoryPrecenter(MainActivity view) {
        this.view = view;
        repository = new CategoryRepository();
    }

    @Override
    public void addCategory(String name) {
        Category category = new Category(name);
        repository.addCategory(category, onSaveCallback);
    }

    @Override
    public void deleteCategoryByPosition(int position) {
        repository.deleteCategoryByPosition(position, onDeleteCallback);
    }

    @Override
    public void deleteCategoryById(String id) {
        repository.deleteCategoryById(id, onDeleteCallback);
    }

    @Override
    public void getCategoryById(String id) {
        repository.getCategoryById(id, onGetCategoryByIdCallback);

    }

    @Override
    public void getAllCategories() {
        repository.getAllCategories(getAllCategoryCallback);
    }

    @Override
    public void subscribeCallbacks() {

        getAllCategoryCallback = new ICategoryRepository.onGetAllCategoryCallback() {

            @Override
            public void onSuccess(RealmResults<Category> categories) {
                view.showData(categories);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, "Get Data Error");
            }
        };

        onSaveCallback = new ICategoryRepository.onSaveCallback(){
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, "Saved");
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, "Save Error");
            }
        };

        onGetCategoryByIdCallback = new ICategoryRepository.onGetCategoryByIdCallback(){
            @Override
            public void onSuccess(Category category) {

            }

            @Override
            public void onError(String message) {
            }
        };

        onDeleteCallback = new ICategoryRepository.onDeleteCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, "Deleted");
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, "Delete Error");
            }
        };

    }

    @Override
    public void unSubscribeCallbacks() {
        getAllCategoryCallback = null;
        onSaveCallback = null;
        onGetCategoryByIdCallback = null;
        onDeleteCallback = null;
    }
}
