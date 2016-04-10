package com.ccjeng.iwish.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.presenter.IItemPresenter;
import com.ccjeng.iwish.presenter.impl.ItemPresenter;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.Utils;
import com.ccjeng.iwish.view.adapter.ItemAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ItemActivity extends BaseActivity {

    private static final String TAG = "ItemActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;

    private IItemPresenter presenter;
    private ItemAdapter adapter;
    private RealmResults<Item> items;
    private String categoryId;
    private String toolbarTitle;

    private Speaker speaker;
    private static Mode mode;
    private MenuItem editMenuItem;
    private ItemTouchHelper swipeToDismissTouchHelper;
    private int fontSize, columnNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);

        presenter = new ItemPresenter(this);
        categoryId = getIntent().getStringExtra(RealmTable.ID);
        toolbarTitle = getIntent().getStringExtra(RealmTable.Category.NAME);

        speaker = new Speaker(this);
        speaker.allow(true);
        initComponents();

        presenter.subscribeCallbacks();
        presenter.getAllItems();

    }

    protected void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(toolbarTitle);

        }

        fontSize = getFontSize();
        columnNum = getColumnNum();

        initRecyclerListener();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }


    public void showData(RealmResults<Item> items) {
        this.items = items;
        adapter = new ItemAdapter(items, fontSize);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, String name) {

                if (mode.equals(Mode.EDIT)) {
                    showEditDialog(id, name);

                } else {
                    if (speaker.isAllowed()) {
                        speaker.speak(toolbarTitle + name);
                    }

                    Intent intent = new Intent(getApplicationContext(), SubItemActivity.class);
                    intent.putExtra(RealmTable.ID, id);
                    intent.putExtra(RealmTable.Category.NAME, toolbarTitle + name);

                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    protected void initRecyclerListener() {

        if (columnNum > 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, columnNum));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
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
        final AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new AddDialog.OnAddClickListener() {
            @Override
            public void OnAddClickListener(String name) {
                dialog.dismiss();

                Item item = new Item();
                item.setName(name);

                presenter.addItemByCategoryId(item, categoryId);
                presenter.getAllItemsByCategoryId(categoryId);

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showEditDialog(final String id, String name) {

        final EditDialog dialog = EditDialog.newInstance(name);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new EditDialog.OnEditClickListener() {
            @Override
            public void OnEditClickListener(String name) {
                dialog.dismiss();
                presenter.updateItemById(id, name);
                adapter.notifyDataSetChanged();
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
