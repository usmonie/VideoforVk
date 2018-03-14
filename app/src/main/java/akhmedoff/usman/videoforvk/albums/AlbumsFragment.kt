package akhmedoff.usman.videoforvk.albums

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_catalog.*

class AlbumsFragment : Fragment(), AlbumsContract.View {

    companion object {
        private const val OWNER_ID = "owner_id"

        fun createFragment(ownerId: String) = AlbumsFragment().apply {
            val bundle = Bundle()
            bundle.putString(OWNER_ID, ownerId)

            arguments = bundle
        }
    }

    override lateinit var presenter: AlbumsContract.Presenter

    private val adapter by lazy {
        AlbumsRecyclerAdapter({ presenter.onItemClicked(it) }, R.layout.catalog_video_item_big)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = AlbumsPresenter(
            this,
            getVideoRepository(context!!, AppDatabase.getInstance(context!!).ownerDao())
        )
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

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

        presenter.onCreated()
    }

    override fun getOwnerId() = arguments?.getString(OWNER_ID)

    override fun showLoading(isLoading: Boolean) {
        update_catalog_layout.isRefreshing = isLoading
    }

    override fun showAlbum(item: Album) {

    }

    override fun showErrorLoading() {
    }

    override fun showEmptyList() {
    }

    override fun setList(items: PagedList<Album>) = adapter.submitList(items)
}