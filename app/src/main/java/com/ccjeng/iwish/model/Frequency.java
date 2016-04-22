package com.ccjeng.iwish.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by andycheng on 2016/4/8.
 */
public class Frequency extends RealmObject {

    @PrimaryKey
    private String id;
    @Required @Index
    private String name;

    private int count;

    //private long datetime;

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

    public int getCount() {
        return count;
    }

    public void setCount() {
        this.count = 1;
    }

    /*
    public long getDatetime() {
        return datetime;
    }

    public void setDatetime() {
        this.datetime = new Date().getTime();
    }*/
}
