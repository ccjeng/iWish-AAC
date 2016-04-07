package com.ccjeng.iwish.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.view.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.custom_text)
    EditText customTextView;

    private Speaker speaker;
    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String textField = "TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //get data
        settings = getSharedPreferences(data,0);
        customTextView.setText(settings.getString(textField, ""));

    }


    @OnClick(R.id.btn_speak)
    public void speak(View view) {

        saveData();

        speaker = new Speaker(this);
        speaker.allow(true);
        speaker.speak(customTextView.getText().toString());

    }

    void saveData(){
        settings = getSharedPreferences(data,0);
        settings.edit()
                .putString(textField, customTextView.getText().toString())
                .apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speaker != null)
            speaker.destroy();
    }

}
