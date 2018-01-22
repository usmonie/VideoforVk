package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.home.CatalogRecyclerAdapter
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView

class CatalogViewHolder(
    itemView: View,
    videoClickListener: AbstractRecyclerAdapter.OnClickListener<VideoCatalog>
) : AbstractViewHolder<Catalog>(itemView) {
    private val adapter: AbstractRecyclerAdapter<VideoCatalog> by lazy {
        CatalogRecyclerAdapter(
            videoClickListener
        )
    }

    private val catalogRecyclerView: RecyclerView by lazy {
        val catalogRecycler = itemView.findViewById<RecyclerView>(R.id.catalog_recycler)

        catalogRecycler.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(itemView.context, HORIZONTAL, false)

        linearLayoutManager.isItemPrefetchEnabled = true
        catalogRecycler.layoutManager = linearLayoutManager

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(catalogRecycler)
        catalogRecycler
    }

    val seeAllButton: TextView by lazy {
        itemView.findViewById<TextView>(R.id.see_all_button)
    }

    private val catalogTitle: TextView by lazy {
        itemView.findViewById<TextView>(R.id.catalog_title)
    }

    override fun bind(item: Catalog) {
        catalogRecyclerView.adapter = adapter

        catalogTitle.text = item.name
        adapter.replace(item.items.toMutableList())
    }
}