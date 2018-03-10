package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.looking.CatalogItemsRecyclerAdapter
import akhmedoff.usman.videoforvk.view.GravitySnapHelper
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.squareup.picasso.Picasso

class CatalogViewHolder(
    itemView: View,
    clickListener: (CatalogItem) -> Unit
) : AbstractViewHolder<Catalog>(itemView) {

    private val adapter = CatalogItemsRecyclerAdapter(
        Picasso.with(itemView.context),
        clickListener
    )

    private val catalogTitle = itemView.findViewById<TextView>(R.id.catalog_title)

    private val gridLayoutManager = GridLayoutManager(itemView.context, 2, HORIZONTAL, false)

    private val linearLayoutManager = LinearLayoutManager(itemView.context, HORIZONTAL, false)
    private val catalogRecycler = itemView.findViewById<RecyclerView>(R.id.catalog_recycler)

    init {
        catalogRecycler.setHasFixedSize(true)
        catalogRecycler.adapter = adapter
        catalogRecycler.addItemDecoration(
            MarginItemDecorator(
                2,
                itemView.context.resources.getDimensionPixelSize(R.dimen.catalog_videos_margin)

            )
        )

        gridLayoutManager.spanSizeLookup = getSpanSizeLookup()

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(catalogRecycler)
    }

    private fun getSpanSizeLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) = when (position) {
            0 -> 2
            else -> 1
        }
    }

    override fun bind(item: Catalog) {
        catalogTitle.text = item.name

        adapter.items = item.items

        item.items?.get(0)?.type?.let {
            when (it) {
                CatalogItemType.ALBUM -> catalogRecycler.layoutManager = linearLayoutManager

                CatalogItemType.VIDEO -> catalogRecycler.layoutManager = gridLayoutManager
            }
        }
    }
}
