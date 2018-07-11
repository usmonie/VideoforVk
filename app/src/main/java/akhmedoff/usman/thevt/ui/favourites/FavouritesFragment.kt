package akhmedoff.usman.thevt.ui.favourites

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.Router
import akhmedoff.usman.thevt.ui.video.VideoActivity
import akhmedoff.usman.thevt.ui.view.MarginItemDecorator
import akhmedoff.usman.thevt.ui.view.adapters.SearchRecyclerAdapter
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : Fragment(), FavouritesContract.View {

    override lateinit var presenter: FavouritesContract.Presenter

    private val adapter: SearchRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SearchRecyclerAdapter(
                { video, view -> showVideo(video, view) },
                R.layout.search_videos
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = FavouritesPresenter(this, getVideoRepository(context!!))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favourites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favourites_recycler.adapter = adapter

        favourites_recycler.itemAnimator = DefaultItemAnimator()
        favourites_recycler.addItemDecoration(
                MarginItemDecorator(
                        1,
                        resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
                )
        )
        update_favourites_layout.setOnRefreshListener { presenter.refresh() }

        presenter.onCreated()

    }

    override fun showVideo(item: Video, view: View) {
        val intent = VideoActivity.getInstance(item,
                ViewCompat.getTransitionName(view), context!!)

        Router.startActivityWithTransition(activity!!, intent, view)
    }

    override fun showVideos(videos: PagedList<Video>) {
        empty_state_text_view.isVisible = videos.size <= 0
        adapter.submitList(videos)
    }

    override fun showLoading(isLoading: Boolean) {
        update_favourites_layout.isRefreshing = isLoading
        empty_state_text_view.isVisible = false
    }

    override fun showError() {
        Snackbar
                .make(
                        update_favourites_layout,
                        R.string.error_loading,
                        Snackbar.LENGTH_LONG
                )
                .setAction(R.string.retry) {
                    presenter.refresh()
                }
                .show()
    }


    override fun showEmptyList() {
        empty_state_text_view.isVisible = true
    }
}