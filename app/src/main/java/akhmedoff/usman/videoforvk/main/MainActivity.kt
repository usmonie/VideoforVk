package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.utils.vkApi
import akhmedoff.usman.videoforvk.video.VideoActivity
import akhmedoff.usman.videoforvk.view.AbstractRecyclerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {
    override lateinit var mainPresenter: MainContract.Presenter

    private val adapter: MainRecyclerAdapter by lazy {
        val adapter = MainRecyclerAdapter(object :
            AbstractRecyclerAdapter.OnClickListener<VideoCatalog> {
            override fun onClick(item: VideoCatalog) {
                presenter.clickVideo(item)
            }
        })

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter =
                MainPresenter(VideoRepository(UserSettings.getUserSettings(context), vkApi))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        home_recycler.layoutManager = layoutManager
        home_recycler.adapter = adapter

        update_home_layout.setOnRefreshListener { mainPresenter.refresh() }
    }

    override fun showProfile() {
    }

    override fun showList(videos: MutableList<Catalog>) = adapter.replace(videos)

    override fun showVideo(video: VideoCatalog) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra(
            VideoActivity.VIDEO_ID,
            video.ownerId.toString() + "_" + video.id.toString()
        )

        startActivity(intent)
    }

    override fun showCatalog(catalog: Catalog) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCatalogs() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        update_home_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_home_layout.isRefreshing = false
    }

    override fun showErrorLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): MainContract.Presenter = mainPresenter


}
