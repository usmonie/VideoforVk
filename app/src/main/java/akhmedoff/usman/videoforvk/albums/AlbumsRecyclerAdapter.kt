package akhmedoff.usman.videoforvk.albums

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.videoforvk.view.holders.VideoViewHolder
import android.arch.paging.PagedListAdapter
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class AlbumsRecyclerAdapter(
    private val clickListener: (Album) -> Unit,
    @LayoutRes private val layoutId: Int
) : PagedListAdapter<Album, VideoViewHolder>(VIDEO_COMPARATOR) {

    companion object {
        val VIDEO_COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
            override fun areContentsTheSame(oldItem: Album, newItem: Album) =
                oldItem.description == newItem.description

            override fun areItemsTheSame(oldItem: Album, newItem: Album) =
                oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoViewHolder(
            Picasso.with(parent.context), LayoutInflater.from(parent.context).inflate(
                layoutId, parent, false
            )
        ).apply { itemView.setOnClickListener { clickListener(getItem(adapterPosition)!!) } }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
        holder.bind(getItem(position)!!)
}