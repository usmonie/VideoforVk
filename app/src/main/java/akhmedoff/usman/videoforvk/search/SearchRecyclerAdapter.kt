package akhmedoff.usman.videoforvk.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.view.holders.SearchViewHolder
import android.arch.paging.PagedListAdapter
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class SearchRecyclerAdapter(
    private val clickListener: (Video, View) -> Unit,
    @LayoutRes private val layoutId: Int
) : PagedListAdapter<Video, SearchViewHolder>(VIDEO_COMPARATOR) {

    companion object {
        val VIDEO_COMPARATOR = object : DiffUtil.ItemCallback<Video>() {
            override fun areContentsTheSame(oldItem: Video, newItem: Video) =
                oldItem.description == newItem.description

            override fun areItemsTheSame(oldItem: Video, newItem: Video) =
                oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder(
            Picasso.get(), LayoutInflater.from(parent.context).inflate(
                layoutId, parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                clickListener(
                    getItem(adapterPosition)!!,
                    videoFrame
                )
            }
        }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.videoFrame.transitionName = "transition_name_$position"

    }
}
