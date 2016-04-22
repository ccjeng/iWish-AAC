package com.ccjeng.iwish.presenter;

import com.ccjeng.iwish.model.Introduce;

import java.util.List;

/**
 * Created by andycheng on 2016/4/22.
 */
public interface IIntroducePresenter extends IBasePresenter {

    void addIntroduce(Introduce introduce);
    void deleteIntroduceById(String id);
    void updateIntroduceById(String id, String name);
    void getAllIntroduce();
    void saveOrder(List<Introduce> list);
}
