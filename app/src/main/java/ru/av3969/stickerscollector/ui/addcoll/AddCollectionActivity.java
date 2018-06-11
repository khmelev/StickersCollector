package ru.av3969.stickerscollector.ui.addcoll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class AddCollectionActivity extends BaseActivity implements AddCollectionActivityCallback {

    public static int REQUEST_ADD_COLL = 11;
    private static String START_CAT_ID = "startCatId";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ActionBar actionBar;
    MenuItem miActionProgressItem;

    private Long startCatId = CatalogCategory.defaultId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            startCatId = savedInstanceState.getLong(START_CAT_ID, CatalogCategory.defaultId);
        }

        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_progress_bar, menu);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        showCategoryList(startCatId); //запуск выбора категории

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(START_CAT_ID, startCatId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setActionBarTitle(int resId) {
        if (actionBar != null) {
            actionBar.setTitle(resId);
        }
    }

    @Override
    public void showLoading() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    @Override
    public void hideLoading() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }

    @Override
    public void showCategoryList(Long filterCat) {
        String fragmentTag = filterCat.equals(CatalogCategory.defaultId) ? CategoryListFragment.FRAGMENT_TAG1
                : CategoryListFragment.FRAGMENT_TAG2;

        FragmentManager fm = getSupportFragmentManager();
        Fragment categoryListFragment = fm.findFragmentByTag(fragmentTag);
        if (categoryListFragment == null) {
            categoryListFragment = new CategoryListFragment();
        }

        Bundle args = new Bundle();
        args.putLong(CategoryListFragment.ARGUMENT_FILTER, filterCat);
        categoryListFragment.setArguments(args);

        FragmentTransaction transaction = fm.beginTransaction()
                .replace(R.id.container, categoryListFragment, fragmentTag);
        if(!filterCat.equals(CatalogCategory.defaultId))
            transaction.addToBackStack(null);
        transaction.commit();

        startCatId = filterCat;
    }

    @Override
    public void showCollectionList(Long parentCat) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment collectionListFragment = fm.findFragmentByTag(CollectionListFragment.FRAGMENT_TAG);
        if (collectionListFragment == null) {
            collectionListFragment = new CollectionListFragment();
        }

        Bundle args = new Bundle();
        args.putLong(CollectionListFragment.ARGUMENT_PARENT_CAT, parentCat);
        collectionListFragment.setArguments(args);

        fm.beginTransaction()
                .replace(R.id.container, collectionListFragment, CollectionListFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showMyCollectionAdd(Long parentColl) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment addCollectionFragment = fm.findFragmentByTag(AddMyCollectionFragment.FRAGMENT_TAG);
        if (addCollectionFragment == null) {
            addCollectionFragment = new AddMyCollectionFragment();
        }

        Bundle args = new Bundle();
        args.putLong(AddMyCollectionFragment.ARGUMENT_PARENT_COLL, parentColl);
        addCollectionFragment.setArguments(args);

        fm.beginTransaction()
                .replace(R.id.container, addCollectionFragment, AddMyCollectionFragment.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }
}
