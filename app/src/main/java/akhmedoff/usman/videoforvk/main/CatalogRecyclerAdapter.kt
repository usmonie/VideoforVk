package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.CatalogItemType
import akhmedoff.usman.videoforvk.view.holders.VideoViewHolder
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import com.squareup.picasso.Picasso

class CatalogRecyclerAdapter(
    private val picasso: Picasso,
    private val clickListener: (CatalogItem) -> Unit,
    private val gestureListener: (CatalogItem, MotionEvent?) -> Unit
) : RecyclerView.Adapter<VideoViewHolder>() {

    var items: List<CatalogItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoViewHolder {
        val holder = VideoViewHolder(
            picasso,
            LayoutInflater.from(parent?.context).inflate(
                viewType,
                parent,
                false
            )
        )


        items?.let { items ->
            holder.itemView.setOnClickListener {
                clickListener(items[holder.adapterPosition])
            }

            val gestureDetectorCompat = GestureDetectorCompat(parent?.context, object :
                GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent?) {


                    gestureListener(items[holder.adapterPosition], e)
                    super.onLongPress(e)
                }
            })

            gestureDetectorCompat.setIsLongpressEnabled(true)

            holder.itemView.setOnTouchListener { _: View, motionEvent: MotionEvent ->
                return@setOnTouchListener gestureDetectorCompat.onTouchEvent(motionEvent)
            }
        }

        return holder
    }

    override fun getItemId(position: Int) = position.toLong() + 1

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: VideoViewHolder?, position: Int) {
        items?.get(position)?.let { holder?.bind(it) }
    }

    override fun getItemViewType(position: Int): Int {
        items?.get(position)?.type?.let {
            return when {
                it == CatalogItemType.VIDEO && position == 0 -> R.layout.catalog_video_item_big

                it == CatalogItemType.VIDEO && position > 0 -> R.layout.catalog_video_item_min

                it == CatalogItemType.ALBUM -> R.layout.catalog_album_item

                else -> throw Exception("Unchecked type")
            }
        }

        return super.getItemViewType(position)
    }
}