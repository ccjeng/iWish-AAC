package com.ccjeng.iwish.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by andycheng on 2016/3/25.
 */
public class Item extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;

    private int order;

    private RealmList<SubItem> subItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public RealmList<SubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(RealmList<SubItem> subItems) {
        this.subItems = subItems;
    }
}
