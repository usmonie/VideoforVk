package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.Group
import akhmedoff.usman.videoforvk.model.User
import akhmedoff.usman.videoforvk.model.Video
import akhmedoff.usman.videoforvk.player.AudioFocusListener
import akhmedoff.usman.videoforvk.utils.vkApi
import android.annotation.TargetApi
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.playback_exo_control_view.*
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@TargetApi(Build.VERSION_CODES.O)
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

    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    override lateinit var videoPresenter: VideoPresenter

    private var player: SimpleExoPlayer? = null

    private val audioManager: AudioManager by lazy {
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private val audioFocusListener: AudioFocusListener by lazy {
        AudioFocusListener(player)
    }

    private val audioFocusRequest: AudioFocusRequest by lazy {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
            .build()
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(audioAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusListener)
            .build()
    }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) pip_toggle.visibility = View.VISIBLE

        fullscreen_toggle.setOnClickListener { videoPresenter.clickFullscreen() }
        pip_toggle.setOnClickListener { videoPresenter.pipToggleButton() }

        // 1. Create a default TrackSelector
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter
        )

        video_exo_player.player = player

    }

    override fun showVideo(item: Video) {
        initVideoInfo(item)
        initExoPlayer(item)
    }

    override fun showLoadError() =
        Snackbar.make(video_layout, getString(R.string.error_loading), Snackbar.LENGTH_LONG).show()

    override fun showRecommendations() {

    }

    override fun setPlayerFullscreen() {
        video_exo_player.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        video_exo_player.requestLayout()
    }

    override fun setPlayerNormal() {
        video_exo_player.layoutParams.height =
                resources.getDimension(R.dimen.exo_player_height).toInt()

        video_exo_player.requestLayout()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun enterPipMode() {
        enterPictureInPictureMode(
            PictureInPictureParams.Builder()
                .build()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun exitPipMode() {
        exitPipMode()
        video_exo_player?.showController()
    }

    override fun isPipMode(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isInPictureInPictureMode

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) = videoPresenter.changedPipMode()

    private fun initExoPlayer(item: Video) {
        video_exo_player.setControlDispatcher(
            object : DefaultControlDispatcher() {
                override fun dispatchSetPlayWhenReady(
                    player: Player?,
                    playWhenReady: Boolean
                ): Boolean {
                    return super.dispatchSetPlayWhenReady(
                        player,
                        when (item.files.external) {
                            null -> {
                                val res = when {
                                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> audioManager.requestAudioFocus(
                                        audioFocusRequest
                                    )
                                    else -> audioManager.requestAudioFocus(
                                        audioFocusListener,
                                        AudioManager.STREAM_MUSIC,
                                        AudioManager.AUDIOFOCUS_GAIN
                                    )
                                }

                                Log.d("audio response", res.toString())


                                when (res) {
                                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> playWhenReady
                                    else -> false
                                }

                            }

                            else -> {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(item.files.external)
                                    )
                                )
                                false
                            }
                        }
                    )
                }
            }
        )
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

        val file = File("${filesDir.parent}/cache")
        val cacheDataSourceFactory =
            CacheDataSourceFactory(SimpleCache(file, NoOpCacheEvictor()), dataSourceFactory)

        // This is the MediaSource representing the media to be played.
        val videoSource = when {
            item.files.hls != null -> HlsMediaSource.Factory(cacheDataSourceFactory)
            else -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
        }.createMediaSource(mp4VideoUri, null, null)

        player?.let {
            it.prepare(videoSource)
            it.seekTo(1)
        }
    }

    override fun showPlayer() {
        video_exo_player.visibility = View.VISIBLE
        video_exo_player.showController()
    }

    override fun hidePlayer() {
        video_exo_player.visibility = View.GONE
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        videoPresenter.onCreate()
    }

    private fun initVideoInfo(item: Video) {
        video_title?.text = item.title
        video_views?.text = item.views?.toString()

        item.views?.let {
            video_views?.text = resources.getQuantityString(
                R.plurals.video_views,
                it,
                NumberFormat.getIntegerInstance().format(it)
            )
        }

        video_date?.text = SimpleDateFormat(
            "HH:mm, dd MMM yyyy",
            Locale.getDefault()
        ).format(Date(item.date * 1000L))
    }

    override fun showGroupOwnerInfo(group: Group) {
        owner_name?.text = group.name
        owner_photo?.let {
            Picasso.with(context).load(group.photo100).into(it)
        }

        owner_follow?.text =
                if (group.isMember) getText(R.string.followed) else getText(R.string.follow)
    }

    override fun showUserOwnerInfo(user: User) {
        owner_name?.text =
                String.format(
                    resources.getText(R.string.user_name).toString(),
                    user.firstName,
                    user.lastName
                )

        owner_photo?.let {
            Picasso.with(context).load(user.photo100).into(it)
        }
        owner_follow?.text =
                if (user.isFriend) getText(R.string.followed) else getText(R.string.follow)
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
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun showSmallScreen() {
    }

    override fun showProgress() {
        video_loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        video_loading.visibility = View.GONE
    }

    override fun hideUi() {
        recommendation_recycler_video_page?.visibility = View.GONE
        cardView?.visibility = View.GONE
        video_title?.visibility = View.GONE
        video_views?.visibility = View.GONE
        video_date?.visibility = View.GONE

        video_exo_player.hideController()
    }

    override fun showUi() {
        recommendation_recycler_video_page?.visibility = View.VISIBLE
        cardView?.visibility = View.VISIBLE
        video_title?.visibility = View.VISIBLE
        video_views?.visibility = View.VISIBLE
        video_date?.visibility = View.VISIBLE
    }

    override fun getVideoState() = player?.playWhenReady

    override fun getVideoPosition() = player?.currentPosition

    override fun getVideoId() = intent.getStringExtra(VideoActivity.VIDEO_ID)!!
}
