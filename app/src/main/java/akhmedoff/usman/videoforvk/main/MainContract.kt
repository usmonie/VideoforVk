package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.arch.paging.PagedList

interface MainContract {

    interface View : BaseContract.View {

        var mainPresenter: Presenter

        fun showList(videos: PagedList<Catalog>)

        fun showVideo(video: VideoCatalog)

        fun showCatalog(catalog: Catalog)

        fun showCatalogs()

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()

        fun showProfile()
    }

    interface Presenter : BaseContract.Presenter<MainContract.View> {

        fun refresh()

        fun clickCatalog(catalog: Catalog)

        fun clickVideo(video: VideoCatalog)

        fun loadCatalogs()

        fun pagination()
    }
}