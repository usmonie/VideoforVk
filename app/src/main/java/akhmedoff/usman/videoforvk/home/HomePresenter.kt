package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.ResponseCatalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class HomePresenter(private val videoRepository: VideoRepository) :
    BasePresenter<HomeContract.View>(), HomeContract.Presenter {
    private var response: ResponseCatalog? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        loadCatalogs()
    }

    override fun loadCatalogs(items: Int, filters: String) {
        view?.let {
            videoRepository
                .getCatalog(items, response?.next, filters)
                .observe(
                    view!!,
                    Observer {
                        response = it?.response
                        view?.showLoading()

                        when {
                            it?.response != null -> {
                                view?.hideLoading()
                                view?.showList(it.response.catalogs)
                            }
                            else -> view?.showErrorLoading()
                        }
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