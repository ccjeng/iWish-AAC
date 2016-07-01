package com.ccjeng.iwish.realm.repository;

import com.ccjeng.iwish.model.Story;

import java.util.List;

/**
 * Created by andycheng on 2016/7/1.
 */
public interface IStoryRepository extends IBaseRepository {

    interface onGetStoryCallback {
        void onSuccess(List<Story> stories);
        void onError(String message);
    }

    void addStory(Story story, onSaveCallback callback);

    void deleteStoryById(String id, onDeleteCallback callback);

    void updateStoryById(String id, String name, onUpdateCallback callback);

    void getAllStory(onGetStoryCallback callback);

    void saveOrder(List<Story> list, onSaveCallback callback);

}
