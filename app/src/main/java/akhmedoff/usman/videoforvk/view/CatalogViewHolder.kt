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
        CatalogRecyclerAdapter(videoClickListener)
    }

    init {
        val catalogRecycler = itemView.findViewById<RecyclerView>(R.id.catalog_recycler)
        val recyclerViewPool = RecyclerView.RecycledViewPool()
        catalogRecycler.recycledViewPool = recyclerViewPool

        catalogRecycler.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(itemView.context, HORIZONTAL, false)

        linearLayoutManager.isItemPrefetchEnabled = true
        catalogRecycler.layoutManager = linearLayoutManager

        catalogRecycler.setItemViewCacheSize(20)
        catalogRecycler.isDrawingCacheEnabled = true
        catalogRecycler.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(catalogRecycler)
        catalogRecycler.adapter = adapter
    }

    private val catalogTitle: TextView by lazy {
        itemView.findViewById<TextView>(R.id.catalog_title)
    }

    override fun bind(item: Catalog) {
        catalogTitle.text = item.name
        adapter.replace(item.items.toMutableList())
    }
}