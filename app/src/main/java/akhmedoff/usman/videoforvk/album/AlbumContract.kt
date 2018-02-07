package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Video
import android.arch.paging.PagedList

interface AlbumContract {

    interface View : BaseContract.View {
        var albumPresenter: Presenter

        fun showVideos(items: PagedList<Video>)

        fun showAlbumTitle(title: String)

        fun showVideo(video: Video)

        fun setAdded(isAdded: Boolean)

        fun getAlbumId(): String?

        fun getAlbumOwnerId(): String?
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun clickVideo(video: Video)

        fun clickAdd()
    }
}