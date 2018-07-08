package akhmedoff.usman.videoforvk.ui.videos

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.ui.video.VideoActivity
import akhmedoff.usman.videoforvk.ui.view.MarginItemDecorator
import akhmedoff.usman.videoforvk.ui.view.VideosRecyclerAdapter
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
import kotlinx.android.synthetic.main.fragment_catalog.*

class VideosFragment : Fragment(), VideosContract.View {

    companion object {
        private const val OWNER_ID = "owner_id"

        fun createFragment(ownerId: String) = VideosFragment().apply {
            val bundle = Bundle()
            bundle.putString(OWNER_ID, ownerId)

            arguments = bundle
        }
    }

    override lateinit var presenter: VideosContract.Presenter

    private val adapter: VideosRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VideosRecyclerAdapter(
                { video, view -> showVideo(video, view) },
                R.layout.catalog_video_item_big
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = VideosPresenter(
                this,
                getVideoRepository(context!!)
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_catalog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catalog_recycler.adapter = adapter

        catalog_recycler.itemAnimator = DefaultItemAnimator()
        catalog_recycler.addItemDecoration(
                MarginItemDecorator(
                        1,
                        resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
                )
        )
        update_catalog_layout.setOnRefreshListener { presenter.refresh() }

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
        update_catalog_layout.isRefreshing = isLoading
        empty_state_text_view.isVisible = !isLoading
    }

    override fun showError() {
        Snackbar
                .make(
                        update_catalog_layout,
                        R.string.error_loading,
                        Snackbar.LENGTH_LONG
                )
                .setAction(R.string.retry) {
                    presenter.refresh()
                }
                .show()
    }

    override fun getOwnerId() = arguments?.getString(OWNER_ID)

    override fun showEmptyList() {
        empty_state_text_view.isVisible = true
    }
}