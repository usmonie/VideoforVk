package akhmedoff.usman.thevt.ui.home

interface HomeContract {

    interface View {

        var presenter: Presenter

        fun initPage(pageCategory: String, pageTitle: String)

        fun getResourcesString(id: Int): String
    }

    interface Presenter {

        var view: View?

        fun onCreated()

        fun onDestroyed()
    }
}