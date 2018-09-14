package ru.av3969.stickerscollector.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
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

    ItemTouchHelper itemTouchHelper;

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
        presenter.loadCollectionsList(false);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setupRecyclerView() {
        adapter = new MyCollectionsListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener((parentCollectionId, collectionId) -> {
            if (activityCallback != null) {
                activityCallback.startEditCollectionActivity(parentCollectionId, collectionId);
            }
        });
        setupItemTouchHelper();
    }

    public void setupItemTouchHelper() {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //Get the from and to position
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                //Swap the items and notify the adapter
                adapter.swapItem(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeItem(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
        });
    }

    @Override
    public void updateCollectionsList(List<CollectionVO> collections) {
        adapter.replaceDataSet(collections);
    }

    public void setEditMode(boolean editMode) {
        adapter.setEditMode(editMode);
        itemTouchHelper.attachToRecyclerView(editMode ? recyclerView : null);
    }

    public void loadCollectionsList() {
        if(presenter != null)
            presenter.loadCollectionsList(true);
    }

    public void commitChanges() {

    }
}
