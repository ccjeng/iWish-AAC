package com.ccjeng.iwish.realm;

import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.model.Item;

import io.realm.annotations.RealmModule;

/**
 * Created by andycheng on 2016/3/26.
 */

@RealmModule(classes = {Item.class, Category.class})
public class AppRealmModule {
}
