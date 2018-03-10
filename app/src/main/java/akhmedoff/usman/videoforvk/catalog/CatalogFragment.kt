package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.album.AlbumActivity
import akhmedoff.usman.videoforvk.base.BaseFragment
import akhmedoff.usman.videoforvk.video.VideoActivity
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_catalog.*

class CatalogFragment : BaseFragment<CatalogContract.View, CatalogContract.Presenter>(),
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

    override lateinit var catalogPresenter: CatalogContract.Presenter

    private val adapter by lazy {
        CatalogRecyclerAdapter { presenter.clickItem(it) }
            .apply { setHasStableIds(true) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        catalogPresenter =
                CatalogPresenter(
                    getVideoRepository(
                        context!!,
                        AppDatabase.getInstance(context!!).ownerDao()
                    )
                )
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_catalog, container, false)

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

        update_catalog_layout.setOnRefreshListener { catalogPresenter.refresh() }

        catalogPresenter.onCreated()
    }

    override fun showList(videos: PagedList<CatalogItem>) = adapter.submitList(videos)

    override fun showVideo(item: CatalogItem) =
        startActivity(VideoActivity.getActivity(item, context!!))

    override fun showAlbum(album: CatalogItem) =
        startActivity(AlbumActivity.getActivity(album, context!!))

    override fun getPageCategory() = arguments?.getString(PAGE_CATEGORY) ?: ""

    override fun showEmptyList() =
        Snackbar.make(
            update_catalog_layout,
            getText(R.string.empty_list),
            Snackbar.LENGTH_LONG
        ).show()

    override fun showLoading() {
        update_catalog_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_catalog_layout.isRefreshing = false
    }

    override fun showErrorLoading() =
        Snackbar.make(
            update_catalog_layout,
            getText(R.string.error_loading),
            Snackbar.LENGTH_LONG
        ).show()

    override fun initPresenter() = catalogPresenter
}
