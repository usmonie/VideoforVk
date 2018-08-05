package akhmedoff.usman.thevt.ui.main


interface MainContract {

    interface View {
        var mainPresenter: Presenter

        fun showHome()

        fun showProfile()


        fun showSettings()

        fun showLastFragment()

    }

    interface Presenter {
        var view: View?

        fun onCreate()

        fun onRecreate()

        fun forwardTo(id: Int)

        fun getUserId(): String
    }
}