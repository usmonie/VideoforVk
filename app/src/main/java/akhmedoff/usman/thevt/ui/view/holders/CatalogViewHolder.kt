package akhmedoff.usman.thevt.ui.view.holders

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.explore.CatalogItemsRecyclerAdapter
import akhmedoff.usman.thevt.ui.view.GravitySnapHelper
import akhmedoff.usman.thevt.ui.view.MarginItemDecorator
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.catalog_item.view.*

class CatalogViewHolder(
        itemView: View,
        clickListener: (CatalogItem, View) -> Unit
) : AbstractViewHolder<Catalog>(itemView) {

    private val adapter = CatalogItemsRecyclerAdapter(
            Picasso.get(),
            clickListener
    )

    private val catalogTitle = itemView.catalog_title

    private val linearLayoutManager =
            LinearLayoutManager(itemView.context, HORIZONTAL, false)
    private val catalogRecycler = itemView.findViewById<RecyclerView>(R.id.catalog_recycler)

    init {
        catalogRecycler.setHasFixedSize(true)
        catalogRecycler.adapter = adapter
        catalogRecycler.addItemDecoration(
                MarginItemDecorator(
                        2,
                        itemView.context.resources.getDimensionPixelSize(R.dimen.catalog_videos_margin)
                )
        )
        catalogRecycler.layoutManager = linearLayoutManager

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(catalogRecycler)
    }


    override fun bind(item: Catalog) {
        catalogTitle.text = item.name

        adapter.items = item.items
        adapter.notifyDataSetChanged()
    }
}
