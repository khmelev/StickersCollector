package ru.av3969.stickerscollector.ui.addcoll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.ui.base.BaseFragment;

public class CategoryListFragment extends BaseFragment implements CategoryListContract.View {

    public static String FRAGMENT_TAG1 = "CategoryListRoot";
    public static String FRAGMENT_TAG2 = "CategoryListDetail";
    public static String ARGUMENT_FILTER = "Filter";

    private CategoryListAdapter adapter;

    private Long catId;

    private AddCollectionActivityCallback activityCallback;

    @Inject
    CategoryListContract.Presenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

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

        catId = getArguments().getLong(ARGUMENT_FILTER, CatalogCategory.defaultId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        activityCallback.setActionBarTitle(R.string.select_category);

        setupRecyclerView();

        presenter.setView(this);
        presenter.loadCategoryList(catId);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupRecyclerView() {
        adapter = new CategoryListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setOnItemClickListener((v, targetId, parentId) -> {
            if (activityCallback != null) {
                if(parentId.equals(CatalogCategory.defaultId))
                    activityCallback.showCategoryList(targetId);
                else
                    activityCallback.showCollectionList(targetId);
            }
        });
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
    public void updateCategoryList(List<CatalogCategory> catalogCategoryList) {
        adapter.replaceDataSet(catalogCategoryList);
    }
}
