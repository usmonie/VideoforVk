package akhmedoff.usman.thevt.ui.view.adapters

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.thevt.ui.view.holders.VideoViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso

class AlbumsRecyclerAdapter(
        private val clickListener: (Album, View) -> Unit,
        @LayoutRes private val layoutId: Int
) : PagedListAdapter<Album, VideoViewHolder>(ALBUM_COMPARATOR) {

    companion object {
        val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
            override fun areContentsTheSame(oldItem: Album, newItem: Album) =
                    oldItem.description == newItem.description

            override fun areItemsTheSame(oldItem: Album, newItem: Album) =
                    oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VideoViewHolder(Picasso.get(), LayoutInflater.from(parent.context).inflate(
                    layoutId, parent, false)
            ).apply {
                poster.transitionName = "transition_name_$adapterPosition"
                itemView.setOnClickListener { getItem(adapterPosition)?.let { album -> clickListener(album, poster) } }
            }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
            holder.bind(getItem(position)!!)
}