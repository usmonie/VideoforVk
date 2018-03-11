package akhmedoff.usman.videoforvk.looking

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.CatalogViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup

class LookingRecyclerAdapter(
    private val clickListener: (CatalogItem) -> Unit
) :
    PagedListAdapter<Catalog, CatalogViewHolder>(CATALOG_COMPARATOR) {

    companion object {
        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<Catalog>() {
            override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog) =
                oldItem.items == newItem.items
        }
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val catalog = getItem(position)

        catalog?.items?.let {
            if (it.isNotEmpty()) {
                holder.bind(catalog)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatalogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.catalog_item,
                parent,
                false
            ), clickListener
        )

}