package akhmedoff.usman.videoforvk.looking

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedList

interface LookingContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()

        fun setList(items: PagedList<Catalog>)

        fun showVideo(item: CatalogItem)

        fun showAlbum(album: CatalogItem)

        fun startSearch()
    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun searchClicked()

        fun onDestroyed()

        fun refresh()

        fun onCatalogItemClicked(item: CatalogItem)
    }
}