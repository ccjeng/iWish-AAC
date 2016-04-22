package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.Selection;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public interface ISelectionPresenter extends IBasePresenter {

    void addSelection(Selection selection);
    void deleteSelectionById(String id);
    void updateSelectionById(String id, String name);
    void getAllSelection();
    void saveOrder(List<Selection> list);

}
