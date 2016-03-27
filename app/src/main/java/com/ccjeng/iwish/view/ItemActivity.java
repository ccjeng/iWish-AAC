package com.ccjeng.iwish.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.presenter.IItemPresenter;
import com.ccjeng.iwish.presenter.impl.ItemPrecenter;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.view.adapter.ItemAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddItemDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class ItemActivity extends BaseActivity {

    private static final String TAG = "ItemActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;

    private IItemPresenter presenter;
    private ItemAdapter adapter;
    private RealmList<Item> items;
    private String categoryId;
    private String toolbarTitle;

    private final int CHECK_CODE = 0x1;
    private Speaker speaker;
    private static Mode mode;
    private MenuItem editMenuItem;
    private ItemTouchHelper swipeToDismissTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);

        presenter = new ItemPrecenter(this);
        categoryId = getIntent().getStringExtra(RealmTable.ID);
        toolbarTitle = getIntent().getStringExtra(RealmTable.Category.NAME);

        speaker = new Speaker(this);
        initComponents();

        presenter.subscribeCallbacks();
        presenter.getAllItemsByCategoryId(categoryId);

    }

    protected void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(toolbarTitle);

        }
        initRecyclerListener();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }


    public void showData(RealmList<Item> items) {
        this.items = items;
        adapter = new ItemAdapter(items);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {

                if (speaker != null) {
                    speaker.allow(true);
                    speaker.speak(name);
                }

            }
        });
        recyclerView.setAdapter(adapter);
    }

    protected void initRecyclerListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
            @Override

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.deleteItemById(items.get(viewHolder.getAdapterPosition()).getId());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        //swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void startMode(Mode modeToStart){

        switch (modeToStart) {
            case NORMAL:
                fab.setVisibility(View.GONE);
                editMenuItem.setVisible(true);

                swipeToDismissTouchHelper.attachToRecyclerView(null);

                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setTitle(toolbarTitle);

                break;

            case EDIT:
                fab.setVisibility(View.VISIBLE);
                editMenuItem.setVisible(false);

                //can be deleted
                swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                toolbar.setTitle(getString(R.string.edit_mode));

                break;
        }

        mode = modeToStart;

    }

    private void showAddDialog() {
        final AddItemDialog dialog = new AddItemDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new AddItemDialog.OnAddClickListener() {
            @Override
            public void OnAddClickListener(Item item) {
                dialog.dismiss();
                presenter.addItemByCategoryId(item, categoryId);
                presenter.getAllItemsByCategoryId(categoryId);
            }
        });
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
                finish();
                break;
            case R.id.action_edit:
                startMode(Mode.EDIT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
