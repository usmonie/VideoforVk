package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.view.AbstractRecyclerAdapter
import akhmedoff.usman.videoforvk.view.CatalogViewHolder
import akhmedoff.usman.videoforvk.view.RecommendationViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup

class HomeRecyclerAdapter(
    private val videoClickListener: AbstractRecyclerAdapter.OnClickListener<VideoCatalog>,
    private val allClickListener: OnClickListener<Catalog>
) : AbstractRecyclerAdapter<Catalog>() {

    companion object {
        const val TYPE_RECOMMENDATIONS = 0
        const val TYPE_CATALOG = 1
    }

    override fun createHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_RECOMMENDATIONS -> RecommendationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recomendation_item,
                parent,
                false
            )
        )
        else -> {
            val holder = CatalogViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.catalog_item,
                    parent,
                    false
                ),
                videoClickListener
            )
            holder.seeAllButton.setOnClickListener {
                allClickListener.onClick(items[holder.adapterPosition])
            }

            holder
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_RECOMMENDATIONS
        else -> TYPE_CATALOG
    }
}
