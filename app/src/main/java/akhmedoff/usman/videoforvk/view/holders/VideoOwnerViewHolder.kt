package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.videoforvk.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class VideoOwnerViewHolder(itemView: View) : AbstractViewHolder<Owner>(itemView) {
    private val title = itemView.findViewById<TextView>(R.id.owner_name)!!
    private val avatar = itemView.findViewById<ImageView>(R.id.owner_avatar)!!

    override fun bind(item: Owner) {
        title.text = item.name

        Picasso
            .with(itemView.context)
            .load(item.photo100)
            .fit()
            .into(avatar)
    }
}