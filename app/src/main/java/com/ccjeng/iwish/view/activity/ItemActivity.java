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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.model.Item;
import com.ccjeng.iwish.presenter.IItemPresenter;
import com.ccjeng.iwish.presenter.impl.ItemPresenter;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.view.adapter.ItemAdapter;
import com.ccjeng.iwish.view.adapter.helper.OnStartDragListener;
import com.ccjeng.iwish.view.adapter.helper.SimpleItemTouchHelperCallback;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemActivity extends BaseActivity implements OnStartDragListener {

    private static final String TAG = "ItemActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;

    private IItemPresenter presenter;
    private ItemAdapter adapter;
    private List<Item> items;
    private String categoryId;
    private String toolbarTitle;

    private Speaker speaker;
    private static Mode mode;
    private MenuItem editMenuItem;
    private MenuItem sortMenuItem;
    private ItemTouchHelper swipeToDismissTouchHelper;
    private ItemTouchHelper dragTouchHelper;
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

    public void showData(List<Item> items) {
        this.items = items;
        adapter = new ItemAdapter(items, fontSize);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, String name) {

                if (mode.equals(Mode.EDIT)) {
                    showEditDialog(id, name);

                } else if (mode.equals(Mode.SORT)) {
                    //do nothing

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
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        dragTouchHelper = new ItemTouchHelper(callback);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (mode == Mode.SORT)
            dragTouchHelper.startDrag(viewHolder);
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
                sortMenuItem.setVisible(true);

                swipeToDismissTouchHelper.attachToRecyclerView(null);
                dragTouchHelper.attachToRecyclerView(null);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setTitle(toolbarTitle);

                break;

            case EDIT:
                fab.setVisibility(View.VISIBLE);
                editMenuItem.setVisible(false);
                sortMenuItem.setVisible(false);

                //can be deleted
                swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
                dragTouchHelper.attachToRecyclerView(null);
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                toolbar.setTitle(getString(R.string.edit_mode));

                break;

            case SORT:
                fab.setVisibility(View.GONE);
                editMenuItem.setVisible(false);
                sortMenuItem.setVisible(false);

                swipeToDismissTouchHelper.attachToRecyclerView(null);
                dragTouchHelper.attachToRecyclerView(recyclerView);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                toolbar.setTitle(getString(R.string.action_sort));

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
                item.setOrder(items.size());

                items.add(item);
                presenter.addItem(item);
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
        sortMenuItem = menu.findItem(R.id.action_sort);
        startMode(Mode.NORMAL);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                switch (mode) {
                    case NORMAL:
                        finish();
                        break;
                    case EDIT:
                        startMode(Mode.NORMAL);
                        break;
                    case SORT:
                        presenter.saveOrder(items);
                        startMode(Mode.NORMAL);
                        break;
                }

                break;
            case R.id.action_edit:
                startMode(Mode.EDIT);
                break;
            case R.id.action_sort:
                startMode(Mode.SORT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
