package akhmedoff.usman.thevt.ui.album

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedList

interface AlbumContract {

    interface View : LifecycleOwner {
        var albumPresenter: Presenter

        fun showVideos(items: PagedList<Video>)

        fun showAlbumTitle(title: String)

        fun showAlbumImage(poster: String)

        fun showVideo(video: Video, view: android.view.View)

        fun setAdded(isAdded: Boolean)

        fun getAlbumId(): String?

        fun getAlbumOwnerId(): String?

        fun getAlbumTitle(): String?
    }

    interface Presenter {
        fun onCreated()

        fun clickVideo(video: Video)

        fun clickAdd()

        fun error(error: Error, message: String)

    }
}