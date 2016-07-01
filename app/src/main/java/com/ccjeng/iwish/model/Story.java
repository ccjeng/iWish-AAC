package com.ccjeng.iwish.model;

import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by andycheng on 2016/7/1.
 */
public class Story extends RealmObject {
    @PrimaryKey
    @Required
    private String id;
    @Required
    private String name;

    private int order;

    private long datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
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

    public long getDatetime() {
        return datetime;
    }

    public String getDate() {
        return String.format(Locale.getDefault(), "%tF", datetime);
    }

    public void setDatetime() {
        this.datetime = new Date().getTime();
    }
}
