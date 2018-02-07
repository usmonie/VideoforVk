package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Video
import akhmedoff.usman.videoforvk.view.AlbumVideoViewHolder
import akhmedoff.usman.videoforvk.view.OnClickListener
import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class AlbumRecyclerAdapter(
    private val clickListener: OnClickListener<Video>
) : PagedListAdapter<Video, AlbumVideoViewHolder>(VIDEO_COMPARATOR) {
    companion object {
        val VIDEO_COMPARATOR = object : DiffCallback<Video>() {
            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.title == newItem.title && oldItem.date == newItem.addingDate

            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumVideoViewHolder {
        val holder = AlbumVideoViewHolder(
            Picasso.with(parent.context),
            LayoutInflater.from(parent.context).inflate(
                R.layout.recommendation_videos,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            clickListener.onClick(getItem(holder.layoutPosition)!!)
        }

        return holder
    }

    override fun getItemId(position: Int) = position.toLong() + 1

    override fun onBindViewHolder(holder: AlbumVideoViewHolder?, position: Int) {
        getItem(position)?.let { holder?.bind(it) }
    }
}