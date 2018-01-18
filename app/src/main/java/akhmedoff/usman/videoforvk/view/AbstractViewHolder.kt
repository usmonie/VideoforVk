package akhmedoff.usman.videoforvk.view

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}