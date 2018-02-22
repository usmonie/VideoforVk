package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.CatalogItem
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoViewHolder(private val picasso: Picasso, itemView: View) :
    AbstractViewHolder<CatalogItem>(itemView) {

    val cardView = itemView.findViewById<CardView>(R.id.catalog_item_cardView)

    private val videoFrame = itemView.findViewById<ImageView>(R.id.video_frame)
    private val videoTitle = itemView.findViewById<TextView>(R.id.video_title)

    override fun bind(item: CatalogItem) {

        val imageUri = when {
            item.photo800 != null -> item.photo800
            item.photo640 != null -> item.photo640
            else -> item.photo320
        }
        picasso
            .load(imageUri)
            .fit()
            .config(Bitmap.Config.RGB_565)
            .into(videoFrame)

        videoTitle.text = item.title
    }
}