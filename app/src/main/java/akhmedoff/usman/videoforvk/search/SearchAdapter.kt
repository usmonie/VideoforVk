package akhmedoff.usman.videoforvk.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.VideoViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso


class SearchAdapter(
    private val clickListener: (Video) -> Unit
) :
    PagedListAdapter<Video, VideoViewHolder>(VIDEO_COMPARATOR) {

    companion object {
        val VIDEO_COMPARATOR = object : DiffCallback<Video>() {
            override fun areContentsTheSame(oldItem: Video, newItem: Video) =
                oldItem.description == newItem.description

            override fun areItemsTheSame(oldItem: Video, newItem: Video) =
                oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoViewHolder {
        val holder = VideoViewHolder(
            Picasso.with(parent?.context), LayoutInflater.from(parent?.context).inflate(
                R.layout.recommendation_videos, parent, false
            )
        )

        holder.itemView.setOnClickListener { clickListener(getItem(holder.adapterPosition)!!) }

        return holder
    }

    override fun onBindViewHolder(holder: VideoViewHolder?, position: Int) {
        holder?.bind(getItem(position)!!)
    }
}