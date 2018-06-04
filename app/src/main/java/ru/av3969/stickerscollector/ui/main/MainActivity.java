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
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.addcoll.AddCollectionActivity;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCollectionActivity.class);
            startActivityForResult(intent, AddCollectionActivity.REQUEST_ADD_COLL);
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
        Fragment collectionsListFragment = fm.findFragmentByTag(CollectionsListFragment.FRAGMENT_TAG);
        if (collectionsListFragment == null) {
            collectionsListFragment = new CollectionsListFragment();
        }
        fm.beginTransaction()
                .replace(R.id.container, collectionsListFragment, CollectionsListFragment.FRAGMENT_TAG)
                .disallowAddToBackStack()
                .commit();
    }
}
