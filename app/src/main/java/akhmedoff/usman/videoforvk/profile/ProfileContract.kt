package akhmedoff.usman.videoforvk.profile

interface ProfileContract {

    interface View {
        var presenter: Presenter

        fun showUserName(name: String)

        fun showUserPhoto(photoUrl: String)

        fun setIsUser(isUser: Boolean)

        fun showTabs()

        fun hideTabs()

        fun showPages(ownerId: String)

        fun startSearch()

        fun showError(message: String)

        fun showLoading(isLoading: Boolean)

        fun getUserId(): String?
    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun onDestroyed()

        fun onSearchClicked()

        fun onViewCreated()


    }
}