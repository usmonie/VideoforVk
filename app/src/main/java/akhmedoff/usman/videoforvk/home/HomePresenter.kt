package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.arch.lifecycle.Observer

class HomePresenter(private val videoRepository: VideoRepository) : BasePresenter<HomeContract.View>(), HomeContract.Presenter {
    override fun clickCatalog(catalog: Catalog) {
        view.showCatalog(catalog)
    }

    override fun clickVideo(video: VideoCatalog) {
        view.showVideo(video)
    }

    override fun onPresenterCreated() {
        super.onPresenterCreated()
        videoRepository.getCatalog()
            .observe(
                view,
                Observer {
            when {
                it?.response != null ->
                    view.showList(it.response.catalogs)
                else -> {

                }
            }
        })
    }
}