package ru.av3969.stickerscollector.ui.addcoll;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.ui.base.BaseFragment;

public class AddMyCollectionFragment extends BaseFragment {

    public static String FRAGMENT_TAG = "AddCollection";
    public static String ARGUMENT_PARENT_COLL = "ParentCollection";

    private Long parentCollectionId;

    @BindView(R.id.collectionName)
    EditText collectionNameView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentComponent().inject(this);

        parentCollectionId = getArguments().getLong(ARGUMENT_PARENT_COLL, CatalogCategory.defaultId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mycollection_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        collectionNameView.setText("На основе коллекции "+String.valueOf(parentCollectionId));
    }
}
