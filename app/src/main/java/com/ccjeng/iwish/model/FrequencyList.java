package com.ccjeng.iwish.model;

/**
 * Created by andycheng on 2016/4/10.
 */
public class FrequencyList implements Comparable<FrequencyList>{
    private String name;
    private long count;

    public FrequencyList(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(FrequencyList compareObject) {
        long compareCount = ((FrequencyList)compareObject).getCount();

        //ascneding order
        return this.count > compareCount ?-1:
                this.count < compareCount ?1:0;
    }
}
