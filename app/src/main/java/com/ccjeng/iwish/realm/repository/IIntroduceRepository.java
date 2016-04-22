package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Introduce;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public interface IIntroduceRepository extends IBaseRepository {

    interface onGetIntroduceCallback {
        void onSuccess(List<Introduce> introduces);
        void onError(String message);
    }

    void addIntroduce(Introduce introduce, onSaveCallback callback);

    void deleteIntroduceById(String Id, onDeleteCallback callback);

    void updateIntroduceById(String id, String name, onUpdateCallback callback);

    void getAllIntroduce(onGetIntroduceCallback callback);

    void saveOrder(List<Introduce> list, onSaveCallback callback);


}
