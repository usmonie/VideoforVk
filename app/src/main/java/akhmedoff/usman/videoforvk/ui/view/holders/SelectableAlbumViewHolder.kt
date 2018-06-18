package akhmedoff.usman.videoforvk.ui.view.holders

import akhmedoff.usman.data.model.Album
import android.view.View
import kotlinx.android.synthetic.main.add_video_multiselect_item.view.*

class SelectableAlbumViewHolder(itemView: View) :
    AbstractViewHolder<Album>(itemView) {

    override fun bind(item: Album) {
        itemView.checkBox.text = item.title
    }

}