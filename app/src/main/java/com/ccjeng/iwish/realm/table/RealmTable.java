package com.ccjeng.iwish.realm.table;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface RealmTable {

    String ID = "id";

    interface Category {
        String ITEMS = "items";
        String NAME = "name";
    }

    interface Item {
        String NAME = "name";
    }

    interface SubItem {
        String NAME = "name";
    }

}
