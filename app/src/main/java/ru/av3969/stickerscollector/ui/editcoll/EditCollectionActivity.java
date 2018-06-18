package ru.av3969.stickerscollector.ui.editcoll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.ui.base.BaseActivity;
import ru.av3969.stickerscollector.ui.main.MainActivity;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class EditCollectionActivity extends BaseActivity implements EditCollectionContract.View {

    public static final String PARENT_COLLECTION = "parentCollection";
    public static final String COLLECTION_ID = "collectionId";

    private Long parentCollection;
    private Long collectionId;

    private StickersListAdapter adapter;

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

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    MenuItem miActionProgressItem;
    MenuItem miSave;

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
            //ab.setDisplayShowTitleEnabled(false);
        }
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_collection, menu);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        miSave = menu.findItem(R.id.miSave);

        presenter.loadCollectionHead(parentCollection, collectionId);
        presenter.loadStickersList(parentCollection, collectionId);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSave:
                presenter.saveCollection();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void setupRecyclerView() {
        adapter = new StickersListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateCollectionHead(CollectionVO collectionVO) {
        collTitle.setText(collectionVO.getTitle());
        releaseYear.setText(String.valueOf(collectionVO.getYear()));
        textStickersOrCards.setText(collectionVO.getStype().equals(CatalogCollection.stickerType)
                ? R.string.quantity_of_stickers : R.string.quantity_of_cards);
        textNumberOfStickers.setText(String.valueOf(collectionVO.getSize()));
        collDescription.setText(collectionVO.getDesc());
    }

    @Override
    public void updateStickersList(List<StickerVO> stickers) {
        adapter.replaceDataSet(stickers);
    }

    @Override
    public void showLoading() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
        if (miSave != null) {
            miSave.setVisible(false);
        }
    }

    @Override
    public void hideLoading() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
        if (miSave != null) {
            miSave.setVisible(true);
        }
    }

    @Override
    public void collectionSaved() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
