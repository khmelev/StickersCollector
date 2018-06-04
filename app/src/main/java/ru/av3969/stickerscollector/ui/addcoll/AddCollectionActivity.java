package ru.av3969.stickerscollector.ui.addcoll;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class AddCollectionActivity extends BaseActivity {

    public static final int REQUEST_ADD_COLL = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);
    }

    private void showCategoryList() {

    }
}
