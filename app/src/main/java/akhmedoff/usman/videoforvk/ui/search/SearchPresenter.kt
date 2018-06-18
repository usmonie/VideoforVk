package akhmedoff.usman.videoforvk.ui.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer

class SearchPresenter(
    private var view: SearchContract.View?,
    private val videoRepository: VideoRepository
) : SearchContract.Presenter {
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
    }
}