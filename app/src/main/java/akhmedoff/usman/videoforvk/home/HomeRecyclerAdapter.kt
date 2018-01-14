package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Item
import akhmedoff.usman.videoforvk.view.AbstractViewHolder
import akhmedoff.usman.videoforvk.view.VideoViewHolder
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.exoplayer2.SimpleExoPlayer

class HomeRecyclerAdapter : RecyclerView.Adapter<AbstractViewHolder>() {

    private val items = mutableListOf<Item>()
    lateinit var player: SimpleExoPlayer

    companion object {
        const val TYPE_CATALOG = 0
        const val TYPE_VIDEO = 1
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_CATALOG
        else -> TYPE_VIDEO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val viewHolder = when (viewType) {
            TYPE_CATALOG -> VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false))
            else -> VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false))
        }

        if (viewHolder is VideoViewHolder) {
            viewHolder.player = player
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(update: List<Item>) {
        items.addAll(update)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: AbstractViewHolder) {
        holder.unBind()

        super.onViewRecycled(holder)
    }

    override fun getItemId(position: Int) = items[position].hashCode().toLong()
}