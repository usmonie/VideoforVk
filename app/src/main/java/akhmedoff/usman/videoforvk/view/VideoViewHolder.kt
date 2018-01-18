package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.VideoCatalog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class VideoViewHolder(itemView: View) : AbstractViewHolder<VideoCatalog>(itemView) {
    private val videoFrame: ImageView by lazy { itemView.findViewById<ImageView>(R.id.video_frame) }
    private val videoTitle: TextView by lazy { itemView.findViewById<TextView>(R.id.video_title) }
    override fun bind(item: VideoCatalog) {
        val imageUri = when {
            item.photo800 != null -> item.photo800
            item.photo640 != null -> item.photo640
            else -> item.photo320
        }
        Glide
            .with(videoFrame.context)
            .load(imageUri?.replace("\"", ""))
            .apply(RequestOptions().centerCrop())
            .into(videoFrame)
        videoTitle.text = item.title
    }
}