package akhmedoff.usman.videoforvk.ui.catalog

import akhmedoff.usman.data.repository.CatalogRepository
import android.arch.lifecycle.Observer

class CatalogPresenter(
        private var view: CatalogContract.View?,
        private val catalogRepository: CatalogRepository
) : CatalogContract.Presenter {

    override fun onCreated() = refresh()

    override fun onStop() {
        view = null
    }

    override fun refresh() {
        view?.showLoading(true)
        loadCatalogs()
    }

    override fun loadCatalogs() {
        view?.let { view ->
            catalogRepository
                    .getCatalogSection(view.getPageCategory())
                    .observe(view, Observer { pagedList ->
                        when {
                            pagedList == null -> view.showErrorLoading()
                            pagedList.size > 0 -> view.showList(pagedList)
                            else -> view.showEmptyList()
                        }

                        view.showLoading(true)
                    })
        }
    }
}