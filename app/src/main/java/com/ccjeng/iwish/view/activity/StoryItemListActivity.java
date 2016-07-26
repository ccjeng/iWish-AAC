package com.ccjeng.iwish.view.activity;

import android.content.Intent;
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
import com.ccjeng.iwish.model.Story;
import com.ccjeng.iwish.presenter.IStoryPresenter;
import com.ccjeng.iwish.presenter.impl.StoryPresenter;
import com.ccjeng.iwish.view.adapter.StoryItemListAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An activity representing a list of StoryItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StoryItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StoryItemListActivity extends BaseActivity {

    private static final String TAG = StoryItemListActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.storyitem_list) RecyclerView recyclerView;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;

    private IStoryPresenter presenter;
    private List<Story> mStories;
    private StoryItemListAdapter mAdapter;

    private Speaker speaker;
    private static Mode mode;
    private MenuItem editMenuItem;
    private MenuItem sortMenuItem;
    private ItemTouchHelper swipeToDismissTouchHelper;
    //private ItemTouchHelper dragTouchHelper;

    private int fontSize;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyitem_list);
        ButterKnife.bind(this);

        initComponents();
        initRecyclerListener();

       // assert recyclerView != null;
       // setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.storyitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        speaker = new Speaker(this);
        speaker.allow(true);

        presenter = new StoryPresenter(this);
        presenter.subscribeCallbacks();
        presenter.getAllStory();
    }


    private void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fontSize = getFontSize();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    private void initRecyclerListener() {
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
                presenter.deleteStoryById(mStories.get(viewHolder.getAdapterPosition()).getId());
                mStories.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

        });
    }

    public void showData(List<Story> stories){

        this.mStories = stories;
        mAdapter = new StoryItemListAdapter(stories, fontSize);

        mAdapter.setOnItemClickListener(new StoryItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String id, String name) {

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(StoryItemDetailFragment.ARG_CONTENT, name);
                    arguments.putInt(StoryItemDetailFragment.ARG_FONTSIZE, fontSize);
                    StoryItemDetailFragment fragment = new StoryItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.storyitem_detail_container, fragment)
                            .commit();

                } else {
                    //Context context = v.getContext();
                    Intent intent = new Intent(StoryItemListActivity.this, StoryItemDetailActivity.class);
                    intent.putExtra(StoryItemDetailFragment.ARG_CONTENT, name);
                    intent.putExtra(StoryItemDetailFragment.ARG_FONTSIZE, fontSize);
                    startActivity(intent);
                }

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

        //show first article
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(StoryItemDetailFragment.ARG_CONTENT, stories.get(0).getName());
            arguments.putInt(StoryItemDetailFragment.ARG_FONTSIZE, fontSize);
            StoryItemDetailFragment fragment = new StoryItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.storyitem_detail_container, fragment)
                    .commit();
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
                }

                break;
            case R.id.action_edit:
                startMode(Mode.EDIT);
                break;

        }
        return super.onOptionsItemSelected(item);
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
        }
    }
    private void startMode(Mode modeToStart){

        switch (modeToStart) {
            case NORMAL:
                fab.setVisibility(View.GONE);
                editMenuItem.setVisible(true);
                sortMenuItem.setVisible(false);

                swipeToDismissTouchHelper.attachToRecyclerView(null);
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setTitle(getString(R.string.story));

                break;

            case EDIT:
                fab.setVisibility(View.VISIBLE);
                editMenuItem.setVisible(false);
                sortMenuItem.setVisible(false);

                //can be deleted
                swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                toolbar.setTitle(getString(R.string.edit_mode));

                break;

        }

        mode = modeToStart;

        Log.d(TAG, "Mode = " + modeToStart);

    }

    private void showAddDialog() {
        final AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setListener(new AddDialog.OnAddClickListener() {
            @Override
            public void OnAddClickListener(String name) {
                dialog.dismiss();

                Story story = new Story();
                story.setName(name);
                story.setOrder(mStories.size());

                presenter.addStory(story);
                presenter.getAllStory();
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
                presenter.updateStoryById(id, name);
                mStories.get(position).setName(name);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
