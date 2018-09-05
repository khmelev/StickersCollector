package ru.av3969.stickerscollector.ui.editcoll;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.internal.operators.flowable.FlowableAllSingle;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.base.BaseActivity;
import ru.av3969.stickerscollector.ui.main.MainActivity;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

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

    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

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

        setupFAB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_collection, menu);

        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        miSave = menu.findItem(R.id.miSave);

        presenter.loadCollectionHead(parentCollection, collectionId);

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

    @Override
    public void loadStickersList() {
        presenter.loadStickersList(parentCollection, collectionId);
    }

    @Override
    public void loadTransactionList() {
        presenter.loadTransactionList(collectionId);
    }

    @Override
    public void deactivateTransaction(TransactionVO transaction) {
        if (transaction != null) {
            presenter.deactivateTransaction(transaction);
        }
    }

    @Override
    public void loadTransactionRow(TransactionVO transaction) {
        if (transaction != null) {
            presenter.loadTransactionRowList(transaction);
        }
    }

    @Override
    public void saveTransactionRows() {
        presenter.saveTransactionRows();
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
    public void commitIncomeStickers(CharSequence transTitle) {
        presenter.commitIncomeStickers(transTitle);
    }

    @Override
    public void commitOutlayStickers(CharSequence transTitle) {
        presenter.commitOutlayStickers(transTitle);
    }

    @Override
    public void updateStickersList(List<StickerVO> stickers) {
        Fragment fragment = pagerAdapter.getItem(StickersListFragment.class);
        if (fragment != null) {
            ((StickersListFragment)fragment).updateStickersList(stickers);
        }
    }

    @Override
    public void assembleStickersAsText() {
        presenter.assembleStickersAsText();
    }

    @Override
    public void showAvailableStickersAsText(CharSequence text) {
        Fragment fragment = pagerAdapter.getItem(StickersListFragment.class);
        if (fragment != null) {
            ((StickersListFragment)fragment).showAvailableStickersAsText(text);
        }
    }

    @Override
    public void updateTransactionList(List<TransactionVO> transactionList) {
        Fragment fragment = pagerAdapter.getItem(TransactionListFragment.class);
        if (fragment != null) {
            ((TransactionListFragment)fragment).updateTransactionList(transactionList);
        }
    }

    @Override
    public void showIncomeStickers(List<StickerVO> stickers) {
        Fragment currentPage = pagerAdapter.getItem(viewPager.getCurrentItem());
        if(currentPage instanceof IncomeOutlayFragment && ((IncomeOutlayFragment) currentPage).incomeMode()) {
            ((IncomeOutlayFragment) currentPage).showParsedStickers(stickers);
        }
    }

    @Override
    public void showOutlayStickers(List<StickerVO> stickers, String comment) {
        Fragment currentPage = pagerAdapter.getItem(viewPager.getCurrentItem());
        if(currentPage instanceof IncomeOutlayFragment && ((IncomeOutlayFragment) currentPage).outlayMode()) {
            ((IncomeOutlayFragment) currentPage).showParsedStickers(stickers, comment);
        }
    }

    @Override
    public void showTransactionRow(List<StickerVO> stickers) {
        Fragment fragment = pagerAdapter.getItem(TransactionListFragment.class);
        if (fragment != null) {
            ((TransactionListFragment)fragment).showTransactionRow(stickers);
        }
    }

    @Override
    public void showMsg(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void copyTextToClipboard(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("simple text", text));
            showMsg(getStringFromRes(R.string.copied_to_clipboard));
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        String[] mFragmentTitles = {
                getResources().getString(R.string.list),
                getResources().getString(R.string.income),
                getResources().getString(R.string.outlay),
                getResources().getString(R.string.transactions)
        };
        pagerAdapter = new Adapter(getSupportFragmentManager(), mFragmentTitles);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        floatingActionButton.setVisibility(View.VISIBLE);
                        break;
                    default:
                        floatingActionButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupFAB() {
        floatingActionButton.setOnClickListener(l -> {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    Fragment fragment = pagerAdapter.getItem(StickersListFragment.class);
                    if (fragment != null) {
                        ((StickersListFragment) fragment).toogleView();
                    }
                    break;
            }
        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private Fragment[] mFragments = new Fragment[4];
        private String[] mFragmentTitles;

        public Adapter(FragmentManager fm, String[] mFragmentTitles) {
            super(fm);
            this.mFragmentTitles = mFragmentTitles;
        }

        public Fragment getItem(Class klass) {
            for (Fragment mFragment : mFragments) {
                if (mFragment != null && mFragment.getClass() == klass)
                    return mFragment;
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = mFragments[position];
            if (frag == null)
            {
                switch (position) {
                    case 0:
                        frag = new StickersListFragment();
                        break;
                    case 1:
                        frag = IncomeOutlayFragment.newIncomeFragment();
                        break;
                    case 2:
                        frag = IncomeOutlayFragment.newOutlayFragment();
                        break;
                    case 3:
                        frag = new TransactionListFragment();
                        break;
                }
                mFragments[position] = frag;
            }
            return frag;
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object ret = super.instantiateItem(container, position);
            mFragments[position] = (Fragment) ret;
            return ret;
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles[position];
        }
    }
}
