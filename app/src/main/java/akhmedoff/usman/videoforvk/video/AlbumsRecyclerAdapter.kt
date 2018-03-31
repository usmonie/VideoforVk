package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.SelectableAlbumViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup

class AlbumsRecyclerAdapter :
    PagedListAdapter<Album, SelectableAlbumViewHolder>(ALBUM_COMPARATOR) {

    companion object {
        val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
            override fun areContentsTheSame(oldItem: Album, newItem: Album) =
                oldItem.description == newItem.description

            override fun areItemsTheSame(oldItem: Album, newItem: Album) =
                oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    val selectedIds: List<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableAlbumViewHolder {
        return SelectableAlbumViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.add_video_multiselect_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectableAlbumViewHolder, position: Int) {
    }
}