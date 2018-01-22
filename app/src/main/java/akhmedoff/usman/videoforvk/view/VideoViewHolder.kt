package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoViewHolder(itemView: View) : AbstractViewHolder<VideoCatalog>(itemView) {
    private val videoFrame: ImageView by lazy { itemView.findViewById<ImageView>(R.id.video_frame) }
    private val videoTitle: TextView by lazy { itemView.findViewById<TextView>(R.id.owner_name) }
    override fun bind(item: VideoCatalog) {
        videoFrame.setImageResource(0)
        videoFrame
        val imageUri = when {
            item.photo800 != null -> item.photo800?.replace("\"", "")
            item.photo640 != null -> item.photo640?.replace("\"", "")
            else -> item.photo320.replace("\"", "")
        }
        Picasso
            .with(videoFrame.context)
            .load(imageUri)
            .into(videoFrame)
        videoTitle.text = item.title
    }
}