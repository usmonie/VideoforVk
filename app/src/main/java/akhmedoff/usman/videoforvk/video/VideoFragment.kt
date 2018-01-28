package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseFragment
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Group
import akhmedoff.usman.videoforvk.model.Item
import akhmedoff.usman.videoforvk.model.User
import akhmedoff.usman.videoforvk.model.Video
import akhmedoff.usman.videoforvk.utils.vkApi
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.playback_exo_control_view.*
import java.text.SimpleDateFormat
import java.util.*

class VideoFragment : BaseFragment<VideoContract.View, VideoContract.Presenter>(),
    VideoContract.View {

    private lateinit var fullscreenDialog: Dialog

    companion object {

        private const val VIDEO_ID = "video_id"

        fun create(item: Item): VideoFragment {
            val fragment = VideoFragment()
            val bundle = Bundle()

            bundle.putString(VIDEO_ID, item.ownerId.toString() + "_" + item.id.toString())
            fragment.arguments = bundle
            return fragment
        }
    }

    override lateinit var videoPresenter: VideoPresenter

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        videoPresenter = VideoPresenter(
            VideoRepository(
                UserSettings.getUserSettings(context?.applicationContext!!), vkApi
            )
        )
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_video, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullscreen_toggle.setOnClickListener { videoPresenter.clickFullscreen() }
    }

    override fun showVideo(item: Video) {
        initVideoInfo(item)
        initExoPlayer(item)
    }

    override fun getVideoId() = arguments!!.getString(VIDEO_ID)!!

    private fun initVideoInfo(item: Video) {
        video_title.text = item.title
        video_views.text = item.views.toString()
        video_desc.text = item.description
        video_desc.linksClickable = true

        val date = Date(item.date)
        video_data.text = SimpleDateFormat(
            "HH:mm, dd MMM ",
            Locale.getDefault()
        ).format(date)
    }

    private fun initExoPlayer(item: Video) {
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
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

        simpleExoPlayerView.setControlDispatcher(
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
            })
        simpleExoPlayerView.player = player

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource = when {
            item.files.hls != null -> HlsMediaSource.Factory(dataSourceFactory)
            else -> ExtractorMediaSource.Factory(dataSourceFactory)
        }.createMediaSource(mp4VideoUri, null, null)

        // Prepare the player with the source.
        player?.prepare(videoSource)
    }


    override fun showGroupOwnerInfo(group: Group) {
        owner_name.text = group.name
        Picasso.with(context).load(group.photo100).into(owner_photo)
    }

    override fun showUserOwnerInfo(user: User) {
        owner_name.text =
                String.format(
                    resources.getText(R.string.user_name).toString(),
                    user.firstName,
                    user.lastName
                )
        Picasso.with(context).load(user.photo100).into(owner_photo)
    }

    override fun pauseVideo() {
        player?.playWhenReady = false
    }

    override fun resumeVideo() {

    }

    override fun initFullscreen() {
        fullscreenDialog =
                object : Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                    override fun onBackPressed() {
                        presenter.clickFullscreen()
                    }
                }

        fullscreenDialog.requestWindowFeature(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    override fun startVideo() {
        player?.playWhenReady = true
    }

    override fun stopVideo() {
        player?.playWhenReady = false
        player?.release()
    }

    override fun setSaved(saved: Boolean) {
    }

    override fun showFullscreen() {
        (simpleExoPlayerView.parent as ViewGroup).removeView(simpleExoPlayerView)
        fullscreenDialog.addContentView(
            simpleExoPlayerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        fullscreen_toggle.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.ic_fullscreen_exit_white_24dp
            )
        )
        fullscreenDialog.show()
    }

    override fun showSmallScreen() {
        (simpleExoPlayerView.parent as ViewGroup).removeView(simpleExoPlayerView)
        (collapsing_toolbar as FrameLayout).addView(simpleExoPlayerView)

        fullscreenDialog.dismiss()
        fullscreen_toggle.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.ic_fullscreen_white_24dp
            )
        )
    }

    override fun initPresenter(): VideoContract.Presenter = videoPresenter
}