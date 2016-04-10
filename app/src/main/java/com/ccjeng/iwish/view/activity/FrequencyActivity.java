package com.ccjeng.iwish.view.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.model.Frequency;
import com.ccjeng.iwish.model.FrequencyList;
import com.ccjeng.iwish.presenter.IFrequencyPresenter;
import com.ccjeng.iwish.presenter.impl.FrequencyPresenter;
import com.ccjeng.iwish.view.adapter.FrequencyAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class FrequencyActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    private IFrequencyPresenter presenter;
    private FrequencyAdapter adapter;
    private List<FrequencyList> frequency;

    private Speaker speaker;
    private static Mode mode;
    private MenuItem editMenuItem;
    private ItemTouchHelper swipeToDismissTouchHelper;
    private int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);

        presenter = new FrequencyPresenter(this);
        speaker = new Speaker(this);
        speaker.allow(true);

        initComponents();

        presenter.subscribeCallbacks();
        presenter.getAllFrequencyDistinctCount();
    }

    private void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fontSize = getFontSize();

        initRecyclerListener();
    }

    private void initRecyclerListener(){

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.deleteFrequencyByName(frequency.get(viewHolder.getAdapterPosition()).getName());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

        });

    }

    public void showData(List<FrequencyList> frequency) {

        this.frequency = frequency;
        adapter = new FrequencyAdapter(frequency, fontSize);

        adapter.setOnItemClickListener(new FrequencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {

                    if (speaker.isAllowed()) {
                        speaker.speak(name);
                    }

            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void startMode(Mode modeToStart){

        switch (modeToStart) {
            case NORMAL:
                editMenuItem.setVisible(true);

                swipeToDismissTouchHelper.attachToRecyclerView(null);

                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setTitle(getString(R.string.frequency));

                break;

            case EDIT:
                editMenuItem.setVisible(false);

                //can be deleted
                swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                toolbar.setTitle(getString(R.string.edit_mode));
                break;
        }

        mode = modeToStart;

    }


    @Override
    public void onBackPressed() {
        if (mode != Mode.NORMAL) {
            startMode(Mode.NORMAL);
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unSubscribeCallbacks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speaker != null)
            speaker.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        editMenuItem = menu.findItem(R.id.action_edit);
        startMode(Mode.NORMAL);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mode != Mode.NORMAL) {
                    startMode(Mode.NORMAL);
                } else {
                    finish();
                }
                break;
            case R.id.action_edit:
                startMode(Mode.EDIT);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
