package com.ccjeng.iwish.presenter;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface ICategoryPresenter extends IBasePresenter {

    void addCategory(String name);
    void deleteCategoryByPosition(int position);
    void deleteCategoryById(String id);
    void getCategoryById(String id);
    void getAllCategories();

}
