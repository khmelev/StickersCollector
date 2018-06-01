package ru.av3969.stickerscollector.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCollectionsList();
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
