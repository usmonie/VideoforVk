package akhmedoff.usman.thevt.ui.profile

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Video
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList

interface ProfileContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun showUserName(name: String)

        fun showUserStatus(status: String)

        fun showUserPhoto(photoUrl: String)

        fun setIsUser(isUser: Boolean)

        fun showAlbums(albums: PagedList<Album>)

        fun showVideos(videos: PagedList<Video>)

        fun showFaveVideos(videos: PagedList<Video>)

        fun showLoadingError()

        fun showLoading(isLoading: Boolean)

        fun getUserId(): String?

        fun getIsUser(): Boolean

        fun showUi(isVisible: Boolean)

        fun showStartPositionVideos()

    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun onDestroyed()

        fun onViewCreated()

        fun refresh()

    }
}