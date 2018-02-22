package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.view.holders.CatalogViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup

class MainRecyclerAdapter(
    private val clickListener: (CatalogItem) -> Unit,
    private val longClickListener: (CatalogItem, MotionEvent?) -> Unit
) :
    PagedListAdapter<Catalog, CatalogViewHolder>(CATALOG_COMPARATOR) {

    companion object {
        val CATALOG_COMPARATOR = object : DiffCallback<Catalog>() {
            override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog) =
                oldItem.items == newItem.items

            override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog) =
                oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatalogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.catalog_item,
                parent,
                false
            ),
            clickListener, longClickListener
        )

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val catalog = getItem(position)!!

        catalog.items?.let {
            if (it.isNotEmpty()) holder.bind(catalog)
        }
    }

    override fun getItemId(position: Int) = position.toLong()
}