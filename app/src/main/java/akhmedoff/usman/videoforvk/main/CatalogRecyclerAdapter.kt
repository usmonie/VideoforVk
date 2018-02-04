package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.view.OnClickListener
import akhmedoff.usman.videoforvk.view.VideoViewHolder
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class CatalogRecyclerAdapter(
    private val picasso: Picasso,
    private val videoClickListener: OnClickListener<VideoCatalog>
) : RecyclerView.Adapter<VideoViewHolder>() {
    companion object {
        const val BIG_ITEM = 0
        const val SMALL_ITEM = 1
        const val ALBUM_ITEM = 2
    }

    var items: List<VideoCatalog>? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoViewHolder {
        val layoutId = when (viewType) {
            BIG_ITEM -> R.layout.catalog_video_item_big
            SMALL_ITEM -> R.layout.catalog_video_item_min
            else -> R.layout.catalog_album_item
        }

        val holder = VideoViewHolder(
            picasso,
            LayoutInflater.from(parent?.context).inflate(
                layoutId,
                parent,
                false
            )
        )

        items?.let { items ->
            holder.itemView.setOnClickListener { videoClickListener.onClick(items[holder.layoutPosition]) }
        }
        return holder
    }

    /**
     * Return the stable ID for the item at `position`. If [.hasStableIds]
     * would return false this method should return [.NO_ID]. The default implementation
     * of this method returns [.NO_ID].
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    override fun getItemId(position: Int): Long {
        return position.toLong() + 1
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: VideoViewHolder?, position: Int) {
        items?.get(position)?.let { holder?.bind(it) }
    }

    /**
     * Return the view type of the item at `position` for the purposes
     * of view recycling.
     *
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `position`. Type codes need not be contiguous.
     */
    override fun getItemViewType(position: Int): Int {
        items?.get(position)?.type?.let {
            return when {
                it == "video" && position == 0 -> BIG_ITEM
                it == "video" && position > 0 -> SMALL_ITEM
                it == "album" -> ALBUM_ITEM
                else -> throw Exception("Unchecked type")
            }
        }

        return super.getItemViewType(position)
    }
}