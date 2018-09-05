package com.ccjeng.iwish.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.view.adapter.MainAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private final int CHECK_CODE = 0x1;
    private Speaker speaker;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initComponents();

        checkTTS();

    }

    private void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<String> mainItems = new ArrayList<String>();

        mainItems.add(getString(R.string.aac));
        mainItems.add(getString(R.string.selection));
        mainItems.add(getString(R.string.daily));
        mainItems.add(getString(R.string.story));
        mainItems.add(getString(R.string.introduce));
        mainItems.add(getString(R.string.frequency));

        adapter = new MainAdapter(mainItems, getFontSize());

        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position, String name) {

                if (speaker != null) {
                    if (speaker.isAllowed()) {
                        speaker.speak(name);
                    }
                }

                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, SelectionActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, DailyActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, StoryItemListActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, IntroduceActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, FrequencyActivity.class));
                        break;
                }
            }

        });

        recyclerView.setAdapter(adapter);

        getPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speaker != null)
            speaker.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                speaker = new Speaker(this);
                speaker.allow(true);
            } else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }


    public void getPermission() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) // ask single or multiple permission once
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申請的權限全部允許
                            Log.d(TAG,"PERMISSION GRANT");
                        } else {
                            //只要有一個權限被拒絕，就會執行
                            Log.d(TAG,"PERMISSION DENY");
                            Toast.makeText(MainActivity.this, "未授權權限，地圖功能不能使用", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
    }

}
