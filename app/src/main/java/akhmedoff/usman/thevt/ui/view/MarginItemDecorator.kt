package akhmedoff.usman.thevt.ui.view

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecorator(@IntRange(from = 0) private val columnsCount: Int = 1, @Px private val margin: Int) :
        RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        outRect.bottom = margin
        outRect.left = margin
        outRect.right = margin

        outRect.top = when {
            columnsCount == 1 && parent.getChildLayoutPosition(view) == 0 -> margin
            columnsCount > 1 -> margin
            else -> 0
        }
    }
}