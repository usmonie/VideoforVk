package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Observer

class CatalogPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<CatalogContract.View>(), CatalogContract.Presenter {

    override fun onCreated() = refresh()

    override fun onStop() {
    }

    override fun refresh() {
        view?.showLoading()
        loadCatalogs()
    }

    override fun loadCatalogs() {
        view?.let { view ->
            videoRepository
                .getCatalogSection(view.getPageCategory())
                .observe(view, Observer { pagedList ->
                    when {
                        pagedList != null -> {
                            view.hideLoading()
                            when {
                                pagedList.size > 0 -> view.showList(pagedList)
                                else -> view.showEmptyList()
                            }
                        }
                        else -> {
                            view.hideLoading()

                            view.showErrorLoading()
                        }
                    }
                })
        }
    }

    override fun clickItem(item: CatalogItem) {
        when (item.type) {
            CatalogItemType.VIDEO -> view?.showVideo(item)

            CatalogItemType.ALBUM -> view?.showAlbum(item)
        }
    }

}