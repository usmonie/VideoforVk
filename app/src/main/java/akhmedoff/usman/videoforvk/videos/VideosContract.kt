package akhmedoff.usman.videoforvk.videos

import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedList

interface VideosContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun showVideos(videos: PagedList<Video>)

        fun showLoading(isLoading: Boolean)

        fun showError(message: String)

        fun getOwnerId(): String?

        fun showEmptyList()

        fun showVideo(item: Video)
    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun onDestroyed()

        fun refresh()

        fun onVideoClicked(item: Video)
    }
}