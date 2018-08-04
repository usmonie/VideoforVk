package akhmedoff.usman.thevt.ui.search

import akhmedoff.usman.data.model.Video
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList

interface SearchContract {

    interface View : LifecycleOwner {
        var searchPresenter: Presenter

        fun showFoundVideos(videos: PagedList<Video>)

        fun expandFilters()

        fun collapseFilters()

        fun showError(errorMessage: String)

        fun getQueryText(): String

        fun getHdFilter(): Int

        fun getAdultFilter(): Int

        fun getSortFilter(): Int

        fun getLengthFilter(): String

        fun searchOwn(): Boolean

        fun getLonger(): Long

        fun getShorter(): Long

        fun onBackClicked()
        fun showVideo(item: Video, view: android.view.View)
    }

    interface Presenter {
        fun onClick(item: Video)

        fun onBackClicked()

        fun search()
    }
}