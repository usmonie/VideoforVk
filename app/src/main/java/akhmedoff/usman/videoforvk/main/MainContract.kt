package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import android.arch.paging.PagedList
import android.view.MotionEvent

interface MainContract {

    interface View : BaseContract.View {

        var mainPresenter: Presenter

        fun showList(videos: PagedList<Catalog>)

        fun showVideo(item: CatalogItem)

        fun showAlbum(album: CatalogItem)

        fun showCatalog(catalog: Catalog)

        fun showLoading()

        fun hideLoading()

        fun showErrorLoading()

        fun showProfile()

        fun showUserAvatar(avatarUrl: String)

        fun showUserName(name: String)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun refresh()

        fun clickCatalog(catalog: Catalog)

        fun clickItem(item: CatalogItem)

        fun loadCatalogs()

        fun error(error: Error, message: String)

        fun pressEvent(item: CatalogItem, event: MotionEvent)
    }
}