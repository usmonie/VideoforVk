package akhmedoff.usman.thevt.ui.albums

import akhmedoff.usman.data.model.Album
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList

interface AlbumsContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun getOwnerId(): String?

        fun showLoading(isLoading: Boolean)

        fun showErrorLoading()

        fun showEmptyList()


        fun setList(items: PagedList<Album>)
    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun onDestroyed()

        fun refresh()

        fun onItemClicked(item: Album)
    }
}