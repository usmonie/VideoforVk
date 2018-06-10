package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.utils.getCatalogRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.album.AlbumFragment
import akhmedoff.usman.videoforvk.video.VideoFragment
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
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


class CatalogFragment : Fragment(),
        CatalogContract.View {

    companion object {
        const val PAGE_CATEGORY = "page_category"
        const val CATALOG_FRAGMENT_TAG = "catalog_fragment_tag"

        fun createFragment(pageCategory: String) = CatalogFragment().apply {
            val bundle = Bundle()
            bundle.putString(PAGE_CATEGORY, pageCategory)

            arguments = bundle
        }
    }

    override lateinit var presenter: CatalogContract.Presenter

    private val adapter: CatalogRecyclerAdapter by lazy {
        CatalogRecyclerAdapter { item, view ->
            when (item.type) {
                CatalogItemType.VIDEO -> showVideo(item, view)

                CatalogItemType.ALBUM -> showAlbum(item, view)
            }


        }.apply { setHasStableIds(true) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = CatalogPresenter(
                this,
                getCatalogRepository(
                        context!!
                )
        )
        presenter.onCreated()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? =
            inflater.inflate(R.layout.fragment_catalog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catalog_recycler.adapter = adapter
        catalog_recycler.addItemDecoration(
                MarginItemDecorator(
                        1,
                        resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
                )
        )

        catalog_recycler.itemAnimator = DefaultItemAnimator()

        update_catalog_layout.setOnRefreshListener { presenter.refresh() }

    }

    override fun showList(videos: PagedList<CatalogItem>) {
        catalog_recycler.isVisible = true
        empty_state_text_view.isVisible = false
        adapter.submitList(videos)
    }

    override fun showVideo(item: CatalogItem, view: View) {
        val fragment = VideoFragment.getInstance(item,
                ViewCompat.getTransitionName(view))

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

    override fun showAlbum(album: CatalogItem, view: View) {
        val fragment = AlbumFragment.getFragment(album)

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

    override fun getPageCategory() = arguments?.getString(PAGE_CATEGORY) ?: ""

    override fun showEmptyList() {
        catalog_recycler.isVisible = false
        empty_state_text_view.isVisible = true
    }

    override fun showLoading() {
        update_catalog_layout?.isRefreshing = true
    }

    override fun hideLoading() {
        update_catalog_layout?.isRefreshing = false
    }

    override fun showErrorLoading() =
            Snackbar
                    .make(
                            update_catalog_layout,
                            getText(R.string.error_loading),
                            Snackbar.LENGTH_LONG
                    )
                    .setAction(R.string.retry, {
                        presenter.refresh()
                    })
                    .show()
}
