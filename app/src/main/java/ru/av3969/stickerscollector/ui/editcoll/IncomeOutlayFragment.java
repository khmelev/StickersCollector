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
import ru.av3969.stickerscollector.utils.SoftKeyboard;

public class IncomeOutlayFragment extends BaseFragment {

    public static int INCOME_MODE = 1;
    public static int OUTLAY_MODE = 2;

    private int mode = INCOME_MODE;

    private EditCollectionActivityCallback activityCallback;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.inputTransTitle)
    EditText inputTransTitle;

    @BindView(R.id.inputStickerList)
    EditText inputStickerList;

    @BindView(R.id.backButton)
    ImageButton backButton;

    @BindView(R.id.transTitleText)
    TextView transTitleText;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.comment)
    TextView commentView;

    private StickersListAdapter adapter;

    public static IncomeOutlayFragment newIncomeFragment() {
        IncomeOutlayFragment incomeFragment = new IncomeOutlayFragment();
        Bundle args = new Bundle();
        args.putInt("mode", INCOME_MODE);
        incomeFragment.setArguments(args);
        return incomeFragment;
    }

    public static IncomeOutlayFragment newOutlayFragment() {
        IncomeOutlayFragment outlayFragment = new IncomeOutlayFragment();
        Bundle args = new Bundle();
        args.putInt("mode", OUTLAY_MODE);
        outlayFragment.setArguments(args);
        return outlayFragment;
    }

    public Boolean incomeMode() {
        return mode == INCOME_MODE;
    }

    public Boolean outlayMode() {
        return mode == OUTLAY_MODE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getArguments().getInt("mode");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof EditCollectionActivityCallback)
            activityCallback = (EditCollectionActivityCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income_outlay, container, false);
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
    }

    private void setupRecyclerView() {
        adapter = new StickersListAdapter(new ArrayList<>(), mode == OUTLAY_MODE
                                                            ? StickersListAdapter.CHECK_REMAINS_MODE
                                                            : StickersListAdapter.ONLY_POSITIVE_MODE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void showParsedStickers(List<StickerVO> stickers) {
        adapter.replaceDataSet(stickers);
        viewFlipper.showNext();
        activityCallback.updateFabVisibility();
    }

    public void showParsedStickers(List<StickerVO> stickers, String comment) {
        if(TextUtils.isEmpty(comment))
            commentView.setVisibility(View.GONE);
        else {
            commentView.setText(comment);
            commentView.setVisibility(View.VISIBLE);
            commentView.setOnLongClickListener(v -> {
                activityCallback.copyTextToClipboard(comment);
                return true;
            });
        }
        showParsedStickers(stickers);
    }

    public void checkIncomeOutlayStickers() {
        transTitleText.setText(inputTransTitle.getText());
        if (getContext() != null && getView() != null)
            SoftKeyboard.hide(getContext(), getView());
        if (incomeMode())
            activityCallback.parseIncomeStickers(inputStickerList.getText());
        else
            activityCallback.parseOutlayStickers(inputStickerList.getText());
    }

    public void acceptIncomeOutlayStickers() {
        if(incomeMode())
            activityCallback.commitIncomeStickers(inputTransTitle.getText());
        else
            activityCallback.commitOutlayStickers(inputTransTitle.getText());
    }

    public int getCurrentScreenNumber() {
        return viewFlipper.getCurrentView().getId() == R.id.screen1
                ? 1 : 2;
    }
}
