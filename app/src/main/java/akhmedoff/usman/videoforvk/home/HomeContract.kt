package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.videoforvk.base.BaseContract
import android.arch.paging.PagedList

interface HomeContract {

    interface View : BaseContract.View {

        var homePresenter: Presenter

        fun showList(videos: PagedList<Catalog>)

        fun showVideo(item: CatalogItem)

        fun showAlbum(album: CatalogItem)

        fun showCatalog(catalog: Catalog)

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()

        fun startSearch()

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun refresh()

        fun clickCatalog(catalog: Catalog)

        fun clickItem(item: CatalogItem)

        fun loadCatalogs()

        fun error(error: Error, message: String)

        fun searchClicked()
    }
}