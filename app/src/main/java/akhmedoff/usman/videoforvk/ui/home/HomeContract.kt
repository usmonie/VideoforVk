package akhmedoff.usman.videoforvk.ui.home

interface HomeContract {

    interface View {

        var presenter: Presenter

        fun initPage(pageCategory: String, pageTitle: String)

        fun startSearch()

        fun getResourcesString(id: Int): String
    }

    interface Presenter {

        var view: View?

        fun onCreated()

        fun searchClicked()

        fun onDestroyed()
    }
}