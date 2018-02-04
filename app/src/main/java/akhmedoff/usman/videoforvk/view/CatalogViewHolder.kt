package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.main.CatalogRecyclerAdapter
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.squareup.picasso.Picasso

class CatalogViewHolder(
    itemView: View,
    videoClickListener: OnClickListener<VideoCatalog>
) : AbstractViewHolder<Catalog>(itemView) {
    private val adapter = CatalogRecyclerAdapter(Picasso.with(itemView.context), videoClickListener)

    private val catalogTitle = itemView.findViewById<TextView>(R.id.catalog_title)

    init {
        val catalogRecycler = itemView.findViewById<RecyclerView>(R.id.catalog_recycler)
        catalogRecycler.setHasFixedSize(true)
        catalogRecycler.adapter = adapter

        val layoutManager = GridLayoutManager(itemView.context, 2, HORIZONTAL, false)

        layoutManager.spanSizeLookup = getSpanSizeLookup()
        layoutManager.isItemPrefetchEnabled = true
        catalogRecycler.layoutManager = layoutManager

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
    }
}