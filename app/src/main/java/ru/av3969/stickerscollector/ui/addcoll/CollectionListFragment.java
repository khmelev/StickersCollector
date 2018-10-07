package ru.av3969.stickerscollector.ui.addcoll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.ui.base.BaseFragment;

public class CollectionListFragment extends BaseFragment implements CollectionListContract.View {

    public static String FRAGMENT_TAG = "CollectionList";
    public static String ARGUMENT_PARENT_CAT = "ParentCategory";

    private CollectionListAdapter adapter;

    private Long parentCatId;
    private AddCollectionActivityCallback activityCallback;

    @Inject
    CollectionListContract.Presenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textError)
    TextView textError;

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

        setupRecyclerView();

        presenter.setView(this);
        presenter.loadCollectionList(parentCatId);

    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupRecyclerView() {
        adapter = new CollectionListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setOnItemClickListener(collectionId -> {
            if (activityCallback != null) {
                activityCallback.startEditCollectionActivity(collectionId);
            }
        });
    }

    @Override
    public void showLoading() {
        //activityCallback.showLoading();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        //activityCallback.hideLoading();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(@StringRes int resId) {
        textError.setText(resId);
        textError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String errorMsg) {
        textError.setText(errorMsg);
        textError.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateCollectionList(List<CatalogCollection> collectionList) {
        adapter.replaceDataSet(collectionList);
    }
}
