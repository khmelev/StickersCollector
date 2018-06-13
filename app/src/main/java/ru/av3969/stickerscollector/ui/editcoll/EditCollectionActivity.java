package ru.av3969.stickerscollector.ui.editcoll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.ui.base.BaseActivity;

public class EditCollectionActivity extends BaseActivity implements EditCollectionContract.View {

    public static final String PARENT_COLLECTION = "parentCollection";
    public static final String COLLECTION_ID = "collectionId";

    private Long parentCollection;
    private Long collectionId;

    @Inject
    EditCollectionContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collTitle)
    TextView collTitle;

    @BindView(R.id.releaseYear)
    TextView releaseYear;

    @BindView(R.id.textStickersOrCards)
    TextView textStickersOrCards;

    @BindView(R.id.textNumberOfStickers)
    TextView textNumberOfStickers;

    @BindView(R.id.collDescription)
    TextView collDescription;

    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collection);

        getActivityComponent().inject(this);

        presenter.setView(this);

        if (savedInstanceState != null) {
            parentCollection = savedInstanceState.getLong(PARENT_COLLECTION, 0L);
            collectionId = savedInstanceState.getLong(COLLECTION_ID, 0L);
        } else {
            Intent intent = getIntent();
            parentCollection = intent.getLongExtra(PARENT_COLLECTION, 0L);
            collectionId = intent.getLongExtra(COLLECTION_ID, 0L);
        }

        ButterKnife.bind(this);

        // Set up the toolbar.
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.collection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_collection, menu);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        presenter.loadCollectionHead(parentCollection, collectionId);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(PARENT_COLLECTION, parentCollection);
        outState.putLong(COLLECTION_ID, collectionId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void updateCollectionHead(CatalogCollection catalogCollection) {
        collTitle.setText(catalogCollection.getTitle());
        releaseYear.setText(String.valueOf(catalogCollection.getYear()));
        @StringRes int resStickersOrCards = catalogCollection.getStype().equals(CatalogCollection.stickerType)
                ? R.string.quantity_of_stickers : R.string.quantity_of_cards;
        textStickersOrCards.setText(resStickersOrCards);
        textNumberOfStickers.setText(String.valueOf(catalogCollection.getSize()));
        collDescription.setText(catalogCollection.getDesc());
    }

    @Override
    public void updateStickersList() {

    }

    @Override
    public void showLoading() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    @Override
    public void hideLoading() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }
}
