package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.fullscreen.FullscreenVideoFragment
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.Group
import akhmedoff.usman.videoforvk.model.User
import akhmedoff.usman.videoforvk.model.Video
import akhmedoff.usman.videoforvk.utils.vkApi
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.util.Rational
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
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
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.playback_exo_control_view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class VideoActivity : BaseActivity<VideoContract.View, VideoContract.Presenter>(),
    VideoContract.View {

    companion object {
        const val VIDEO_ID = "video_id"

        fun getActivity(item: Video, context: Context): Intent {
            val intent = Intent(context, VideoActivity::class.java)

            intent.putExtra(VIDEO_ID, item.ownerId.toString() + "_" + item.id.toString())

            return intent
        }

        fun getActivity(item: CatalogItem, context: Context): Intent {
            val intent = Intent(context, VideoActivity::class.java)

            intent.putExtra(VIDEO_ID, item.ownerId.toString() + "_" + item.id.toString())

            return intent
        }

    }

    override lateinit var videoPresenter: VideoPresenter
    private var player: SimpleExoPlayer? = null

    override fun initPresenter() = videoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        videoPresenter = VideoPresenter(
            VideoRepository(vkApi),
            UserRepository(
                userSettings = UserSettings.getUserSettings(applicationContext),
                api = vkApi
            )
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        fullscreen_toggle.setOnClickListener { videoPresenter.clickFullscreen() }
        pip_toggle.setOnClickListener { videoPresenter.pipToggleButton() }
    }

    override fun showVideo(item: Video) {
        initVideoInfo(item)
        initExoPlayer(item)
    }

    override fun getVideoState() = player?.playWhenReady

    override fun getVideoPosition() = player?.currentPosition

    override fun getVideoId() = intent.getStringExtra(VideoActivity.VIDEO_ID)!!

    override fun showLoadError() {
    }

    override fun showRecommendations() {

    }

    override fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(
                PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(video_exo_player.width, video_exo_player.height))
                    .build()
            )
        }
    }

    override fun exitPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            exitPipMode()
            video_exo_player?.showController()
            video_exo_player?.layoutParams?.width = MATCH_PARENT
        }
    }

    override fun isPipMode(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isInPictureInPictureMode

    private fun initVideoInfo(item: Video) {
        video_title?.text = item.title
        video_views?.text = item.views.toString()

        item.views?.let {
            video_views?.text = resources.getQuantityString(
                R.plurals.video_views,
                it,
                NumberFormat.getIntegerInstance().format(it)
            )
        }

        video_date?.text = SimpleDateFormat(
            "HH:mm, dd MMM ",
            Locale.getDefault()
        ).format(Date(item.date))
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        videoPresenter.changedPipMode()

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
            context,
            Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource = when {
            item.files.hls != null -> HlsMediaSource.Factory(dataSourceFactory)
            else -> ExtractorMediaSource.Factory(dataSourceFactory)
        }.createMediaSource(mp4VideoUri, null, null)

        // Prepare the player with the source.

        player?.let {
            it.prepare(videoSource)

            it.seekTo(1)
        }
    }

    override fun showGroupOwnerInfo(group: Group) {
        owner_name?.text = group.name
        owner_photo?.let {
            Picasso.with(context).load(group.photo100).into(it)
        }

        owner_follow?.text = when {
            group.isMember -> getText(R.string.followed)
            else -> getText(R.string.follow)
        }
    }

    override fun showUserOwnerInfo(user: User) {
        owner_name?.text =
                String.format(
                    resources.getText(R.string.user_name).toString(),
                    user.firstName,
                    user.lastName
                )
        Picasso
            .with(context)
            .load(user.photo100)
            .into(owner_photo)

        owner_follow?.text = when {
            user.isFavorite -> getText(R.string.followed)
            else -> getText(R.string.follow)
        }

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
        player?.release()
    }

    override fun setSaved(saved: Boolean) {
    }

    override fun showFullscreen(video: Video) {
        val fragmentManager = supportFragmentManager
        val newFragment = FullscreenVideoFragment.createFragment(video)

        // The device is smaller, so show the fragment fullscreen
        val transaction = fragmentManager.beginTransaction()
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
            .addToBackStack(null).commit()
    }

    override fun showSmallScreen() {
    }

    override fun hideUi() {
        recommendation_recycler_video_page?.visibility = View.GONE
        cardView?.visibility = View.GONE
        video_title?.visibility = View.GONE
        video_views?.visibility = View.GONE
        video_date?.visibility = View.GONE
        video_exo_player?.hideController()

    }

    override fun showUi() {
        recommendation_recycler_video_page?.visibility = View.VISIBLE
        cardView?.visibility = View.VISIBLE
        video_title?.visibility = View.VISIBLE
        video_views?.visibility = View.VISIBLE
        video_date?.visibility = View.VISIBLE
        video_exo_player?.showController()

    }
}
