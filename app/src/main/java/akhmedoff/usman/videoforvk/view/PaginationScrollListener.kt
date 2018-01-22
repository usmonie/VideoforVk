package akhmedoff.usman.videoforvk.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class PaginationScrollListener(internal var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    abstract val totalPageCount: Int

    abstract val isLastPage: Boolean

    abstract val isLoading: Boolean

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        when {
            !isLoading && !isLastPage -> if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
}