package com.ccjeng.iwish.realm.table;

/**
 * Created by andycheng on 2016/3/26.
 */
public interface RealmTable {

    String ID = "id";

    interface Category {
        String ITEMS = "items";
        String NAME = "name";
        String ORDER = "order";
    }

    interface Item {
        String NAME = "name";
        String ORDER = "order";
    }

    interface SubItem {
        String NAME = "name";
    }


    interface Daily {
        String NAME = "name";
        String DATETIME = "datetime";
    }

    interface Frequency {
        String NAME = "name";
        String COUNT = "count";
    }

    interface Introduce {
        String NAME = "name";
        String ORDER = "order";
    }

    interface Selection {
        String NAME = "name";
        String ORDER = "order";
    }
}
