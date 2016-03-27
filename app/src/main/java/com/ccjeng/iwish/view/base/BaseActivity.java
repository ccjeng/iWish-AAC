package com.ccjeng.iwish.view.base;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by andycheng on 2016/3/26.
 */
public class BaseActivity extends AppCompatActivity {

    public static enum Mode {
        NORMAL, EDIT, SORT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMessage(CoordinatorLayout coordinatorLayout, int message) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();

    }
}
