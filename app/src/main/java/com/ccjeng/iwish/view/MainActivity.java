package com.ccjeng.iwish.view;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.ccjeng.iwish.R;
import com.ccjeng.iwish.controller.Speaker;
import com.ccjeng.iwish.model.Category;
import com.ccjeng.iwish.presenter.ICategoryPresenter;
import com.ccjeng.iwish.presenter.impl.CategoryPresenter;
import com.ccjeng.iwish.realm.table.RealmTable;
import com.ccjeng.iwish.utils.RealmMigration;
import com.ccjeng.iwish.view.adapter.CategoryAdapter;
import com.ccjeng.iwish.view.base.BaseActivity;
import com.ccjeng.iwish.view.dialogs.AddDialog;
import com.ccjeng.iwish.view.dialogs.EditDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

import io.realm.RealmResults;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.coordinatorlayout) public CoordinatorLayout coordinatorlayout;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    private ICategoryPresenter presenter;
    private RealmResults<Category> categories;
    private CategoryAdapter adapter;

    private final int CHECK_CODE = 0x1;
    private Speaker speaker;
    private static Mode mode;
    private MenuItem editMenuItem;
    private ItemTouchHelper swipeToDismissTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new CategoryPresenter(this);

        initComponents();

        presenter.subscribeCallbacks();
        presenter.getAllCategories();

        checkTTS();

    }

    private void initComponents() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initRecyclerListener();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }

    private void initRecyclerListener(){
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
                presenter.deleteCategoryById(categories.get(viewHolder.getAdapterPosition()).getId());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });


    }

    public void showData(RealmResults<Category> categories) {
        this.categories = categories;
        adapter = new CategoryAdapter(categories);

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

                //toolbar.setLogo(null);
                toolbar.setTitle(getString(R.string.app_name));

                break;

            case EDIT:
                fab.setVisibility(View.VISIBLE);
                editMenuItem.setVisible(false);

                //can be deleted
                swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);

                //toolbar.setLogo(R.mipmap.icon_toolbal_arrow_white);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));

                //RealmMigration migration = new RealmMigration(this);
                //migration.backup();
                //migration.restore();

                break;
            case R.id.action_edit:
                startMode(Mode.EDIT);
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
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this);
                speaker.allow(true);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //item.getMenuInfo()
        switch (item.getItemId()) {
            case R.id.edit:
                Log.d(TAG, "edit");
                break;
            case R.id.delete:
                Log.d(TAG, "delete");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
