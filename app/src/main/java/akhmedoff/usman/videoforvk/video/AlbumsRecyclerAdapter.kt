package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.SelectableAlbumViewHolder
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.add_video_multiselect_item.view.*


class AlbumsRecyclerAdapter(private val changedListener: (Album, Boolean) -> Unit) :
    PagedListAdapter<Album, SelectableAlbumViewHolder>(ALBUM_COMPARATOR) {

    companion object {
        val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
            override fun areContentsTheSame(oldItem: Album, newItem: Album) =
                oldItem.description == newItem.description

            override fun areItemsTheSame(oldItem: Album, newItem: Album) =
                oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SelectableAlbumViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.add_video_multiselect_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SelectableAlbumViewHolder, position: Int) {
        holder.itemView.checkBox.setOnCheckedChangeListener(null)

        if (itemCount > position)
            getItem(position)?.let { holder.bind(it) }

        holder.itemView.checkBox.setOnCheckedChangeListener({ _, isChecked ->
            getItem(position)?.let { changedListener(it, isChecked) }
        })
    }

    override fun onBindViewHolder(
        holder: SelectableAlbumViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = payloads[0]

            if (item is List<*>) {
                if (item.isNotEmpty()) {
                    if (item[0] is Int)
                        item.forEach {
                            holder.itemView.checkBox.isChecked = getItem(position)?.id == it
                        }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}