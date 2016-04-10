package com.ccjeng.iwish.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by andycheng on 2016/4/8.
 */
public class Daily extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String name;

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

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime() {
        this.datetime = new Date().getTime();
    }
}
