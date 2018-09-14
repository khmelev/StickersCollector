package ru.av3969.stickerscollector.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.addcoll.AddCollectionActivity;
import ru.av3969.stickerscollector.ui.base.BaseActivity;
import ru.av3969.stickerscollector.ui.editcoll.EditCollectionActivity;

public class MainActivity extends BaseActivity implements MainActivityCallback {

    MyCollectionsListFragment collectionsListFragment;

    @BindView(R.id.fab_add_collection)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    MenuItem miEdit;
    MenuItem miCommit;

    ActionBar actionBar;

    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Set up the toolbar.
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCollectionActivity.class);
            startActivity(intent);
        });

        showCollectionsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_collections, menu);

        miEdit = menu.findItem(R.id.miEdit);
        miCommit = menu.findItem(R.id.miCommit);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(editMode)
                    disableEditMode(false);
                else
                    mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.miEdit:
                enableEditMode();
                return true;
            case R.id.miCommit:
                disableEditMode(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCollectionsList() {
        FragmentManager fm = getSupportFragmentManager();
        collectionsListFragment = (MyCollectionsListFragment) fm.findFragmentByTag(MyCollectionsListFragment.FRAGMENT_TAG);
        if (collectionsListFragment == null) {
            collectionsListFragment = new MyCollectionsListFragment();
        }
        fm.beginTransaction()
                .replace(R.id.container, collectionsListFragment, MyCollectionsListFragment.FRAGMENT_TAG)
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void startEditCollectionActivity(Long parentCollectionId, Long collectionId) {
        Intent intent = new Intent(this, EditCollectionActivity.class);
        intent.putExtra(EditCollectionActivity.PARENT_COLLECTION, parentCollectionId);
        intent.putExtra(EditCollectionActivity.COLLECTION_ID, collectionId);
        startActivity(intent);
    }

    private void enableEditMode() {
        editMode = true;
        miEdit.setVisible(false);
        miCommit.setVisible(true);
        fab.setVisibility(View.GONE);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        collectionsListFragment.setEditMode(true);
    }

    private void disableEditMode(boolean commitChanges) {
        editMode = false;
        miEdit.setVisible(true);
        miCommit.setVisible(false);
        fab.setVisibility(View.VISIBLE);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        collectionsListFragment.setEditMode(false);
        if(commitChanges) {
            collectionsListFragment.commitChanges();
        } else {
            collectionsListFragment.loadCollectionsList();
        }
    }
}
