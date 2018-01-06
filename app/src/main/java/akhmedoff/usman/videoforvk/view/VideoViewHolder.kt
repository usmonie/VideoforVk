package akhmedoff.usman.videoforvk.view

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.model.Item
import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class VideoViewHolder(itemView: View) : AbstractViewHolder(itemView) {

    private val simpleExoPlayerView = itemView.findViewById<SimpleExoPlayerView>(R.id.video_player_small)!!

    override fun bind(item: Item) {
// 1. Create a default TrackSelector
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

// 3. Create the player

        val player = ExoPlayerFactory.newSimpleInstance(itemView.context, trackSelector)

//Set media controller
        simpleExoPlayerView.requestFocus()

// Bind the player to the view.
        simpleExoPlayerView.player = player


// I. ADJUST HERE:
//CHOOSE CONTENT: LiveStream / SdCard

//LIVE STREAM SOURCE: * Livestream links may be out of date so find any m3u8 files online and replace:

        val mp4VideoUri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
        val bandwidthMeterA = DefaultBandwidthMeter()
//Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(itemView.context, Util.getUserAgent(itemView.context, "exoplayer2example"), bandwidthMeterA)
//Produces Extractor instances for parsing the media data.
        val extractorsFactory = DefaultExtractorsFactory()


// II. ADJUST HERE:

//This is the MediaSource representing the media to be played:
//FOR SD CARD SOURCE:
//        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

//FOR LIVESTREAM LINK:
        val videoSource = HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null)
        val loopingSource = LoopingMediaSource(videoSource)

// Prepare the player with the source.
        player.prepare(loopingSource)

        player.playWhenReady = true
    }
}
