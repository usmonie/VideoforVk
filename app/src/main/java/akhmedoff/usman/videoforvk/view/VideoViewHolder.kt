package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Item
import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    private val simpleExoPlayerView: SimpleExoPlayerView by lazy { itemView.findViewById<SimpleExoPlayerView>(R.id.video_player_small) }
    lateinit var player: SimpleExoPlayer

    override fun bind(item: Item) {
        simpleExoPlayerView.requestFocus()

        simpleExoPlayerView.player = player

        val mp4VideoUri = when {
            item.files?.external != null -> Uri.parse(item.files?.external!!)
            item.files?.mp4480 != null -> Uri.parse(item.files?.mp4480!!)
            item.files?.mp4360 != null -> Uri.parse(item.files?.mp4360!!)
            else -> Uri.parse(item.files?.mp4240!!)
        }

        val bandwidthMeterA = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(itemView.context, Util.getUserAgent(itemView.context, "vk"), bandwidthMeterA)

        val extractorsFactory = DefaultExtractorsFactory()

        val videoSource = ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null)
        val loopingSource = LoopingMediaSource(videoSource)

        player.prepare(loopingSource)

        player.playWhenReady = true
    }

    override fun unBind() {
        player.release()
    }
}