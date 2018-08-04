package akhmedoff.usman.thevt.ui.explore

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.utils.getCatalogRepository
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.Router
import akhmedoff.usman.thevt.ui.album.AlbumFragment
import akhmedoff.usman.thevt.ui.search.SearchFragment
import akhmedoff.usman.thevt.ui.video.VideoActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment(), ExploreContract.View {

    companion object {
        const val FRAGMENT_TAG = "explore_fragment_tag"
        const val RETAINED_KEY = "retained"
        const val LIST_STATE_KEY = "list_state_key"
    }

    override lateinit var presenter: ExploreContract.Presenter

    private val adapter: ExploreRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
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

        if (savedInstanceState == null || !savedInstanceState.containsKey(RETAINED_KEY)) {
            presenter.onCreated()
        } else if (savedInstanceState.get(RETAINED_KEY) == true) {
            presenter.onRetained()
        }

        if (savedInstanceState?.containsKey(LIST_STATE_KEY) == true) {
            looking_recycler?.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY))
        }
        looking_recycler.adapter = adapter
        looking_recycler.itemAnimator = DefaultItemAnimator()
        search_box_collapsed.setOnClickListener { presenter.searchClicked() }
        update_looking_layout.setOnRefreshListener { presenter.refresh() }
    }

    override fun showVideo(item: CatalogItem, view: View) {
        val intent = VideoActivity.getInstance(item,
                ViewCompat.getTransitionName(view), context!!)

        Router.startActivityWithTransition(activity!!, intent, view)
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
                    VideoActivity.FRAGMENT_TAG,
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(RETAINED_KEY, true)

        val listState = looking_recycler?.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, listState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState?.containsKey(RETAINED_KEY) == false) {
            presenter.onCreated()
        } else if (savedInstanceState?.get(RETAINED_KEY) == true) {
            presenter.onRetained()
        }

        if (savedInstanceState?.containsKey(LIST_STATE_KEY) == true) {
            looking_recycler?.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY))
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }
}
