package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class HomePresenter(
    private val videoRepository: VideoRepository
) :
    BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun stateUpdated() {
        view?.let { view ->
            if (view.lifecycle.currentState == Lifecycle.State.STARTED)
                refresh()

        }
    }

    override fun refresh() {
        view?.let { view ->
            if (view.lifecycle.currentState == Lifecycle.State.STARTED)
                view.showLoading()
        }
        //loadUserInfo()
        loadCatalogs()
    }

    override fun loadCatalogs() {
        view?.let { view ->
            videoRepository
                .getCatalog()
                .observe(view, Observer { pagedList ->
                    pagedList?.let { catalogs ->
                        view.hideLoading()
                        view.showList(catalogs)
                    }
                })
        }
    }

    override fun clickCatalog(catalog: Catalog) {
        view?.showCatalog(catalog)
    }

    override fun clickItem(item: CatalogItem) {
        when (item.type) {
            CatalogItemType.VIDEO -> view?.showVideo(item)

            CatalogItemType.ALBUM -> view?.showAlbum(item)
        }
    }

    override fun searchClicked() {
        view?.startSearch()
    }

    override fun error(error: Error, message: String) {

    }
}