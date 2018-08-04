package akhmedoff.usman.thevt.ui.explore

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType.ALBUM
import akhmedoff.usman.data.model.CatalogItemType.VIDEO
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.view.holders.VideoViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CatalogItemsRecyclerAdapter(
        private val picasso: Picasso,
        private val clickListener: (CatalogItem, View) -> Unit
) : RecyclerView.Adapter<VideoViewHolder>() {

    var items: List<CatalogItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val holder = VideoViewHolder(
                picasso,
                LayoutInflater.from(parent.context).inflate(
                        viewType,
                        parent,
                        false
                )
        )

        items?.let { items ->
            holder.itemView.setOnClickListener {
                holder.poster.transitionName = "transition_name_${holder.adapterPosition}"
                clickListener(items[holder.adapterPosition], holder.poster)
            }
        }

        return holder
    }

    override fun getItemId(position: Int) = position.toLong() + 1

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items?.get(position)!!)

        if (items?.get(position)?.type == VIDEO && position == 0) {
            holder.itemView.layoutParams.width =
                    holder.itemView.resources.getDimensionPixelSize(R.dimen.width_main_list_first_video)
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        items?.get(position)?.type == VIDEO -> R.layout.catalog_video_item_big

        items?.get(position)?.type == ALBUM -> R.layout.catalog_album_item

        else -> throw Exception("Unchecked type")
    }
}