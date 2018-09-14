package ru.av3969.stickerscollector.ui.main;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class MyCollectionsListPresenter extends BasePresenter
        implements MyCollectionsListContract.Presenter {

    private MyCollectionsListContract.View view;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    List<CollectionVO> collectionsVO;

    @Inject
    MyCollectionsListPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                               CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void setView(MyCollectionsListContract.View view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }

    @Override
    public void loadCollectionsList(boolean forceLoad) {
        if(!forceLoad && collectionsVO != null && !collectionsVO.isEmpty()) {
            view.updateCollectionsList(collectionsVO);
            return;
        }
        compositeDisposable.add(
                dataManager.loadCollectionsVO()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(collectionsVO -> {
                            this.collectionsVO = collectionsVO;
                            view.updateCollectionsList(collectionsVO);
                        })
        );
    }

    @Override
    public void commitCollectionsOrder() {
        if(collectionsVO == null || collectionsVO.isEmpty()) return;
        compositeDisposable.add(
                dataManager.commitCollectionsOrder(collectionsVO)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe()
        );
    }

    @Override
    public void destroyCollections(List<CollectionVO> collectionsForDestroy) {
        if (collectionsForDestroy == null) return;
        compositeDisposable.add(
                dataManager.destroyCollections(collectionsForDestroy)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe()
        );
    }
}
