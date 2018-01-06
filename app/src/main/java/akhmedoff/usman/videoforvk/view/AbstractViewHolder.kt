package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.model.Item
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: Item)
}