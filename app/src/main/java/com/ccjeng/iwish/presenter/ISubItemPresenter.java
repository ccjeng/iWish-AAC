package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.SubItem;

/**
 * Created by andycheng on 2016/3/27.
 */
public interface ISubItemPresenter extends IBasePresenter {

    void addSubItem(SubItem subItem);
    void addSubItemByItemId(SubItem subItem, String itemId);
    void deleteSubItemByPosition(int position);
    void deleteSubItemById(String subItemId, String itemId);
    void updateSubItemById(String id, String name);

    void getSubItemById(String id);
    void getAllSubItemsByItemId(String itemId);

    void addFrequency(String name);
}
