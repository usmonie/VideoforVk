package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.view.AbstractRecyclerAdapter
import akhmedoff.usman.videoforvk.view.VideoViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup

class CatalogRecyclerAdapter(private val videoClickListener: OnClickListener<VideoCatalog>) :
    AbstractRecyclerAdapter<VideoCatalog>() {
    override fun createHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val holder = VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.video_item,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener { videoClickListener.onClick(items[holder.adapterPosition]) }
        return holder
    }
}