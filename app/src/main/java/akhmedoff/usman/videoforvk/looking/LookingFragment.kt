package akhmedoff.usman.videoforvk.looking

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.album.AlbumActivity
import akhmedoff.usman.videoforvk.search.SearchActivity
import akhmedoff.usman.videoforvk.videonew.VideoFragment
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_catalog.*
import kotlinx.android.synthetic.main.fragment_looking.*
import kotlinx.android.synthetic.main.search_toolbar.*

class LookingFragment : Fragment(), LookingContract.View {

    companion object {
        const val FRAGMENT_TAG = "looking_fragment_tag"
    }

    override lateinit var presenter: LookingContract.Presenter

    private val adapter by lazy {
        LookingRecyclerAdapter { presenter.onCatalogItemClicked(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = LookingPresenter(
            this,
            getVideoRepository(context!!, AppDatabase.getInstance(context!!).ownerDao())
        )

        return inflater.inflate(R.layout.fragment_looking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreated()
        looking_recycler.adapter = adapter

        search_box_collapsed.setOnClickListener { presenter.searchClicked() }
    }

    override fun startSearch() = startActivity(Intent(context, SearchActivity::class.java))

    override fun showVideo(item: CatalogItem) {
        activity?.supportFragmentManager?.let {
            Router.replaceFragment(
                it,
                VideoFragment.getInstance(item),
                true,
                VideoFragment.FRAGMENT_TAG
            )
        }
    }

    override fun showAlbum(album: CatalogItem) =
        startActivity(AlbumActivity.getActivity(album, context!!))

    override fun showLoading() {
        update_looking_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_looking_layout.isRefreshing = false
    }

    override fun showErrorLoading() =
        Snackbar.make(
            update_catalog_layout,
            getText(R.string.error_loading),
            Snackbar.LENGTH_LONG
        ).show()


    override fun setList(items: PagedList<Catalog>) = adapter.submitList(items)

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }
}
