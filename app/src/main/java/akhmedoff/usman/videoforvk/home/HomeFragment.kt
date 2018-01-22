package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseFragment
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.utils.vkApi
import akhmedoff.usman.videoforvk.video.VideoFragment
import akhmedoff.usman.videoforvk.view.AbstractRecyclerAdapter.OnClickListener
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {
    override lateinit var homePresenter: HomeContract.Presenter

    private lateinit var adapter: HomeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        homePresenter = HomePresenter(
            VideoRepository(
                UserSettings.getUserSettings(context?.applicationContext!!), vkApi
            )
        )
        super.onCreate(savedInstanceState)

        adapter = HomeRecyclerAdapter(object : OnClickListener<VideoCatalog> {
            override fun onClick(item: VideoCatalog) {
                presenter.clickVideo(item)
            }
        }, object : OnClickListener<Catalog> {
            override fun onClick(item: Catalog) {
                presenter.clickCatalog(item)
            }
        })
        adapter.setHasStableIds(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, VERTICAL, false)
        home_recycler.layoutManager = layoutManager

        home_recycler.adapter = adapter

        home_recycler.setItemViewCacheSize(15)
        home_recycler.isDrawingCacheEnabled = true
        home_recycler.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    }

    override fun showList(videos: List<Catalog>) = adapter.replace(videos.toMutableList())

    override fun showVideo(video: VideoCatalog) {
        val videoFragment = VideoFragment.create(video)
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.content, videoFragment)
            .addToBackStack("")
            .commitAllowingStateLoss()
    }

    override fun showCatalog(catalog: Catalog) {
        Toast.makeText(context, catalog.name, Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showErrorLoading() {
    }

    override fun showCatalogs() {
    }

    override fun initPresenter() = homePresenter
}