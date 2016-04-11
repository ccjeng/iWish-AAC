package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.Item;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface IItemPresenter extends IBasePresenter {

    void addItem(Item item);
    void deleteItemByPosition(int position);
    void deleteItemById(String id);
    void updateItemById(String id, String name);

    void getItemById(String id);
    void getAllItems();
}
