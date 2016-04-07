package com.ccjeng.iwish.view.adapter;

/**
 * Created by andycheng on 2016/4/6.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
