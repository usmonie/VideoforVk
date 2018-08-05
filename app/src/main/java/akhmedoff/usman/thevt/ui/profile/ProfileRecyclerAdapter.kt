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

private const val ALBUMS_TYPE = 1001
private const val FAVE_VIDEOS_TYPE = 1002
private const val VIDEOS_TYPE = 1003

class ProfileRecyclerAdapter(private val videoClickListener: (Video, View) -> Unit,
                             private val albumClickListener: (Album, View) -> Unit,
                             private val albumsClickListener: (View) -> Unit,
                             private val favouritesClickList: () -> Unit) : PagedListAdapter<Video, RecyclerView.ViewHolder>(CATALOG_COMPARATOR) {

    var albums: PagedList<Album>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var faveVideos: PagedList<Video>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.id == newItem.id && oldItem.ownerId == newItem.ownerId

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.title == newItem.title && oldItem.photo320 == newItem.photo320
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        checkList(albums, position, 0) -> ALBUMS_TYPE
        checkList(faveVideos, position, if (checkList(albums, position - 1, 0)) 1 else 0) -> FAVE_VIDEOS_TYPE
        else -> VIDEOS_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when {
        checkList(albums, viewType, ALBUMS_TYPE) -> ProfileAlbumsSectorViewHolder(albumClickListener, LayoutInflater.from(parent.context).inflate(R.layout.albums_item, parent, false)).apply {
            cardView.setOnClickListener {
                cardView.transitionName = "transition_name_$adapterPosition"
                albumsClickListener(cardView)
            }
        }
        checkList(faveVideos, viewType, FAVE_VIDEOS_TYPE) -> FaveVideosSectorViewHolder(videoClickListener, LayoutInflater.from(parent.context).inflate(R.layout.albums_item, parent, false)).apply {
            cardView.setOnClickListener {
                favouritesClickList()
            }
        }
        else -> SearchViewHolder(Picasso.get(), LayoutInflater.from(parent.context).inflate(R.layout.search_videos, parent, false)).apply {
            videoFrame.setOnClickListener { _ ->
                getItem(adapterPosition - minusPosition(albums, faveVideos))?.let {
                    videoClickListener(it, videoFrame.apply { transitionName = "transition_name_$adapterPosition" })
                }
            }
        }
    }

    override fun getItemCount(): Int = when {
        albums?.isNotEmpty() == true && faveVideos?.isNotEmpty() == true -> 2 + super.getItemCount()
        albums?.isNotEmpty() == true || faveVideos?.isNotEmpty() == true -> 1 + super.getItemCount()

        else -> super.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileAlbumsSectorViewHolder -> holder.bind(albums!!)
            is FaveVideosSectorViewHolder -> holder.bind(faveVideos!!)
            is SearchViewHolder -> {
                val pos = position - minusPosition(albums, faveVideos)
                if (pos in 0..(itemCount - 1))
                    getItem(pos)?.let { holder.bind(it) }
            }
        }
    }
}

private fun <T> checkList(items: List<T>?, currentPosition: Int, itemPosition: Int): Boolean =
        items?.isNotEmpty() == true && currentPosition == itemPosition

private fun <T, E> minusPosition(itemsA: List<T>?, itemsB: List<E>?): Int = when {
    itemsA?.isNotEmpty() == true && itemsB?.isNotEmpty() == true -> 2
    itemsA?.isNotEmpty() == true || itemsB?.isNotEmpty() == true -> 1
    else -> 0
}