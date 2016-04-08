package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.realm.repository.IBaseRepository;

/**
 * Created by andycheng on 2016/4/8.
 */
public interface IFrequencyPresenter extends IBaseRepository {

    void addFrequency(String name);
    void deleteFrequencyByPosition(int position);
    void deleteFrequencyById(String id);
    void updateFrequencyById(String id, String name);
    void getAllFrequency();

}
