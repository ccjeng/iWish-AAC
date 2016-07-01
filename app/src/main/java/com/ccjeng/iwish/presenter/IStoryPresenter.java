package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.Story;

import java.util.List;

/**
 * Created by andycheng on 2016/7/1.
 */
public interface IStoryPresenter extends IBasePresenter {

    void addStory(Story story);
    void deleteStoryById(String id);
    void updateStoryById(String id, String name);
    void getAllStory();
    void saveOrder(List<Story> list);
}
