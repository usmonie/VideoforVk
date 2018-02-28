package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.album.AlbumActivity
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.search.SearchActivity
import akhmedoff.usman.videoforvk.video.VideoActivity
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    override lateinit var mainPresenter: MainContract.Presenter

    private val adapter: MainRecyclerAdapter by lazy {

        val adapter = MainRecyclerAdapter { presenter.clickItem(it) }

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter =
                MainPresenter(getVideoRepository(this, AppDatabase.getInstance(this).ownerDao()))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        home_recycler.adapter = adapter

        update_home_layout.setOnRefreshListener { mainPresenter.refresh() }

        search_box_collapsed.setOnClickListener { mainPresenter.searchClicked() }
    }

    override fun showUserAvatar(avatarUrl: String) {
    }

    override fun showUserName(name: String) {

    }

    override fun showList(videos: PagedList<Catalog>) = adapter.setList(videos)

    override fun showVideo(item: CatalogItem) = startActivity(VideoActivity.getActivity(item, this))

    override fun showAlbum(album: CatalogItem) =
        startActivity(AlbumActivity.getActivity(album, this))

    override fun showProfile() {
    }

    override fun startSearch() = startActivity(Intent(this, SearchActivity::class.java))

    override fun showCatalog(catalog: Catalog) {
    }

    override fun showLoading() {
        update_home_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_home_layout.isRefreshing = false
    }

    override fun showErrorLoading() =
        Snackbar.make(main_layout, getText(R.string.error_loading), Snackbar.LENGTH_LONG).show()

    override fun initPresenter() = mainPresenter
}
