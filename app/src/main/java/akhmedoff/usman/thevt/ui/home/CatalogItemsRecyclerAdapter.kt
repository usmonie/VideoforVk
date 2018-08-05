package akhmedoff.usman.thevt.ui.home

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

    var items: List<CatalogItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val holder = VideoViewHolder(
                picasso,
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_video,
                        parent,
                        false
                )
        )

        holder.itemView.setOnClickListener {
            if (items.isNotEmpty()) {
                holder.poster.transitionName = "transition_name_${holder.adapterPosition}"
                clickListener(items[holder.adapterPosition], holder.poster)
            }
        }

        return holder
    }

    override fun getItemId(position: Int) = position.toLong() + 1

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (items.isNotEmpty()) {

            holder.bind(items[position])

            if (items[position].type == VIDEO && position == 0) {
                holder.itemView.layoutParams.width =
                        holder.itemView.resources.getDimensionPixelSize(R.dimen.width_main_list_first_video)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        items[position].type == VIDEO -> R.layout.catalog_video_item_big

        items[position].type == ALBUM -> R.layout.catalog_album_item

        else -> throw Exception("Unchecked type")
    }
}