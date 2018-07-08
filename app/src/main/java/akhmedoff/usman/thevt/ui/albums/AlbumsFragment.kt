package akhmedoff.usman.thevt.ui.albums

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.utils.getAlbumRepository
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.Router
import akhmedoff.usman.thevt.ui.album.AlbumFragment
import akhmedoff.usman.thevt.ui.video.VideoActivity
import akhmedoff.usman.thevt.ui.view.MarginItemDecorator
import akhmedoff.usman.thevt.ui.view.adapters.AlbumsRecyclerAdapter
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_catalog.*

private const val OWNER_ID = "owner_id"
private const val TRANSITION_NAME = "transition_name"

class AlbumsFragment : Fragment(), AlbumsContract.View {

    companion object {

        fun getFragment(ownerId: String, transitionName: String) = AlbumsFragment().apply {
            val bundle = Bundle()
            bundle.putString(OWNER_ID, ownerId)
            bundle.putString(TRANSITION_NAME, transitionName)

            arguments = bundle
        }
    }

    override lateinit var presenter: AlbumsContract.Presenter

    private val adapter: AlbumsRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AlbumsRecyclerAdapter({ video, view -> showAlbum(video, view) }, R.layout.catalog_video_item_big)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_catalog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = AlbumsPresenter(
                this,
                getAlbumRepository(context!!)
        )
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

    override fun getOwnerId() = arguments?.getString(OWNER_ID)

    override fun showLoading(isLoading: Boolean) {
        update_catalog_layout.isRefreshing = isLoading
    }

    private fun showAlbum(item: Album, view: View) {
        val fragment = AlbumFragment.getFragment(item, view.transitionName)

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(
                    fragmentManager,
                    this,
                    fragment,
                    true,
                    VideoActivity.FRAGMENT_TAG,
                    view
            )
        }
    }

    override fun showErrorLoading() {
    }

    override fun showEmptyList() {
    }

    override fun setList(items: PagedList<Album>) = adapter.submitList(items)

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }
}