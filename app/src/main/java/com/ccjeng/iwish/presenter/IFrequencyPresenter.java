package com.ccjeng.iwish.presenter;

/**
 * Created by andycheng on 2016/4/8.
 */
public interface IFrequencyPresenter extends IBasePresenter {

    void deleteFrequencyByName(String name);
    void getAllFrequencyDistinctCount();
    void getAllFrequency();

}
