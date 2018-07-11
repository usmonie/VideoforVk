package akhmedoff.usman.thevt.ui.view.holders

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.view.GravitySnapHelper
import akhmedoff.usman.thevt.ui.view.MarginItemDecorator
import akhmedoff.usman.thevt.ui.view.adapters.VideosRecyclerAdapter
import android.arch.paging.PagedList
import android.support.v7.widget.DefaultItemAnimator
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.albums_item.view.*

class FaveVideosSectorViewHolder(clickListener: (Video, View) -> Unit, itemView: View) : AbstractViewHolder<PagedList<Video>>(itemView) {

    val cardView = itemView.albums_card_view

    private val adapter = VideosRecyclerAdapter(clickListener, R.layout.album_item).apply {
        itemView.albums_recycler.adapter = this
        itemView.albums_recycler.itemAnimator = DefaultItemAnimator()
        itemView.albums_recycler.addItemDecoration(MarginItemDecorator(0, itemView.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)))
    }

    init {
        itemView.sector_name.text = itemView.resources.getText(R.string.favourites)

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(itemView.albums_recycler)
    }

    override fun bind(item: PagedList<Video>) = adapter.submitList(item)
}