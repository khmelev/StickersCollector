package ru.av3969.stickerscollector.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.addcoll.AddCollectionActivity;
import ru.av3969.stickerscollector.ui.base.BaseActivity;
import ru.av3969.stickerscollector.ui.editcoll.EditCollectionActivity;

public class MainActivity extends BaseActivity implements MainActivityCallback {

    @BindView(R.id.fab_add_collection)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Set up the toolbar.
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCollectionActivity.class);
            startActivity(intent);
        });

        showCollectionsList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCollectionsList() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment collectionsListFragment = fm.findFragmentByTag(MyCollectionsListFragment.FRAGMENT_TAG);
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
}
