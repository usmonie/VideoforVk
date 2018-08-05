package akhmedoff.usman.thevt.ui.home

import akhmedoff.usman.data.repository.CatalogRepository
import androidx.lifecycle.Observer

class HomePresenter(override var view: HomeContract.View? = null,
                    private val catalogRepository: CatalogRepository) : HomeContract.Presenter {

    override fun onCreated() = refresh()

    override fun onRetained() {
        refresh()
    }

    override fun refresh() {
        view?.let { view ->
            view.setLoading(true)
            catalogRepository.getCatalog("feed,ugc,top,series,other")
                    .observe(view, Observer { catalogs ->
                        if (catalogs != null && catalogs.size > 0) {
                            view.setLoading(false)
                            view.setList(catalogs)
                        } else {
                            view.setLoading(false)
                            view.showErrorLoading()
                        }
                    })
        }
    }

    override fun onDestroyed() {
        view = null
    }
}