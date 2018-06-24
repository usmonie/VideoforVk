package akhmedoff.usman.videoforvk.ui.explore

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.repository.CatalogRepository
import android.arch.lifecycle.Observer

class ExplorePresenter(
        override var view: ExploreContract.View?,
        private val catalogRepository: CatalogRepository
) : ExploreContract.Presenter {

    override fun onCreated() = refresh()

    override fun refresh() {
        view?.let { view ->
            view.showLoading()
            catalogRepository
                    .getCatalog("ugc")
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


    }

    override fun onDestroyed() {
        view = null
    }

    override fun searchClicked() {
        view?.startSearch()
    }
}