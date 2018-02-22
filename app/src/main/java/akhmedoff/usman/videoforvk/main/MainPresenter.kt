package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.CatalogItemType
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.view.MotionEvent

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

    override fun error(error: Error, message: String) {

    }

    override fun pressEvent(item: CatalogItem, event: MotionEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}