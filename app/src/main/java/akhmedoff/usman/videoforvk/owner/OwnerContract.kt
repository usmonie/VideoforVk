package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Group
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.base.BaseContract
import android.arch.paging.PagedList

interface OwnerContract {
    interface View : BaseContract.View {

        var ownerPresenter: Presenter

        fun showOwnerInfo(owner: User)

        fun showOwnerInfo(owner: Group)

        fun showVideos(items: PagedList<Video>)

        fun showAlbums(items: PagedList<Album>)

        fun showVideo(item: Video)

        fun showAlbum(item: Album)

        fun showFriends()

        fun showFavourites()

        fun getOwnerType(): String

        fun getOwnerId(): String

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun clicked(item: Album)

        fun clicked(item: Video)

        fun openFriends()

        fun openFavourites()
    }
}