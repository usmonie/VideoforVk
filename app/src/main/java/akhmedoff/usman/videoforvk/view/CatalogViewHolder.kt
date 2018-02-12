package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.main.CatalogRecyclerAdapter
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.CatalogItemType
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
    clickListener: OnClickListener<CatalogItem>
) : AbstractViewHolder<Catalog>(itemView) {

    private val adapter = CatalogRecyclerAdapter(
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
        adapter.notifyDataSetChanged()

        item.items[0].type?.let {
            when (it) {
                CatalogItemType.ALBUM ->
                    catalogRecycler.layoutManager = linearLayoutManager

                CatalogItemType.VIDEO -> catalogRecycler.layoutManager = gridLayoutManager
            }
        }

    }
}