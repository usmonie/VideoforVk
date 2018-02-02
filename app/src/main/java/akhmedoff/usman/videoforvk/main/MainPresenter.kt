package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.ResponseCatalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class MainPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<MainContract.View>(), MainContract.Presenter {

    private var response: ResponseCatalog? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = loadCatalogs(null)

    override fun refresh() = loadCatalogs(null)

    override fun loadCatalogs(next: String?, items: Int, filters: String) {
        view?.showLoading()

        view?.let {
            videoRepository
                .getCatalog(items, next, filters)
                .observe(
                    it,
                    Observer { responseCatalog ->
                        response = responseCatalog?.response

                        when {
                            responseCatalog?.response != null -> {
                                it.showList(responseCatalog.response.catalogs)
                            }
                            else -> it.showErrorLoading()
                        }
                        it.hideLoading()
                    }
                )
        }

    }

    override fun clickCatalog(catalog: Catalog) {
        view?.showCatalog(catalog)
    }

    override fun clickVideo(video: VideoCatalog) {
        view?.showVideo(video)
    }
}