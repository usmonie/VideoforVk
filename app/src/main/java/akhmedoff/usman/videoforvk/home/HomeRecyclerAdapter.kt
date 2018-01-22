package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.view.AbstractRecyclerAdapter
import akhmedoff.usman.videoforvk.view.CatalogViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup

class HomeRecyclerAdapter(
    private val videoClickListener: AbstractRecyclerAdapter.OnClickListener<VideoCatalog>,
    private val allClickListener: OnClickListener<Catalog>
) : AbstractRecyclerAdapter<Catalog>() {
    override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog): Boolean =
        oldItem.name == newItem.name

    override fun createHolder(parent: ViewGroup, viewType: Int) = run {
        val holder = CatalogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.catalog_item,
                parent,
                false
            ),
            videoClickListener
        )
        holder.seeAllButton.setOnClickListener {
            allClickListener.onClick(items!![holder.adapterPosition])
        }

        holder
    }
}
