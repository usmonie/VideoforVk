package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Item

interface HomeContract {

    interface View : BaseContract.View {
        var homePresenter: Presenter
        fun showList(items: List<Item>)

        fun showVideo(id: Int)

        fun showCatalogs()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun clickItem(id: Int)
    }
}