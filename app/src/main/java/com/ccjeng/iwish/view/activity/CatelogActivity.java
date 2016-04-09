package com.ccjeng.iwish.view.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
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
import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.presenter.ICategoryPresenter;
import com.ccjeng.iwish.presenter.impl.CategoryPresenter;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.view.adapter.CategoryAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class CatelogActivity  extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    private ICategoryPresenter presenter;
    private RealmResults<Category> categories;
    private CategoryAdapter adapter;

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

        presenter = new CategoryPresenter(this);

        speaker = new Speaker(this);
        speaker.allow(true);
        initComponents();

        presenter.subscribeCallbacks();
        presenter.getAllCategories();


    }

    private void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void initRecyclerListener(){

        if (columnNum > 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, columnNum));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

      /*  ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        */
        swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.deleteCategoryById(categories.get(viewHolder.getAdapterPosition()).getId());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

        });

    }

    public void showData(RealmResults<Category> categories) {
        this.categories = categories;
        adapter = new CategoryAdapter(categories, fontSize);

        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, String name) {

                if (mode.equals(Mode.EDIT)) {
                    showEditDialog(id, name);

                } else {
                    if (speaker.isAllowed()) {
                        speaker.speak(name);
                    }

                    Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                    intent.putExtra(RealmTable.ID, id);
                    intent.putExtra(RealmTable.Category.NAME, name);

                    startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void showAddDialog() {
        final AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new AddDialog.OnAddClickListener() {
            @Override
            public void OnAddClickListener(String name) {
                dialog.dismiss();
                presenter.addCategory(name);
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
                presenter.updateCategoryById(id, name);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void startMode(Mode modeToStart){

        switch (modeToStart) {
            case NORMAL:
                fab.setVisibility(View.GONE);
                editMenuItem.setVisible(true);

                swipeToDismissTouchHelper.attachToRecyclerView(null);

                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setTitle(getString(R.string.aac));

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
