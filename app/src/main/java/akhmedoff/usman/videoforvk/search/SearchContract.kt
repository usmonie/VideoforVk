package akhmedoff.usman.videoforvk.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.base.BaseContract
import android.arch.paging.PagedList

interface SearchContract {

    interface View : BaseContract.View {
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

        fun showVideo(item: Video)

        fun onBackClicked()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onClick(item: Video)

        fun onBackClicked()

        fun search()
    }
}