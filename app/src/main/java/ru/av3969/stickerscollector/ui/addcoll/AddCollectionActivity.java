package ru.av3969.stickerscollector.ui.addcoll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class AddCollectionActivity extends BaseActivity implements AddCollectionActivityCallback {

    public static int REQUEST_ADD_COLL = 11;
    private static String START_CAT_ID = "startCatIt";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Long startCatIt = CatalogCategory.defaultId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            startCatIt = savedInstanceState.getLong(START_CAT_ID);
        }

        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.select_category);
        }

        showCategoryList(startCatIt); //Сначала покажем корневые категории
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(START_CAT_ID, startCatIt);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showCategoryList(Long filter) {
        String fragmentTag = filter.equals(CatalogCategory.defaultId) ? CategoryListFragment.FRAGMENT_TAG1
                : CategoryListFragment.FRAGMENT_TAG2;

        FragmentManager fm = getSupportFragmentManager();
        Fragment categoryListFragment = fm.findFragmentByTag(fragmentTag);
        if (categoryListFragment == null) {
            categoryListFragment = new CategoryListFragment();
        }

        Bundle args = new Bundle();
        args.putLong(CategoryListFragment.ARGUMENT_FILTER, filter);
        categoryListFragment.setArguments(args);

        FragmentTransaction transaction = fm.beginTransaction()
                .replace(R.id.container, categoryListFragment, fragmentTag);
        if(!filter.equals(CatalogCategory.defaultId))
            transaction.addToBackStack(null);
        transaction.commit();

        startCatIt = filter;
    }

}
