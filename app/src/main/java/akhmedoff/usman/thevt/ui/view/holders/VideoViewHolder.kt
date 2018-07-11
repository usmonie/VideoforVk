package akhmedoff.usman.thevt.ui.view.holders

import akhmedoff.usman.data.model.Item
import akhmedoff.usman.thevt.R
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoViewHolder(private val picasso: Picasso, itemView: View) :
        AbstractViewHolder<Item>(itemView) {

    val poster = itemView.findViewById<ImageView>(R.id.poster)
    private val title = itemView.findViewById<TextView>(R.id.title)

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
                .into(poster)

        title.text = item.title

    }
}