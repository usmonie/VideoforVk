package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.model.CatalogItem
import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedList

interface CatalogContract {

    interface View : LifecycleOwner {

        var presenter: Presenter

        fun showList(videos: PagedList<CatalogItem>)

        fun showVideo(item: CatalogItem, view: android.view.View)

        fun showAlbum(album: CatalogItem, view: android.view.View)

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()

        fun showEmptyList()

        fun getPageCategory(): String

    }

    interface Presenter {

        fun onCreated()

        fun onStop()

        fun refresh()

        fun clickItem(item: CatalogItem)

        fun loadCatalogs()

    }
}