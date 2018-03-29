package akhmedoff.usman.videoforvk.videos

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.video.VideoFragment
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
import akhmedoff.usman.videoforvk.view.VideosRecyclerAdapter
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private val adapter by lazy {
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
        val fragment = VideoFragment.getInstance(item, ViewCompat.getTransitionName(view))

        activity?.supportFragmentManager?.let {
            Router.replaceFragment(
                it,
                this,
                fragment,
                true,
                VideoFragment.FRAGMENT_TAG,
                view
            )
        }
    }

    override fun showVideos(videos: PagedList<Video>) = adapter.submitList(videos)

    override fun showLoading(isLoading: Boolean) {
        update_catalog_layout.isRefreshing = isLoading
    }

    override fun showError(message: String) {
    }

    override fun getOwnerId() = arguments?.getString(OWNER_ID)

    override fun showEmptyList() =
        Snackbar.make(
            update_catalog_layout,
            getText(R.string.empty_list),
            Snackbar.LENGTH_LONG
        ).show()
}