package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.videoforvk.base.BaseContract
import android.arch.paging.PagedList

interface CatalogContract {

    interface View : BaseContract.View {

        var catalogPresenter: Presenter

        fun showList(videos: PagedList<CatalogItem>)

        fun showVideo(item: CatalogItem)

        fun showAlbum(album: CatalogItem)

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()

        fun showEmptyList()

        fun getPageCategory(): String

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun onCreated()

        fun onStop()

        fun refresh()

        fun clickItem(item: CatalogItem)

        fun loadCatalogs()

    }
}