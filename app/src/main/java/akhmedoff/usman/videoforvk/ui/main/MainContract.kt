package akhmedoff.usman.videoforvk.ui.main


interface MainContract {

    interface View {
        var mainPresenter: Presenter

        fun showHome()

        fun showProfile()

        fun showLooking()

        fun showSettings()

        fun hidePrevious()

        fun showLastFragment()

    }

    interface Presenter {
        var view: View?
        fun onCreate()

        fun onRecreate()

        fun forwardTo(id: Int)
    }
}