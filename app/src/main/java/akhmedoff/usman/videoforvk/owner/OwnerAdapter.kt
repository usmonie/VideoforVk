package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Item
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.view.holders.AbstractViewHolder
import akhmedoff.usman.videoforvk.view.holders.AlbumsViewHolder
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class OwnerAdapter(
    private val clickListener: (Item) -> Unit
) : PagedListAdapter<Item, AbstractViewHolder<Item>>(CATALOG_COMPARATOR) {
    override fun onBindViewHolder(holder: AbstractViewHolder<Item>, position: Int) {
    }

    companion object {

        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areContentsTheSame(oldItem: Item, newItem: Item) =
                oldItem.photo320 == newItem.photo320

            override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    var albums: PagedList<Album>? = null

    override fun getItemCount(): Int = super.getItemCount() + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AlbumsViewHolder(
            Picasso.with(parent.context),
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_videos,
                parent,
                false
            )
        )

}