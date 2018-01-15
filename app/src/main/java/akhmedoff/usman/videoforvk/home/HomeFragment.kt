package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseFragment
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Item
import akhmedoff.usman.videoforvk.utils.vkApi
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {
    override lateinit var homePresenter: HomeContract.Presenter

    private lateinit var adapter: HomeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        homePresenter = HomePresenter(
                VideoRepository(
                        UserSettings.getUserSettings(context?.applicationContext!!), vkApi))
        super.onCreate(savedInstanceState)

        adapter = HomeRecyclerAdapter()
        adapter.setHasStableIds(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, VERTICAL, false)
        home_recycler.layoutManager = layoutManager

        home_recycler.adapter = adapter

        home_recycler.setItemViewCacheSize(15)
        home_recycler.isDrawingCacheEnabled = true
        home_recycler.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    }

    override fun showList(items: List<Item>) = adapter.setItems(items)

    override fun showVideo(id: Int) {
    }

    override fun showCatalogs() {
    }

    override fun initPresenter() = homePresenter
}