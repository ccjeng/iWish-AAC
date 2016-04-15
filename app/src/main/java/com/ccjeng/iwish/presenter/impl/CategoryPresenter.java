package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.presenter.ICategoryPresenter;
import com.ccjeng.iwish.realm.repository.ICategoryRepository;
import com.ccjeng.iwish.realm.repository.impl.CategoryRepository;
import com.ccjeng.iwish.view.activity.CategoryActivity;

import java.util.List;

/**
 * Created by andycheng on 2016/3/26.
 */
public class CategoryPresenter implements ICategoryPresenter {

    private CategoryActivity view;
    private ICategoryRepository repository;

    private ICategoryRepository.onGetAllCategoryCallback getAllCategoryCallback;
    private ICategoryRepository.onSaveCallback onSaveCallback;
    private ICategoryRepository.onUpdateCallback onUpdateCallback;
    private ICategoryRepository.onGetCategoryByIdCallback onGetCategoryByIdCallback;
    private ICategoryRepository.onDeleteCallback onDeleteCallback;


    public CategoryPresenter(CategoryActivity view) {
        this.view = view;
        repository = new CategoryRepository();
    }

    @Override
    public void addCategory(Category category) {
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
    public void updateCategoryById(String id, String name) {
        repository.updateCategoryById(id, name, onUpdateCallback);
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
    public void saveOrder(List<Category> categoryList) {
        repository.saveOrder(categoryList, onSaveCallback);
    }

    @Override
    public void subscribeCallbacks() {

        getAllCategoryCallback = new ICategoryRepository.onGetAllCategoryCallback() {

            @Override
            public void onSuccess(List<Category> categories) {
                view.showData(categories);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onSaveCallback = new ICategoryRepository.onSaveCallback(){
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new ICategoryRepository.onUpdateCallback(){
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
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
                view.showMessage(view.coordinatorlayout, R.string.deleted);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
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
