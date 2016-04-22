package com.ccjeng.iwish.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.model.Selection;
import com.ccjeng.iwish.presenter.ISelectionPresenter;
import com.ccjeng.iwish.presenter.impl.SelectionPresenter;
import com.ccjeng.iwish.view.adapter.SelectionAdapter;
import com.ccjeng.iwish.view.adapter.helper.OnStartDragListener;
import com.ccjeng.iwish.view.adapter.helper.SimpleItemTouchHelperCallback;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectionActivity extends BaseActivity implements OnStartDragListener {

    private static final String TAG = SelectionActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private ISelectionPresenter presenter;
    private List<Selection> mSelections;
    private SelectionAdapter mAdapter;

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

        presenter = new SelectionPresenter(this);

        speaker = new Speaker(this);
        speaker.allow(true);

        initComponents();

        presenter.subscribeCallbacks();
        presenter.getAllSelection();
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

        /*
        if (columnNum > 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, columnNum));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }*/

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                presenter.deleteSelectionById(mSelections.get(viewHolder.getAdapterPosition()).getId());
                mSelections.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

        });

    }

    public void showData(List<Selection> selections) {
        this.mSelections = selections;
        mAdapter = new SelectionAdapter(mSelections, fontSize);

        mAdapter.setOnItemClickListener(new SelectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String id, String name) {

                if (mode.equals(Mode.EDIT)) {
                    showEditDialog(position, id, name);

                } else if (mode.equals(Mode.SORT)) {
                    //do nothing

                } else {
                    if (speaker.isAllowed()) {
                        speaker.speak(name);
                    }
                }
            }

        });

        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        dragTouchHelper = new ItemTouchHelper(callback);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (mode == Mode.SORT)
            dragTouchHelper.startDrag(viewHolder);
    }

    private void showAddDialog() {
        final AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new AddDialog.OnAddClickListener() {
            @Override
            public void OnAddClickListener(String name) {
                dialog.dismiss();

                Selection selection = new Selection();
                selection.setName(name);
                selection.setOrder(mSelections.size());

                presenter.addSelection(selection);
                presenter.getAllSelection();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showEditDialog(final int position, final String id, String name) {

        final EditDialog dialog = EditDialog.newInstance(name);
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new EditDialog.OnEditClickListener() {
            @Override
            public void OnEditClickListener(String name) {
                dialog.dismiss();
                presenter.updateSelectionById(id, name);
                mSelections.get(position).setName(name);
                mAdapter.notifyDataSetChanged();
            }
        });
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
                toolbar.setTitle(getString(R.string.selection));

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

        Log.d(TAG, "Mode = " + modeToStart);

    }

    @Override
    public void onBackPressed() {
        switch (mode) {
            case NORMAL:
                finish();
                break;
            case EDIT:
                startMode(Mode.NORMAL);
                break;
            case SORT:
                presenter.saveOrder(mSelections);
                startMode(Mode.NORMAL);
                break;
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

                switch (mode) {
                    case NORMAL:
                        finish();
                        break;
                    case EDIT:
                        startMode(Mode.NORMAL);
                        break;
                    case SORT:
                        presenter.saveOrder(mSelections);
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
