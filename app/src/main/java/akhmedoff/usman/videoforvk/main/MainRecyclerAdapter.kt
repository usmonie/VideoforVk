package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.view.CatalogViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup

class MainRecyclerAdapter(private val clickListener: (CatalogItem) -> Unit) :
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
            clickListener
        )

    /**
     * Return the stable ID for the item at `position`. If [.hasStableIds]
     * would return false this method should return [.NO_ID]. The default implementation
     * of this method returns [.NO_ID].
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    override fun getItemId(position: Int) = position.toLong() + 1

    override fun onBindViewHolder(holder: CatalogViewHolder?, position: Int) {
        getItem(position)?.let { holder?.bind(it) }
    }
}
