package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Video
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class AlbumVideoViewHolder(private val picasso: Picasso, itemView: View) :
    AbstractViewHolder<Video>(itemView) {

    private val poster = itemView.findViewById<ImageView>(R.id.recommendation_video_poster)
    private val title = itemView.findViewById<TextView>(R.id.recommendation_video_title)
    private val views = itemView.findViewById<TextView>(R.id.recommendation_video_views)

    override fun bind(item: Video) {

        val posterUrl = when {
            item.photo800 != null -> item.photo800
            item.photo640 != null -> item.photo640
            item.photo320 != null -> item.photo320
            else -> item.photo130
        }

        picasso
            .load(posterUrl)
            .fit()
            .config(Bitmap.Config.RGB_565)
            .into(poster)

        title.text = item.title

        item.views?.let {
            views.text = itemView.context.resources.getQuantityString(
                R.plurals.video_views,
                it,
                NumberFormat.getIntegerInstance().format(it)
            )
        }

    }
}