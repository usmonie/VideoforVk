package akhmedoff.usman.videoforvk.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Observer

class SearchPresenter(
    private val videoRepository: VideoRepository
) :
    BasePresenter<SearchContract.View>(), SearchContract.Presenter {
    override fun onBackClicked() {
        view?.onBackClicked()
    }

    override fun search() {
        view?.let { view ->
            with(view) {
                if (getQueryText().isNotBlank())
                    videoRepository.search(
                        getQueryText(),
                        getSortFilter(),
                        getHdFilter(),
                        getAdultFilter(),
                        getLengthFilter(),
                        searchOwn(),
                        null,
                        null
                    ).observe(
                        view,
                        Observer {
                            it?.let { pagedList -> showFoundVideos(pagedList) }
                        }
                    )
            }
        }
    }

    override fun onClick(item: Video) {
        view?.showVideo(item)
    }
}