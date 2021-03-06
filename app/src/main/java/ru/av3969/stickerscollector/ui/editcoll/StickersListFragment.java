package ru.av3969.stickerscollector.ui.editcoll;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.base.BaseFragment;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class StickersListFragment extends BaseFragment {

    private StickersListAdapter adapter;

    private EditCollectionActivityCallback activityCallback;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.stickersAsText)
    TextView stickersAsText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EditCollectionActivityCallback)
            activityCallback = (EditCollectionActivityCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stickers_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setupRecyclerView();

        activityCallback.loadStickersList();

        stickersAsText.setOnLongClickListener(v -> {
            activityCallback.copyTextToClipboard(stickersAsText.getText());
            return true;
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupRecyclerView() {
        adapter = new StickersListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void updateStickersList(List<StickerVO> stickers) {
        if (adapter != null)
            adapter.replaceDataSet(stickers);
    }

    public void switchScreen() {
        if(getCurrentScreenNumber() == 1) {
            activityCallback.assembleStickersAsText();
        }
        viewFlipper.showNext();
    }

    public int getCurrentScreenNumber() {
        return viewFlipper.getCurrentView().getId() == R.id.recycler_view
                ? 1 : 2;
    }

    public void showAvailableStickersAsText(CharSequence text) {
        stickersAsText.setText(text);
    }
}
