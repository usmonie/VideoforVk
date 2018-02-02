package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog

interface MainContract {

    interface View : BaseContract.View {

        var mainPresenter: Presenter

        fun showList(videos: MutableList<Catalog>)

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

        fun loadCatalogs(next: String?, items: Int = 10, filters: String = "other")
    }
}