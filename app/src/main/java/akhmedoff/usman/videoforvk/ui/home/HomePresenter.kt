package akhmedoff.usman.videoforvk.ui.home

import akhmedoff.usman.videoforvk.R

class HomePresenter(override var view: HomeContract.View? = null) : HomeContract.Presenter {

    override fun onCreated() {
        view?.let { view ->
            view.initPage("feed", view.getResourcesString(R.string.tab_title_feed))
            view.initPage("top", view.getResourcesString(R.string.tab_title_top))
            view.initPage("ugc", view.getResourcesString(R.string.tab_title_ugc))
            view.initPage("series", view.getResourcesString(R.string.tab_title_series_and_tv))
        }
    }

    override fun onDestroyed() {
        view = null
    }
}