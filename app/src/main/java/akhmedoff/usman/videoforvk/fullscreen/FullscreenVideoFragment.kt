package akhmedoff.usman.videoforvk.fullscreen

import akhmedoff.usman.videoforvk.App
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseDialogFragment
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Video
import akhmedoff.usman.videoforvk.utils.vkApi
import akhmedoff.usman.videoforvk.video.VideoActivity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*

class FullscreenVideoFragment :
    BaseDialogFragment<FullscreenVideoContract.View, FullscreenVideoContract.Presenter>(),
    FullscreenVideoContract.View {

    companion object {
        const val VIDEO_ID = "video_id"

        fun createFragment(video: Video): FullscreenVideoFragment {
            val bundle = Bundle()

            bundle.putString(
                VideoActivity.VIDEO_ID,
                video.ownerId.toString() + "_" + video.id.toString()
            )

            val fragment = FullscreenVideoFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override lateinit var videoPresenter: FullscreenVideoContract.Presenter

    private var player: SimpleExoPlayer? = null

    private lateinit var decorView: View

    override fun initPresenter(): FullscreenVideoContract.Presenter = videoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        videoPresenter = FullscreenVideoPresenter(VideoRepository(vkApi))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fullscreen_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        decorView = activity?.window?.decorView!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun showVideo(item: Video) {
        val mp4VideoUri = Uri.parse(
            when {
                item.files.hls != null -> item.files.hls
                item.files.external != null -> item.files.external
                item.files.mp41080 != null -> item.files.mp41080
                item.files.mp4720 != null -> item.files.mp4720
                item.files.mp4480 != null -> item.files.mp4480
                item.files.mp4360 != null -> item.files.mp4360
                else -> item.files.mp4240
            }
        )
        // 1. Create a default TrackSelector
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(App.context, trackSelector)

        video_exo_player.setControlDispatcher(
            object : DefaultControlDispatcher() {
                override fun dispatchSetPlayWhenReady(
                    player: Player?,
                    playWhenReady: Boolean
                ): Boolean {
                    item.files.external?.let {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(it)
                            )
                        )
                        return false
                    }
                    return super.dispatchSetPlayWhenReady(player, playWhenReady)
                }
            }
        )

        video_exo_player.player = player

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            App.context,
            Util.getUserAgent(App.context, "vtv"), bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource = when {
            item.files.hls != null -> HlsMediaSource.Factory(dataSourceFactory)
            else -> ExtractorMediaSource.Factory(dataSourceFactory)
        }.createMediaSource(mp4VideoUri, null, null)

        // Prepare the player with the source.

        player?.let {
            it.prepare(videoSource)

            it.seekTo(2)
        }
    }

    override fun setSaved(saved: Boolean) {
    }

    override fun showSmallScreen() {
    }

    override fun pauseVideo() {
        player?.playWhenReady = false
    }

    override fun resumeVideo(state: Boolean, position: Long) {
        player?.seekTo(position)
        player?.playWhenReady = state
    }

    override fun startVideo() {
        player?.playWhenReady = true
    }

    override fun stopVideo() {
        player?.playWhenReady = false
        player = null
    }

    override fun getVideoId(): String = arguments?.getString(VIDEO_ID)!!

    override fun getVideoState(): Boolean? {
        return player?.playWhenReady
    }

    override fun getVideoPosition(): Long? {
        return player?.contentPosition
    }

    override fun showLoadError() {
    }
}