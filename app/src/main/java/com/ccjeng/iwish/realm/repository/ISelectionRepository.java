package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Selection;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public interface ISelectionRepository extends IBaseRepository {

    interface onGetSelectionCallback {
        void onSuccess(List<Selection> selections);
        void onError(String message);
    }

    void addSelection(Selection selection, onSaveCallback callback);

    void deleteSelectionById(String id, onDeleteCallback callback);

    void updateSelectionById(String id, String name, onUpdateCallback callback);

    void getAllSelection(onGetSelectionCallback callback);

    void saveOrder(List<Selection> list, onSaveCallback callback);


}
