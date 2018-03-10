package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.VideoViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class AlbumRecyclerAdapter(
    private val clickListener: (Video) -> Unit
) : PagedListAdapter<Video, VideoViewHolder>(VIDEO_COMPARATOR) {

    companion object {
        val VIDEO_COMPARATOR = object : DiffUtil.ItemCallback<Video>() {
            override fun areContentsTheSame(oldItem: Video, newItem: Video) =
                oldItem.title == newItem.title && oldItem.date == newItem.addingDate

            override fun areItemsTheSame(oldItem: Video, newItem: Video) =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val holder = VideoViewHolder(
            Picasso.with(parent.context),
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_videos,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            clickListener(getItem(holder.layoutPosition)!!)
        }

        return holder
    }

    override fun getItemId(position: Int) = position.toLong() + 1

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}