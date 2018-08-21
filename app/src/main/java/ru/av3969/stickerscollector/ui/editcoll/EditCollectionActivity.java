package ru.av3969.stickerscollector.ui.editcoll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.base.BaseActivity;
import ru.av3969.stickerscollector.ui.main.MainActivity;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class EditCollectionActivity extends BaseActivity implements EditCollectionContract.View, EditCollectionActivityCallback {

    public static final String PARENT_COLLECTION = "parentCollection";
    public static final String COLLECTION_ID = "collectionId";

    private Long parentCollection;
    private Long collectionId;

    private Adapter pagerAdapter;

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

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

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
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
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

    private void setupViewPager(ViewPager viewPager) {

        pagerAdapter = new Adapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new StickersListFragment(), getResources().getString(R.string.list));
        pagerAdapter.addFragment(IncomeOutlayFragment.newIncomeFragment(), getResources().getString(R.string.income));
        pagerAdapter.addFragment(IncomeOutlayFragment.newOutlayFragment(), getResources().getString(R.string.outlay));
        pagerAdapter.addFragment(new TransactionListFragment(), getResources().getString(R.string.transactions));
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment currentPage = pagerAdapter.getItem(position);
                if (currentPage instanceof StickersListFragment) {
                    //loadStickersList();
                } else if (currentPage instanceof TransactionListFragment) {
                    presenter.loadTransactionList(collectionId);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
        Fragment currentPage = pagerAdapter.getItem(viewPager.getCurrentItem());
        if(currentPage instanceof StickersListFragment) {
            ((StickersListFragment)currentPage).updateStickersList(stickers);
        }
    }

    @Override
    public void updateTransactionList(List<Transaction> transactionList) {
        Fragment currentPage = pagerAdapter.getItem(viewPager.getCurrentItem());
        if(currentPage instanceof TransactionListFragment) {
            ((TransactionListFragment)currentPage).updateTransactionList(transactionList);
        }
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
    public String getStringFromRes(@StringRes int resId) {
        return getResources().getString(resId);
    }

    @Override
    public void collectionSaved() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void transactionSaved() {
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1, true);
    }

    @Override
    public void parseIncomeStickers(CharSequence stickerString) {
        presenter.parseIncomeStickers(stickerString);
    }

    @Override
    public void parseOutlayStickers(CharSequence stickerString) {
        presenter.parseOutlayStickers(stickerString);
    }

    @Override
    public void commitIncomeStickers() {
        presenter.commitIncomeStickers();
    }

    @Override
    public void commitOutlayStickers() {
        presenter.commitOutlayStickers();
    }

    @Override
    public void showIncomeStickers(List<StickerVO> stickers) {
        Fragment currentPage = pagerAdapter.getItem(viewPager.getCurrentItem());
        if(currentPage instanceof IncomeOutlayFragment && ((IncomeOutlayFragment) currentPage).incomeMode()) {
            ((IncomeOutlayFragment) currentPage).showParsedStickes(stickers);
        }
    }

    @Override
    public void showOutlayStickers(List<StickerVO> stickers) {
        Fragment currentPage = pagerAdapter.getItem(viewPager.getCurrentItem());
        if(currentPage instanceof IncomeOutlayFragment && ((IncomeOutlayFragment) currentPage).outlayMode()) {
            ((IncomeOutlayFragment) currentPage).showParsedStickes(stickers);
        }
    }

    @Override
    public void showError(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
