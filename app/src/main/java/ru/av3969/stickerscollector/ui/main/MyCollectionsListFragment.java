package ru.av3969.stickerscollector.ui.main;

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
import ru.av3969.stickerscollector.ui.base.BaseFragment;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;

public class MyCollectionsListFragment extends BaseFragment implements MyCollectionsListContract.View {

    public static String FRAGMENT_TAG = "CollectionsList";

    private MyCollectionsListAdapter adapter;

    private MainActivityCallback activityCallback;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    public MyCollectionsListContract.Presenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivityCallback)
            activityCallback = (MainActivityCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mycollections_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setupRecyclerView();

        presenter.setView(this);
        presenter.loadCollectionsList();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupRecyclerView() {
        adapter = new MyCollectionsListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener((parentCollectionId, collectionId) -> {
            if (activityCallback != null) {
                activityCallback.startEditCollectionActivity(parentCollectionId, collectionId);
            }
        });
    }

    @Override
    public void updateCollectionsList(List<CollectionVO> collections) {
        adapter.replaceDataSet(collections);
    }
}
