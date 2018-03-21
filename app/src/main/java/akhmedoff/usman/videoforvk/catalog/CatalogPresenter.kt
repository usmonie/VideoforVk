package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.repository.CatalogRepository
import android.arch.lifecycle.Observer

class CatalogPresenter(
    private var view: CatalogContract.View?,
    private val catalogRepository: CatalogRepository
) : CatalogContract.Presenter {

    override fun onCreated() = refresh()

    override fun onStop() {
    }

    override fun refresh() {
        view?.showLoading()
        loadCatalogs()
    }

    override fun loadCatalogs() {
        view?.let { view ->
            catalogRepository
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
    }

}