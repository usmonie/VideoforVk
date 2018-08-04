package akhmedoff.usman.thevt.ui.profile

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.view.holders.FaveVideosSectorViewHolder
import akhmedoff.usman.thevt.ui.view.holders.ProfileAlbumsSectorViewHolder
import akhmedoff.usman.thevt.ui.view.holders.SearchViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProfileRecyclerAdapter(private val videoClickListener: (Video, View) -> Unit,
                             private val albumClickListener: (Album, View) -> Unit,
                             private val albumsClickListener: (View) -> Unit,
                             private val favouritesClickList: () -> Unit) : PagedListAdapter<Video, RecyclerView.ViewHolder>(CATALOG_COMPARATOR) {

    var albums: PagedList<Album>? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    var faveVideos: PagedList<Video>? = null
        set(value) {
            field = value
            notifyItemChanged(1)
        }

    companion object {
        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.id == newItem.id && oldItem.ownerId == newItem.ownerId

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.title == newItem.title && oldItem.photo130 == newItem.photo130
        }
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if ((viewType == 0 && albums?.isNotEmpty() == true)) {
                ProfileAlbumsSectorViewHolder(albumClickListener, LayoutInflater.from(parent.context).inflate(R.layout.albums_item, parent, false)).apply {
                    cardView.setOnClickListener {
                        cardView.transitionName = "transition_name_$adapterPosition"
                        albumsClickListener(cardView)
                    }
                }
            } else if (viewType == 1 && faveVideos?.isNotEmpty() == true) {
                FaveVideosSectorViewHolder(videoClickListener, LayoutInflater.from(parent.context).inflate(R.layout.albums_item, parent, false)).apply {
                    cardView.setOnClickListener {
                        favouritesClickList()
                    }
                }
            } else {
                SearchViewHolder(Picasso.get(), LayoutInflater.from(parent.context).inflate(R.layout.search_videos, parent, false)).apply {
                    videoFrame.setOnClickListener {
                        getItem(if (albums != null && faveVideos != null) adapterPosition - 2 else if (albums != null || faveVideos != null) adapterPosition - 1 else adapterPosition)?.let {
                            videoClickListener(it, videoFrame.apply { transitionName = "transition_name_$adapterPosition" })
                        }
                    }
                }
            }

    override fun getItemCount(): Int = when {
        super.getItemCount() == 0 && albums?.isNotEmpty() == true -> 1
        albums?.isNotEmpty() == true -> super.getItemCount() + 1
        else -> super.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProfileAlbumsSectorViewHolder && position == 0 && albums?.isNotEmpty() == true) {
            holder.bind(albums!!)
        } else if (holder is FaveVideosSectorViewHolder && position == 1 && faveVideos?.isNotEmpty() == true) {
            holder.bind(faveVideos!!)
        } else if (holder is SearchViewHolder) {
            val pos = if (albums?.isNotEmpty() == true && faveVideos?.isNotEmpty() == true) position - 2 else if (albums?.isNotEmpty() == true || faveVideos?.isNotEmpty() == true) position - 1 else position
            if (pos < itemCount)
                getItem(pos)?.let { holder.bind(it) }
        }
    }
}
