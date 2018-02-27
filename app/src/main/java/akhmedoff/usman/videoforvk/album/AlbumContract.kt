package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.base.BaseContract
import android.arch.paging.PagedList

interface AlbumContract {

    interface View : BaseContract.View {
        var albumPresenter: Presenter

        fun showVideos(items: PagedList<Video>)

        fun showAlbumTitle(title: String)

        fun showAlbumImage(poster: String)

        fun showVideo(video: Video)

        fun setAdded(isAdded: Boolean)

        fun getAlbumId(): String?

        fun getAlbumOwnerId(): String?

        fun getAlbumTitle(): String?
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun clickVideo(video: Video)

        fun clickAdd()

        fun error(error: Error, message: String)

    }
}