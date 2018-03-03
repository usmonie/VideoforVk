package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.album.AlbumActivity
import akhmedoff.usman.videoforvk.base.BaseFragment
import akhmedoff.usman.videoforvk.search.SearchActivity
import akhmedoff.usman.videoforvk.video.VideoActivity
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    override lateinit var homePresenter: HomeContract.Presenter

    private val adapter: MainRecyclerAdapter by lazy {

        val adapter = MainRecyclerAdapter { presenter.clickItem(it) }

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        homePresenter =
                HomePresenter(
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
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_recycler.adapter = adapter

        update_home_layout.setOnRefreshListener { homePresenter.refresh() }

        search_box_collapsed.setOnClickListener { homePresenter.searchClicked() }
    }

    override fun showList(videos: PagedList<Catalog>) = adapter.setList(videos)

    override fun showVideo(item: CatalogItem) =
        startActivity(VideoActivity.getActivity(item, context!!))

    override fun showAlbum(album: CatalogItem) =
        startActivity(AlbumActivity.getActivity(album, context!!))

    override fun startSearch() = startActivity(Intent(context, SearchActivity::class.java))

    override fun showCatalog(catalog: Catalog) {
    }

    override fun showLoading() {
        update_home_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_home_layout.isRefreshing = false
    }

    override fun showErrorLoading() =
        Snackbar.make(main_layout, getText(R.string.error_loading), Snackbar.LENGTH_LONG).show()

    override fun initPresenter() = homePresenter
}
