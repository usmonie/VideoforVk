package akhmedoff.usman.videoforvk.catalog

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.VideoViewHolder
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
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem) =
                oldItem.accessKey == newItem.accessKey
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoViewHolder(
            Picasso.get(),
            LayoutInflater.from(parent.context).inflate(
                R.layout.catalog_video_item_big,
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    clickListener(it, videoFrame)
                }
            }
        }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }

        holder.videoFrame.transitionName = "transition_name_$position"
    }

    override fun getItemId(position: Int) = position.toLong()
}