package com.ccjeng.iwish.presenter.impl;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.model.Story;
import com.ccjeng.iwish.presenter.IStoryPresenter;
import com.ccjeng.iwish.realm.repository.IStoryRepository;
import com.ccjeng.iwish.realm.repository.impl.StoryRepository;
import com.ccjeng.iwish.view.activity.StoryActivity;

import java.util.List;

/**
 * Created by andycheng on 2016/7/1.
 */
public class StoryPresenter implements IStoryPresenter {

    private StoryActivity view;
    private IStoryRepository repository;
    private IStoryRepository.onGetStoryCallback onGetStoryCallback;
    private IStoryRepository.onSaveCallback onSaveCallback;
    private IStoryRepository.onDeleteCallback onDeleteCallback;
    private IStoryRepository.onUpdateCallback onUpdateCallback;

    public StoryPresenter(StoryActivity view) {
        this.view = view;
        this.repository = new StoryRepository();
    }

    @Override
    public void addStory(Story story) {
        repository.addStory(story, onSaveCallback);
    }

    @Override
    public void deleteStoryById(String id) {
        repository.deleteStoryById(id, onDeleteCallback);
    }

    @Override
    public void updateStoryById(String id, String name) {
        repository.updateStoryById(id, name, onUpdateCallback);
    }

    @Override
    public void getAllStory() {
        repository.getAllStory(onGetStoryCallback);
    }

    @Override
    public void saveOrder(List<Story> list) {
        repository.saveOrder(list, onSaveCallback);
    }

    @Override
    public void subscribeCallbacks() {
        onGetStoryCallback = new IStoryRepository.onGetStoryCallback() {
            @Override
            public void onSuccess(List<Story> story) {
                view.showData(story);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onSaveCallback = new IStoryRepository.onSaveCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onUpdateCallback = new IStoryRepository.onUpdateCallback() {
            @Override
            public void onSuccess() {
                view.showMessage(view.coordinatorlayout, R.string.saved);
            }

            @Override
            public void onError(String message) {
                view.showMessage(view.coordinatorlayout, R.string.error);
            }
        };

        onDeleteCallback = new IStoryRepository.onDeleteCallback() {
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
        onGetStoryCallback = null;
        onSaveCallback = null;
        onDeleteCallback = null;
        onUpdateCallback = null;
    }
}
