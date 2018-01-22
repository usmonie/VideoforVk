package akhmedoff.usman.videoforvk.view

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


abstract class AbstractRecyclerAdapter<T> : RecyclerView.Adapter<AbstractViewHolder<T>>() {
    protected var items: MutableList<T>? = null
    private var dataVersion = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        createHolder(parent, viewType)

    abstract fun createHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<T>

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) {
        items?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount() = items?.size ?: 0

    override fun getItemId(position: Int) = items?.get(position)?.hashCode()?.toLong() ?: 0L

    @SuppressLint("StaticFieldLeak")
    @MainThread
    fun replace(update: MutableList<T>?) {
        dataVersion++
        when {
            items == null -> {
                when (update) {
                    null -> return
                    else -> {
                        items = update
                        notifyDataSetChanged()
                    }
                }
            }
            update == null -> {
                val oldSize = items?.size ?: 0
                items = null
                notifyItemRangeRemoved(0, oldSize)
            }
            else -> {
                val startVersion = dataVersion
                val oldItems = items
                object : AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                    override fun doInBackground(vararg voids: Void): DiffUtil.DiffResult {
                        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                            override fun getOldListSize() = oldItems?.size ?: 0

                            override fun getNewListSize() = update.size

                            override fun areItemsTheSame(
                                oldItemPosition: Int,
                                newItemPosition: Int
                            ): Boolean {
                                val oldItem = oldItems!![oldItemPosition]
                                val newItem = update[newItemPosition]
                                return this@AbstractRecyclerAdapter.areItemsTheSame(
                                    oldItem,
                                    newItem
                                )
                            }

                            override fun areContentsTheSame(
                                oldItemPosition: Int,
                                newItemPosition: Int
                            ): Boolean {
                                val oldItem = oldItems!![oldItemPosition]
                                val newItem = update[newItemPosition]
                                return this@AbstractRecyclerAdapter.areContentsTheSame(
                                    oldItem,
                                    newItem
                                )
                            }
                        })
                    }

                    override fun onPostExecute(diffResult: DiffUtil.DiffResult) {
                        if (startVersion != dataVersion) {
                            // ignore update
                            return
                        }
                        items = update
                        diffResult.dispatchUpdatesTo(this@AbstractRecyclerAdapter)
                    }
                }.execute()
            }
        }
    }

    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    protected abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    interface OnClickListener<in T> {
        fun onClick(item: T)
    }
}