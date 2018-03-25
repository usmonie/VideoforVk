package akhmedoff.usman.videoforvk.looking

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.repository.CatalogRepository
import android.arch.lifecycle.Observer

class LookingPresenter(
    override var view: LookingContract.View?,
    private val catalogRepository: CatalogRepository
) : LookingContract.Presenter {

    override fun onCreated() = refresh()

    override fun refresh() {
        view?.let { view ->
            view.showLoading()
            catalogRepository
                .getCatalog("other")
                .observe(view, Observer { catalogs ->
                    if (catalogs != null && catalogs.size > 0) {
                        view.hideLoading()
                        view.setList(catalogs)
                    } else {
                        view.hideLoading()
                        view.showErrorLoading()
                    }
                })
        }
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