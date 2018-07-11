package akhmedoff.usman.thevt.ui.favourites

import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedList

interface FavouritesContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun showVideos(videos: PagedList<Video>)

        fun showLoading(isLoading: Boolean)

        fun showError()


        fun showEmptyList()

        fun showVideo(item: Video, view: android.view.View)
    }

    interface Presenter {
        var view: View?

        fun onCreated()

        fun onDestroyed()

        fun refresh()

        fun onVideoClicked(item: Video)
    }
}