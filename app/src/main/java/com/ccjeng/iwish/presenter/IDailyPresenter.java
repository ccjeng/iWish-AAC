package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.Daily;

/**
 * Created by andycheng on 2016/4/8.
 */
public interface IDailyPresenter extends IBasePresenter {

    void addDaily(Daily daily);
    void deleteDailyByPosition(int position);
    void deleteDailyById(String id);
    void updateDailyById(String id, String name);
    void getAllDaily();
}
