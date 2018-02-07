package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.CatalogItem
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoViewHolder(private val picasso: Picasso, itemView: View) :
    AbstractViewHolder<CatalogItem>(itemView) {
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
            .config(Bitmap.Config.RGB_565)
            .into(videoFrame)

        videoTitle.text = item.title
    }
}