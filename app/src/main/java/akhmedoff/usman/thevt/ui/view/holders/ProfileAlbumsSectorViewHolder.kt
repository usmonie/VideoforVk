package akhmedoff.usman.thevt.ui.view.holders

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.view.MarginItemDecorator
import akhmedoff.usman.thevt.ui.view.adapters.AlbumsRecyclerAdapter
import android.arch.paging.PagedList
import android.support.v7.widget.DefaultItemAnimator
import android.view.View
import kotlinx.android.synthetic.main.albums_item.view.*

class ProfileAlbumsSectorViewHolder(clickListener: (Album, View) -> Unit, itemView: View) : AbstractViewHolder<PagedList<Album>>(itemView) {

    val cardView = itemView.albums_card_view

    private val adapter = AlbumsRecyclerAdapter(clickListener, R.layout.album_item).apply {
        itemView.albums_recycler.adapter = this
        itemView.albums_recycler.itemAnimator = DefaultItemAnimator()
        itemView.albums_recycler.addItemDecoration(MarginItemDecorator(0, itemView.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)))
    }

    init {

    }

    override fun bind(item: PagedList<Album>) = adapter.submitList(item)
}