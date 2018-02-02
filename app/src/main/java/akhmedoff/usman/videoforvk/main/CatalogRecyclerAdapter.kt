package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.view.AbstractRecyclerAdapter
import akhmedoff.usman.videoforvk.view.VideoViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup

class CatalogRecyclerAdapter(private val videoClickListener: OnClickListener<VideoCatalog>) :
    AbstractRecyclerAdapter<VideoCatalog>() {

    companion object {
        const val BIG_ITEM = 0
        const val SMALL_ITEM = 1
    }

    /**
     * Return the view type of the item at `position` for the purposes
     * of view recycling.
     *
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `position`. Type codes need not be contiguous.
     */
    override fun getItemViewType(position: Int) = when (position) {
        0 -> BIG_ITEM
        else -> SMALL_ITEM
    }

    override fun areItemsTheSame(oldItem: VideoCatalog, newItem: VideoCatalog): Boolean =
        oldItem.accessKey == newItem.accessKey

    override fun areContentsTheSame(oldItem: VideoCatalog, newItem: VideoCatalog): Boolean =
        oldItem.description == newItem.description

    override fun createHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val layoutId = when (viewType) {
            BIG_ITEM -> R.layout.catalog_video_item_big
            else -> R.layout.catalog_video_item_min
        }

        val holder = VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener { videoClickListener.onClick(items!![holder.adapterPosition]) }
        return holder
    }
}