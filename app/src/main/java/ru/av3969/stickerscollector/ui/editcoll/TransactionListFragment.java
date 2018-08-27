package ru.av3969.stickerscollector.ui.editcoll;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.base.BaseFragment;

public class TransactionListFragment extends BaseFragment {

    private TransactionListAdapter adapter;

    private EditCollectionActivityCallback activityCallback;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EditCollectionActivityCallback)
            activityCallback = (EditCollectionActivityCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setupRecyclerView();

        activityCallback.loadTransactionList();
    }

    public void updateTransactionList(List<Transaction> transactionList) {
        if (adapter != null)
            adapter.replaceDataSet(transactionList);
    }

    private void setupRecyclerView() {
        adapter = new TransactionListAdapter(new ArrayList<>(), (t) -> {activityCallback.deactivateTransaction(t);});
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
