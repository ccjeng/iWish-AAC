package com.ccjeng.iwish.realm;

import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.model.Daily;
import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.model.Introduce;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.model.Selection;
import com.ccjeng.iwish.model.Story;
import com.ccjeng.iwish.model.SubItem;

import io.realm.annotations.RealmModule;

/**
 * Created by andycheng on 2016/3/26.
 */

@RealmModule(classes = {Introduce.class
        , Selection.class
        , Daily.class
        , Frequency.class
        , SubItem.class
        , Item.class
        , Category.class
        , Story.class})
public class AppRealmModule {
}
