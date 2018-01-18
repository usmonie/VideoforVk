package akhmedoff.usman.videoforvk.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class AbstractRecyclerAdapter<T> : RecyclerView.Adapter<AbstractViewHolder<T>>() {
    protected val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        createHolder(parent, viewType)

    abstract fun createHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<T>

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(update: List<T>) {
        items.addAll(update)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int) = items[position]!!.hashCode().toLong()

    interface OnClickListener<in T> {
        fun onClick(item: T)
    }
}