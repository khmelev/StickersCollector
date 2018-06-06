package ru.av3969.stickerscollector.ui.addcoll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class AddCollectionActivity extends BaseActivity {

    public static final int REQUEST_ADD_COLL = 1;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.select_category);

        showCategoryList();
    }

    private void showCategoryList() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment addCollectionFragment = fm.findFragmentByTag(CategoryListFragment.FRAGMENT_TAG);
        if (addCollectionFragment == null) {
            addCollectionFragment = new CategoryListFragment();
        }
        fm.beginTransaction()
                .replace(R.id.container, addCollectionFragment, CategoryListFragment.FRAGMENT_TAG)
                .disallowAddToBackStack()
                .commit();
    }
}
