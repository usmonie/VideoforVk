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
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
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
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        val player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), trackSelector)
        player.volume = 0f

        adapter.player = player

        val layoutManager = LinearLayoutManager(context, VERTICAL, false)
        home_recycler.layoutManager = layoutManager

        home_recycler.adapter = adapter

        home_recycler.setItemViewCacheSize(20)
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