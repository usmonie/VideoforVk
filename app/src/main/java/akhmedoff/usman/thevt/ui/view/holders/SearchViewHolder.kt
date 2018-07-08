package akhmedoff.usman.thevt.ui.view.holders

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.thevt.R
import android.graphics.Bitmap
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class SearchViewHolder(private val picasso: Picasso, itemView: View) :
        AbstractViewHolder<Video>(itemView) {

    val videoFrame = itemView.findViewById<ImageView>(R.id.poster)
    private val videoTitle = itemView.findViewById<TextView>(R.id.title)
    private val videoDuration = itemView.findViewById<TextView>(R.id.video_duration)
    private val videoSource = itemView.findViewById<TextView>(R.id.video_source)
    private val videoViews = itemView.findViewById<TextView>(R.id.video_views)
    private val videoDate = itemView.findViewById<TextView>(R.id.video_date)

    override fun bind(item: Video) {
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

        item.duration?.toLong()?.let {
            val duration = Date(it * 1000L)

            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.UK)
            dateFormat.timeZone = TimeZone.getTimeZone("GMT0:00")
            videoDuration.text = dateFormat.format(duration)

            Log.d("dateFormat", duration.toString() + videoDuration.text)
        }

        if (item.player.contentEquals("youtube")) {
            videoSource.visibility = View.VISIBLE
            videoSource.text = "Youtube"
        }

        videoViews.text = item.views?.let {
            itemView.resources.getQuantityString(
                    R.plurals.video_views,
                    it,
                    it.toString()
            )
        }

        videoDate.text = DateUtils.getRelativeTimeSpanString(
                item.date * 1000L,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS
        )
    }
}