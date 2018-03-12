package akhmedoff.usman.videoforvk.view.holders

import akhmedoff.usman.data.model.Owner
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.video_owner_item.view.*

class VideoOwnerViewHolder(itemView: View) : AbstractViewHolder<Owner>(itemView) {

    override fun bind(item: Owner) {
        itemView.owner_name.text = item.name

        Picasso
            .with(itemView.context)
            .load(item.photo100)
            .fit()
            .into(itemView.owner_avatar)
    }
}