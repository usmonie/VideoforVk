package akhmedoff.usman.videoforvk.ui.profile

interface ProfileContract {

    interface View {
        var presenter: Presenter

        fun showUserName(name: String)

        fun showUserPhoto(photoUrl: String)

        fun setIsUser(isUser: Boolean)

        fun showTabs(isShowing: Boolean)

        fun showPages(ownerId: String)

        fun startSearch()

        fun showError(message: String)

        fun showLoading(isLoading: Boolean)

        fun getUserId(): String?

        fun getIsUser(): Boolean
    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun onDestroyed()

        fun onSearchClicked()

        fun onViewCreated()


    }
}