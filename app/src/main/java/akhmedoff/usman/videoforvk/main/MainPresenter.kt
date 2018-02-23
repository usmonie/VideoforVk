package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class MainPresenter(
    private val userRepository: UserRepository,
    private val videoRepository: VideoRepository
) :
    BasePresenter<MainContract.View>(), MainContract.Presenter {


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = refresh()

    override fun refresh() {
        view?.showLoading()
        //loadUserInfo()
        loadCatalogs()
    }

    private fun loadUserInfo() {
        view?.let { view ->
            userRepository
                .getUsers()
                .observe(view, Observer { users ->
                    users?.let {
                        if (it.isNotEmpty()) {
                            val user = it[0]

                            view.showUserName(user.firstName + " " + user.lastName)

                            if (user.hasPhoto) view.showUserAvatar(user.photo100)
                        }
                    }

                })
        }
    }

    override fun loadCatalogs() {
        view?.let { view ->
            videoRepository
                .getCatalog()
                .observe(view, Observer { pagedList ->
                    pagedList?.let { catalogs ->
                        view.hideLoading()
                        view.showList(catalogs)
                    }
                })
        }
    }

    override fun clickCatalog(catalog: Catalog) {
        view?.showCatalog(catalog)
    }

    override fun clickItem(item: CatalogItem) {
        when (item.type) {
            CatalogItemType.VIDEO -> view?.showVideo(item)

            CatalogItemType.ALBUM -> view?.showAlbum(item)
        }
    }

    override fun searchClicked() {
        view?.startSearch()
    }

    override fun error(error: Error, message: String) {

    }
}