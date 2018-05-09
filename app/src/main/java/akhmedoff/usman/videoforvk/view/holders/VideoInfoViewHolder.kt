package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.R
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.video_info_item.view.*

class VideoInfoViewHolder(
        private val clickListener: (Int) -> Unit,
        itemView: View
) : AbstractViewHolder<Video>(itemView) {

    private val popupMenu: PopupMenu = PopupMenu(itemView.context, itemView.add_button).apply {
        this.inflate(R.menu.add_video_menu)
        this.setOnMenuItemClickListener {
            clickListener(it.itemId)
            true
        }
    }

    init {
        itemView.like_button.setOnClickListener {
            clickListener(it.id)
        }
        itemView.share_button.setOnClickListener {
            clickListener(it.id)
        }
        itemView.send_button.setOnClickListener {
            clickListener(it.id)
        }
        itemView.add_button.setOnClickListener {
            popupMenu.show()
        }
    }

    override fun bind(item: Video) {
        itemView.video_title.text = item.title

        itemView.video_date.text = DateUtils.getRelativeTimeSpanString(
                item.date * 1000,
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

        setDrawable(itemView.like_button,
                when (item.likes?.userLikes) {
                    true -> R.drawable.ic_favorite_fill_24dp
                    else -> R.drawable.ic_favorite_border
                })
    }

    fun setDrawable(imageView: ImageView, @DrawableRes id: Int) =
            imageView.setImageDrawable(ContextCompat.getDrawable(itemView.context, id))
}