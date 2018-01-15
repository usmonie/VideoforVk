package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Item
import akhmedoff.usman.videoforvk.view.AbstractViewHolder
import akhmedoff.usman.videoforvk.view.VideoViewHolder
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class HomeRecyclerAdapter : RecyclerView.Adapter<AbstractViewHolder>() {

    private val items = mutableListOf<Item>()

    companion object {
        const val TYPE_CATALOG = 0
        const val TYPE_VIDEO = 1
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_CATALOG
        else -> TYPE_VIDEO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_CATALOG -> VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false))
        else -> VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false))
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

    override fun onViewAttachedToWindow(holder: AbstractViewHolder?) {
        super.onViewAttachedToWindow(holder)
        holder?.onAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        holder?.onDetachedFromWindow()
    }

    override fun getItemId(position: Int) = items[position].hashCode().toLong()
}