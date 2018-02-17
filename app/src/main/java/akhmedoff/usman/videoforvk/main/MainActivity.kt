package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.album.AlbumActivity
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.utils.vkApi
import akhmedoff.usman.videoforvk.video.VideoActivity
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    override lateinit var mainPresenter: MainContract.Presenter

    private val adapter: MainRecyclerAdapter by lazy {

        val adapter = MainRecyclerAdapter { presenter.clickItem(it) }

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter = MainPresenter(VideoRepository(vkApi))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getText(R.string.main_screen)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        home_recycler.layoutManager = layoutManager
        home_recycler.adapter = adapter

        update_home_layout.setOnRefreshListener { mainPresenter.refresh() }
    }

    override fun showProfile() {
    }

    override fun showList(videos: PagedList<Catalog>) = adapter.setList(videos)

    override fun showVideo(item: CatalogItem) = startActivity(VideoActivity.getActivity(item, this))

    override fun showAlbum(album: CatalogItem) =
        startActivity(AlbumActivity.getActivity(album, this))

    override fun showCatalog(catalog: Catalog) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun showLoading() {
        update_home_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_home_layout.isRefreshing = false
    }

    override fun showErrorLoading() =
        Snackbar.make(activity_main, getText(R.string.error_loading), Snackbar.LENGTH_LONG).show()

    override fun initPresenter() = mainPresenter
}
