package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.R
import android.text.format.DateUtils
import android.view.View
import kotlinx.android.synthetic.main.video_info_item.view.*

class VideoInfoViewHolder(val clickListener: (Int) -> Unit, itemView: View) :
    AbstractViewHolder<Video>(itemView) {

    init {
        itemView.like_button.setOnClickListener { clickListener(it.id) }
        itemView.share_button.setOnClickListener { clickListener(it.id) }
        itemView.send_button.setOnClickListener { clickListener(it.id) }
        itemView.add_button.setOnClickListener { clickListener(it.id) }
    }

    override fun bind(item: Video) {
        itemView.video_title.text = item.title

        itemView.video_date.text = DateUtils.getRelativeTimeSpanString(
            item.date,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS
        )

        itemView.video_views.text =
                item.views?.let {
                    itemView.resources.getQuantityString(
                        R.plurals.video_views,
                        it,
                        it.toString()
                    )
                }

        itemView.video_desc.text = item.description
    }
}