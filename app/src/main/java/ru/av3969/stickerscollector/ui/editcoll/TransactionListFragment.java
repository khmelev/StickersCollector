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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.base.BaseFragment;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

public class TransactionListFragment extends BaseFragment {

    private TransactionListAdapter transactionListAdapter;

    private StickersListAdapter stickersListAdapter;

    private EditCollectionActivityCallback activityCallback;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.trans_recycler_view)
    RecyclerView transRecyclerView;

    @BindView(R.id.trans_row_recycler_view)
    RecyclerView transRowRecyclerView;

    @BindView(R.id.backButton)
    ImageButton backButton;

    @BindView(R.id.transTitleText)
    EditText transTitleText;

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

        backButton.setOnClickListener(v -> {
            viewFlipper.showPrevious();
            activityCallback.updateFabVisibility();
        });

        activityCallback.loadTransactionList();

    }

    public void updateTransactionList(List<TransactionVO> transactionList) {
        if (transactionListAdapter != null)
            transactionListAdapter.replaceDataSet(transactionList);
    }

    public void showTransactionRow(List<StickerVO> stickers, TransactionVO transaction) {
        stickersListAdapter.replaceDataSet(stickers);
        viewFlipper.showNext();
        transTitleText.setText(transaction.getTitle());
        activityCallback.updateFabVisibility();
    }

    public void saveTransactionRows() {
        activityCallback.saveTransactionRows(transTitleText.getText());
        viewFlipper.showPrevious();
        activityCallback.updateFabVisibility();
    }

    private void setupRecyclerView() {
        transactionListAdapter = new TransactionListAdapter(new ArrayList<>(),
                (t) -> activityCallback.deactivateTransaction(t),
                (t) -> activityCallback.loadTransactionRow(t),
                (t) -> activityCallback.copyTextToClipboard(t));
        transRecyclerView.setAdapter(transactionListAdapter);
        transRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        stickersListAdapter = new StickersListAdapter(StickersListAdapter.EDIT_TRANS_MODE);
        transRowRecyclerView.setAdapter(stickersListAdapter);
        transRowRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public int getCurrentScreenNumber() {
        return viewFlipper.getCurrentView().getId() == R.id.trans_recycler_view
                ? 1 : 2;
    }
}
