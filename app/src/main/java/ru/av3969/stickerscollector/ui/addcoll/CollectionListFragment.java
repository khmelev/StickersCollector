package ru.av3969.stickerscollector.ui.addcoll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.ui.base.BaseFragment;

public class CollectionListFragment extends BaseFragment implements CollectionListContract.View {

    public static String FRAGMENT_TAG = "CollectionList";
    public static String ARGUMENT_PARENT_CAT = "ParentCategory";

    private Long parentCatId;
    private AddCollectionActivityCallback activityCallback;

    @Inject
    CollectionListContract.Presenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AddCollectionActivityCallback)
            activityCallback = (AddCollectionActivityCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentComponent().inject(this);

        parentCatId = getArguments().getLong(ARGUMENT_PARENT_CAT, CatalogCategory.defaultId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        activityCallback.setActionBarTitle(R.string.select_collection);

        presenter.setView(this);
        presenter.loadCollectionList(parentCatId);

    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        activityCallback.showLoading();
    }

    @Override
    public void hideLoading() {
        activityCallback.hideLoading();
    }

    @Override
    public void updateCollectionList() {

    }
}
