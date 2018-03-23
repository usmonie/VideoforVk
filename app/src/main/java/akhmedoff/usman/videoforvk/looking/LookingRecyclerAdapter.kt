package akhmedoff.usman.videoforvk.looking

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.CatalogViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LookingRecyclerAdapter(
    private val clickListener: (CatalogItem, View) -> Unit
) : PagedListAdapter<Catalog, CatalogViewHolder>(CATALOG_COMPARATOR) {

    companion object {
        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<Catalog>() {
            override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
                Log.d("ARE_ITEMS_THE_SAME", oldItem.name + newItem.name)
                return false
            }

            override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
                Log.d("ARE_ITEMS_THE_SAME", oldItem.toString() + newItem.toString())

                return false
            }
        }
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        if (getItem(position)?.items!!.isNotEmpty()) {
            holder.bind(getItem(position)!!)
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