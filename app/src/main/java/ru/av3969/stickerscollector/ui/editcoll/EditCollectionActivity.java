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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import ru.av3969.stickerscollector.ui.vo.TransactionVO;
import ru.av3969.stickerscollector.utils.SoftKeyboard;

public class EditCollectionActivity extends BaseActivity implements EditCollectionContract.View, EditCollectionActivityCallback {

    public static final String PARENT_COLLECTION    = "parentCollection";
    public static final String COLLECTION_ID        = "collectionId";

    public static final int STICKERS_PAGE       = 0;
    public static final int INCOME_PAGE         = 1;
    public static final int OUTLAY_PAGE         = 2;
    public static final int TRANSACTIONS_PAGE   = 3;

    public static final int FIRST_SCREEN        = 1;
    public static final int SECOND_SCREEN       = 2;

    private PagerAdapter pagerAdapter;

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

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    MenuItem miSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collection);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        presenter.setView(this);

        if (savedInstanceState != null) {
            presenter.loadCollectionHead(savedInstanceState.getLong(PARENT_COLLECTION, 0L),
                                            savedInstanceState.getLong(COLLECTION_ID, 0L));
        } else {
            Intent intent = getIntent();
            presenter.loadCollectionHead(intent.getLongExtra(PARENT_COLLECTION, 0L),
                                            intent.getLongExtra(COLLECTION_ID, 0L));
        }

        // Set up the toolbar.
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        setupFabClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_collection, menu);

        miSave = menu.findItem(R.id.miSave);

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
        outState.putLong(PARENT_COLLECTION, presenter.getParentCollection());
        outState.putLong(COLLECTION_ID, presenter.getCollectionId());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void loadStickersList() {
        presenter.loadStickersList(false);
    }

    @Override
    public void loadTransactionList() {
        presenter.loadTransactionList(false);
    }

    public void loadTransactionList(Boolean forceLoad) {
        presenter.loadTransactionList(forceLoad);
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
    public void saveTransactionRows(CharSequence transTitle) {
        presenter.saveTransactionRows(transTitle);
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
    public void showLoading(int page) {
        if(page != viewPager.getCurrentItem())
            return;

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        updateFabVisibility();
//        if (miSave != null) {
//            miSave.setVisible(false);
//        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        updateFabVisibility();
//        if (miSave != null) {
//            miSave.setVisible(true);
//        }
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
        int currentPageId = viewPager.getCurrentItem();

        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1, true);
        loadTransactionList(true);

        IncomeOutlayFragment pageFragment = (IncomeOutlayFragment)pagerAdapter.getPageFragment(currentPageId);
        if(pageFragment != null) {
            pageFragment.showFirstScreen();
        }
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
        presenter.assembleStickersVOAsText();
    }

    @Override
    public void showAvailableStickersAsText(CharSequence text) {
        Fragment fragment = pagerAdapter.getItem(StickersListFragment.class);
        if (fragment != null) {
            ((StickersListFragment)fragment).showAvailableStickersAsText(text);
        }
    }

    @Override
    public void showAlertDialog(String title, String message) {
        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(title, message);
        alertDialogFragment.show(getSupportFragmentManager(), "alert_dialog");
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
    public void showTransactionRow(List<StickerVO> stickers, TransactionVO transaction) {
        Fragment fragment = pagerAdapter.getItem(TransactionListFragment.class);
        if (fragment != null) {
            ((TransactionListFragment)fragment).showTransactionRow(stickers, transaction);
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

    private void setupFabClickListener() {
        floatingActionButton.setOnClickListener(l -> {
            int position = viewPager.getCurrentItem();
            Fragment fragment = pagerAdapter.getPageFragment(position);
            if(fragment == null) return;

            switch (position) {
                case STICKERS_PAGE:
                    ((StickersListFragment) fragment).switchScreen();
                    break;
                case INCOME_PAGE:
                case OUTLAY_PAGE:
                    switch (getFragmentScreenNumber(position)) {
                        case FIRST_SCREEN:
                            ((IncomeOutlayFragment)fragment).checkIncomeOutlayStickers();
                            break;
                        case SECOND_SCREEN:
                            ((IncomeOutlayFragment)fragment).acceptIncomeOutlayStickers();
                            break;
                    }
                    break;
                case TRANSACTIONS_PAGE:
                    SoftKeyboard.hide(this);
                    ((TransactionListFragment) fragment).saveTransactionRows();
                    break;
            }
            setupFabVisibility(position);
        });
    }

    private void setupFabVisibility(int position) {
        if(progressBar.getVisibility() == View.VISIBLE) {
            floatingActionButton.setVisibility(View.INVISIBLE);
            return;
        }
        switch (position) {
            case STICKERS_PAGE:
                floatingActionButton.setVisibility(View.VISIBLE);
                switch (getFragmentScreenNumber(position)) {
                    case FIRST_SCREEN:
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_stickers_as_text));
                        break;
                    case SECOND_SCREEN:
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_stickers_as_list));
                        break;
                }
                break;
            case INCOME_PAGE:
            case OUTLAY_PAGE:
                floatingActionButton.setVisibility(View.VISIBLE);
                switch (getFragmentScreenNumber(position)) {
                    case FIRST_SCREEN:
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_stickers_as_list));
                        break;
                    case SECOND_SCREEN:
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
                        break;
                }
                break;
            case TRANSACTIONS_PAGE:
                switch (getFragmentScreenNumber(position)) {
                    case FIRST_SCREEN:
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        break;
                    case SECOND_SCREEN:
                        floatingActionButton.setVisibility(View.VISIBLE);
                        floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
                        break;
                }
                break;
            default:
                floatingActionButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateFabVisibility() {
        setupFabVisibility(viewPager.getCurrentItem());
    }

    private int getFragmentScreenNumber(int position) {
        Fragment fragment = pagerAdapter.getPageFragment(position);
        if(fragment == null) return 0;
        switch (position) {
            case STICKERS_PAGE:
                return ((StickersListFragment) fragment).getCurrentScreenNumber();
            case INCOME_PAGE:
            case OUTLAY_PAGE:
                return ((IncomeOutlayFragment) fragment).getCurrentScreenNumber();
            case TRANSACTIONS_PAGE:
                return ((TransactionListFragment) fragment).getCurrentScreenNumber();
        }
        return 0;
    }

    private void closeSoftKeyboard(int position) {
        switch (position) {
            case STICKERS_PAGE:
            case TRANSACTIONS_PAGE:
                SoftKeyboard.hide(this);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setupFabVisibility(position);
                closeSoftKeyboard(position);
                //Что бы не было падений после поворота экрана
                presenter.loadStickersList(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    class PagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mFragments = new Fragment[4];
        private String[] mFragmentTitles= {
                getStringFromRes(R.string.list),
                getStringFromRes(R.string.income),
                getStringFromRes(R.string.outlay),
                getStringFromRes(R.string.transactions)
        };

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment getItem(Class klass) {
            for (Fragment mFragment : mFragments) {
                if (mFragment != null && mFragment.getClass() == klass)
                    return mFragment;
            }
            return null;
        }

        Fragment getPageFragment(int position) {
            return mFragments[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = mFragments[position];
            if (frag == null)
            {
                switch (position) {
                    case STICKERS_PAGE:
                        frag = new StickersListFragment();
                        break;
                    case INCOME_PAGE:
                        frag = IncomeOutlayFragment.newIncomeFragment();
                        break;
                    case OUTLAY_PAGE:
                        frag = IncomeOutlayFragment.newOutlayFragment();
                        break;
                    case TRANSACTIONS_PAGE:
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
