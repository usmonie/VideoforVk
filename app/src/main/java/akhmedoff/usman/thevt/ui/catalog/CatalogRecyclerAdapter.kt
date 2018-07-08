package akhmedoff.usman.thevt.ui.catalog

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.view.holders.VideoViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class CatalogRecyclerAdapter(
    private val clickListener: (CatalogItem, View) -> Unit
) :
    PagedListAdapter<CatalogItem, VideoViewHolder>(CATALOG_COMPARATOR) {

    companion object {
        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<CatalogItem>() {
            override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem) =
                    oldItem.id == newItem.id && oldItem.ownerId == newItem.ownerId

            override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem) =
                    oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoViewHolder(
            Picasso.get(),
            LayoutInflater.from(parent.context).inflate(
                    R.layout.catalog_video_item,
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                poster.transitionName = "transition_name_$adapterPosition"
                getItem(adapterPosition)?.let {
                    clickListener(it, poster)
                }
            }

            itemView.isLongClickable = true
            /*itemView.setOnLongClickListener {
                getItem(adapterPosition)?.let {
                    longClickListener(it, poster)
                }
                true
            }*/
        }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemId(position: Int) = position.toLong()
}