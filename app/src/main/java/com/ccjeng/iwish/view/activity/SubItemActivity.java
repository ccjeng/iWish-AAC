package com.ccjeng.iwish.view.activity;

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
import com.ccjeng.iwish.model.SubItem;
import com.ccjeng.iwish.presenter.ISubItemPresenter;
import com.ccjeng.iwish.presenter.impl.SubItemPresenter;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.view.adapter.SubItemAdapter;
import com.ccjeng.iwish.view.adapter.helper.OnStartDragListener;
import com.ccjeng.iwish.view.adapter.helper.SimpleItemTouchHelperCallback;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class SubItemActivity extends BaseActivity implements OnStartDragListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;

    private ISubItemPresenter presenter;
    private SubItemAdapter adapter;
    private RealmList<SubItem> subItems;
    private String itemId;
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

        presenter = new SubItemPresenter(this);
        itemId = getIntent().getStringExtra(RealmTable.ID);
        toolbarTitle = getIntent().getStringExtra(RealmTable.Item.NAME);

        speaker = new Speaker(this);
        speaker.allow(true);

        initComponents();
        presenter.subscribeCallbacks();
        presenter.getAllSubItemsByItemId(itemId);

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

    public void showData(RealmList<SubItem> subItems) {

        this.subItems = subItems;
        adapter = new SubItemAdapter(subItems, fontSize);

        adapter.setOnItemClickListener(new SubItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, String name) {
                if (mode.equals(Mode.EDIT)) {
                    showEditDialog(id, name);

                } else {
                    if (speaker.isAllowed()) {
                        speaker.speak(toolbarTitle + name);
                    }

                    //add frequency log
                    presenter.addFrequency(toolbarTitle + name);
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
                presenter.deleteSubItemById(subItems.get(viewHolder.getAdapterPosition()).getId(), itemId);
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

                SubItem subItem = new SubItem();
                subItem.setName(name);

                presenter.addSubItemByItemId(subItem, itemId);

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
                presenter.updateSubItemById(id, name);
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
        sortMenuItem = menu.findItem(R.id.action_sort);
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
            case R.id.action_sort:
                startMode(Mode.SORT);
                 break;
        }
        return super.onOptionsItemSelected(item);
    }
}
