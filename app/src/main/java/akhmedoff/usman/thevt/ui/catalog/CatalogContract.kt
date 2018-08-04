package akhmedoff.usman.thevt.ui.catalog

import akhmedoff.usman.data.model.CatalogItem
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList

interface CatalogContract {

    interface View : LifecycleOwner {

        var presenter: Presenter

        fun showList(videos: PagedList<CatalogItem>)

        fun showVideo(item: CatalogItem, view: android.view.View)

        fun showAlbum(album: CatalogItem, view: android.view.View)

        fun showLoading(isRefreshing: Boolean)

        fun showErrorLoading()

        fun showEmptyList()

        fun getPageCategory(): String

        fun upRecyclerView()
    }

    interface Presenter {

        fun onCreated()

        fun onStop()

        fun refresh()

        fun loadCatalogs()

    }
}