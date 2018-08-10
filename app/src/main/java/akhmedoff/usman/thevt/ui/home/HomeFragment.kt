package akhmedoff.usman.thevt.ui.home

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.utils.getCatalogRepository
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.Router
import akhmedoff.usman.thevt.ui.album.AlbumFragment
import akhmedoff.usman.thevt.ui.profile.ProfileFragment
import akhmedoff.usman.thevt.ui.search.SearchFragment
import akhmedoff.usman.thevt.ui.video.VideoActivity
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.catalog_item.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), HomeContract.View {

    companion object {
        const val FRAGMENT_TAG = "home_fragment_tag"
        const val RETAINED_KEY = "retained"
    }

    override lateinit var presenter: HomeContract.Presenter

    private val adapter: HomeRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeRecyclerAdapter { item, view ->
            when (item.type) {
                CatalogItemType.VIDEO -> showVideo(item, view)

                CatalogItemType.ALBUM -> showAlbum(item, view)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = HomePresenter(this, getCatalogRepository(context!!))

        presenter.onCreated()

        home_recycler.adapter = adapter
        home_recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
            }
        })

        profile_button.setOnClickListener {
            Router.replaceFragment(
                    activity?.supportFragmentManager!!,
                    this,
                    ProfileFragment(),
                    true,
                    ProfileFragment.FRAGMENT_TAG,
                    it,
                    home_title
            )
        }

        search_button.setOnClickListener { startSearch() }

        home_title.setOnClickListener {
            catalog_recycler.smoothScrollToPosition(0)
        }

        update_looking_layout.setOnRefreshListener { presenter.refresh() }
    }

    override fun onStart() {
        super.onStart()
        presenter.view = this
    }

    override fun getResourcesString(id: Int) = context?.getString(id) ?: ""

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }

    override fun startSearch() {
        Router.replaceFragment(
                activity?.supportFragmentManager!!,
                this,
                SearchFragment(),
                true,
                SearchFragment.FRAGMENT_TAG,
                search_button
        )
    }

    override fun setLoading(isLoading: Boolean) {
        update_looking_layout.isRefreshing = isLoading
    }

    override fun setList(items: PagedList<Catalog>) {
        adapter.submitList(items)
    }

    override fun showErrorLoading() = Snackbar.make(
            update_looking_layout,
            getText(R.string.error_loading),
            Snackbar.LENGTH_LONG
    ).show()

    private fun showAlbum(album: CatalogItem, view: View) {
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

    private fun showVideo(item: CatalogItem, view: View) {
        val intent = VideoActivity.getInstance(item,
                ViewCompat.getTransitionName(view), context!!)

        Router.startActivityWithTransition(activity!!, intent, view)
    }
}
