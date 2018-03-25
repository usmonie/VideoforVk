package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.data.model.Item
import akhmedoff.usman.videoforvk.R
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoViewHolder(private val picasso: Picasso, itemView: View) :
    AbstractViewHolder<Item>(itemView) {

    val cardView = itemView.findViewById<CardView>(R.id.video_item_cardView)
    private val videoFrame = itemView.findViewById<ImageView>(R.id.video_poster)
    private val videoTitle = itemView.findViewById<TextView>(R.id.video_title)

    override fun bind(item: Item) {
        val imageUri = when {
            item.photo800 != null -> item.photo800
            item.photo640 != null -> item.photo640
            else -> item.photo320
        }

        picasso
            .load(imageUri)
            .config(Bitmap.Config.RGB_565)
            .centerCrop()
            .fit()
            .into(videoFrame)

        videoTitle.text = item.title
    }
}