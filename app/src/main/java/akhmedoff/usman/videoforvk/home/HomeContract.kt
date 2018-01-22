package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog

interface HomeContract {

    interface View : BaseContract.View {
        var homePresenter: Presenter

        fun showList(videos: List<Catalog>)

        fun showVideo(video: VideoCatalog)

        fun showCatalog(catalog: Catalog)

        fun showCatalogs()

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun clickCatalog(catalog: Catalog)

        fun clickVideo(video: VideoCatalog)

        fun loadCatalogs(items: Int = 10, filters: String = "other")
    }
}