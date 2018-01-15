package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Item
import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    private val simpleExoPlayerView: SimpleExoPlayerView by lazy { itemView.findViewById<SimpleExoPlayerView>(R.id.video_player_small) }

    private val player: SimpleExoPlayer

    private lateinit var mp4VideoUri: Uri
    private val dataSourceFactory: DefaultDataSourceFactory

    init {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), trackSelector)
        player.volume = 0f
        simpleExoPlayerView.requestFocus()

        simpleExoPlayerView.player = player
        val bandwidthMeterA = DefaultBandwidthMeter()
        dataSourceFactory = DefaultDataSourceFactory(itemView.context, Util.getUserAgent(itemView.context, "vk"), bandwidthMeterA)
    }

    override fun onAttachedToWindow() {
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri, null, null)
        val loopingSource = LoopingMediaSource(videoSource)

        player.prepare(loopingSource)

        player.playWhenReady = true
    }

    override fun onDetachedFromWindow() {
        player.playWhenReady = false
    }

    override fun bind(item: Item) {

        mp4VideoUri = when {
            item.files?.external != null -> Uri.parse(item.files?.external!!)
            item.files?.mp4480 != null -> Uri.parse(item.files?.mp4480!!)
            item.files?.mp4360 != null -> Uri.parse(item.files?.mp4360!!)
            else -> Uri.parse(item.files?.mp4240!!)
        }
    }

    override fun unBind() {
        player.playWhenReady = false
        player.release()

    }
}