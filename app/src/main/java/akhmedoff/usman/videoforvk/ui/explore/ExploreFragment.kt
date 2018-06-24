package akhmedoff.usman.videoforvk.ui.explore

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.utils.getCatalogRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.ui.album.AlbumFragment
import akhmedoff.usman.videoforvk.ui.search.SearchFragment
import akhmedoff.usman.videoforvk.ui.video.VideoFragment
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment(), ExploreContract.View {

    companion object {
        const val FRAGMENT_TAG = "looking_fragment_tag"
        const val RETAINED_KEY = "retained"
    }

    override lateinit var presenter: ExploreContract.Presenter

    private val adapter: ExploreRecyclerAdapter by lazy {
        ExploreRecyclerAdapter { item, view ->
            when (item.type) {
                CatalogItemType.VIDEO -> showVideo(item, view)

                CatalogItemType.ALBUM -> showAlbum(item, view)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_explore, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ExplorePresenter(
                this,
                getCatalogRepository(context!!)
        )

        if (savedInstanceState == null || !savedInstanceState.containsKey(RETAINED_KEY))
            presenter.onCreated()

        looking_recycler.adapter = adapter

        search_box_collapsed.setOnClickListener { presenter.searchClicked() }
        update_looking_layout.setOnRefreshListener { presenter.refresh() }
    }

    override fun showVideo(item: CatalogItem, view: View) {
        activity?.supportFragmentManager?.let {
            Router.replaceFragment(
                    it,
                    this,
                    VideoFragment.getInstance(item, ViewCompat.getTransitionName(view)),
                    true,
                    VideoFragment.FRAGMENT_TAG,
                    view
            )
        }
    }

    override fun startSearch() {
        val fragment = SearchFragment()

        activity?.supportFragmentManager?.let {
            Router.replaceFragment(
                    it,
                    this,
                    fragment,
                    true,
                    SearchFragment.FRAGMENT_TAG,
                    search_box_collapsed
            )
        }
    }

    override fun showAlbum(album: CatalogItem, view: View) {
        val fragment = AlbumFragment.getFragment(album, view.transitionName)

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

    override fun showLoading() {
        update_looking_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_looking_layout.isRefreshing = false
    }

    override fun showErrorLoading() = Snackbar.make(
            update_looking_layout,
            getText(R.string.error_loading),
            Snackbar.LENGTH_LONG
    ).show()

    override fun setList(items: PagedList<Catalog>) = adapter.submitList(items)

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }
}
