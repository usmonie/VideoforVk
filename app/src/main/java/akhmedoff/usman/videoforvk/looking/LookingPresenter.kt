package akhmedoff.usman.videoforvk.looking

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer

class LookingPresenter(
    override var view: LookingContract.View?,
    private val videoRepository: VideoRepository
) : LookingContract.Presenter {

    override fun onCreated() {
        view?.let { view ->
            view.showLoading()
            videoRepository
                .getCatalog("other")
                .observe(view, Observer {
                    if (it != null && it.size > 0) {
                        view.hideLoading()
                        view.setList(it)
                    } else {
                        view.hideLoading()
                        view.showErrorLoading()
                    }
                })
        }
    }

    override fun refresh() {
    }

    override fun onCatalogItemClicked(item: CatalogItem) {
        when (item.type) {
            CatalogItemType.VIDEO -> view?.showVideo(item)

            CatalogItemType.ALBUM -> view?.showAlbum(item)
        }

    }

    override fun onDestroyed() {
        view = null
    }

    override fun searchClicked() {
        view?.startSearch()
    }
}