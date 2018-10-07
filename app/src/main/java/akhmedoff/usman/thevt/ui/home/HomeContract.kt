package akhmedoff.usman.thevt.ui.home

import akhmedoff.usman.data.model.Catalog
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList

interface HomeContract {

    interface View : LifecycleOwner {

        var presenter: Presenter

        fun getResourcesString(id: Int): String

        fun startSearch()

        fun setLoading(isLoading: Boolean)

        fun setList(items: PagedList<Catalog>)

        fun showErrorLoading()
    }

    interface Presenter {

        var view: View?

        fun onCreated()

        fun onRetained()

        fun refresh()

        fun onDestroyed()
    }
}