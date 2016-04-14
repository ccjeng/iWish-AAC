package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.Category;

import java.util.List;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface ICategoryPresenter extends IBasePresenter {

    void addCategory(String name);
    void deleteCategoryByPosition(int position);
    void deleteCategoryById(String id);
    void updateCategoryById(String id, String name);
    void getCategoryById(String id);
    void getAllCategories();
    void saveOrder(List<Category> categoryList);
}
