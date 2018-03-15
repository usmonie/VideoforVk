package akhmedoff.usman.videoforvk.videonew

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.model.Item
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.App
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.player.AudioFocusListener
import akhmedoff.usman.videoforvk.player.SimpleControlDispatcher
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.playback_exo_control_view.*
import java.io.File

class VideoFragment : Fragment(), VideoContract.View {

    companion object {
        const val FRAGMENT_TAG = "video_fragment_tag"

        private const val VIDEO_ID = "video_id"
        private const val OWNER_ID = "owner_id"

        fun getInstance(item: Item): Fragment {
            val fragment = VideoFragment()
            val bundle = Bundle()

            bundle.putString(VIDEO_ID, item.id.toString())
            bundle.putString(OWNER_ID, item.ownerId.toString())
            fragment.arguments = bundle

            return fragment
        }
    }

    override lateinit var presenter: VideoContract.Presenter

    private lateinit var file: File

    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory

    private lateinit var audioManager: AudioManager

    private lateinit var audioFocusListener: AudioFocusListener

    private lateinit var simpleControlDispatcher: SimpleControlDispatcher

    private var player: SimpleExoPlayer? = null

    private lateinit var adapter: VideoInfoRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = VideoPresenter(
            this,
            getVideoRepository(context!!, AppDatabase.getInstance(context!!).ownerDao()),
            getUserRepository(context!!)
        )

        if (savedInstanceState != null) presenter.view = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_video, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) pip_toggle.visibility = View.VISIBLE

        fullscreen_toggle.setOnClickListener { presenter.clickFullscreen() }
        pip_toggle.setOnClickListener { presenter.pipToggleButton() }
        exo_arrow_back.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }

        file = File("${context!!.filesDir.parent}/cache")

        val bandwidthMeter = DefaultBandwidthMeter()
        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        )

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = DefaultDataSourceFactory(
            App.context,
            Util.getUserAgent(App.context, "yourApplicationName"), bandwidthMeter
        )

        cacheDataSourceFactory =
                CacheDataSourceFactory(SimpleCache(file, NoOpCacheEvictor()), dataSourceFactory)

        audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        audioFocusListener = AudioFocusListener(player)

        simpleControlDispatcher = SimpleControlDispatcher(audioFocusListener, audioManager) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it)
                )
            )
        }

        video_exo_player.setControlDispatcher(simpleControlDispatcher)
        video_exo_player.player = player

        video_info_recycler.itemAnimator = DefaultItemAnimator()
        video_info_recycler.addItemDecoration(
            MarginItemDecorator(
                1,
                resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
            )
        )
        adapter = VideoInfoRecyclerAdapter { presenter.onClick(it) }
        video_info_recycler.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) =
        presenter.changedPipMode()

    override fun showVideo(item: Video) {
        adapter.video = item
        initExoPlayer(item)
    }

    override fun showOwnerInfo(owner: Owner) {
        adapter.owner = owner
    }

    private fun initExoPlayer(item: Video) {
        simpleControlDispatcher.item = item

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

        if (item.files.external != null) pip_toggle.visibility = View.GONE

        // This is the MediaSource representing the media to be played.
        val videoSource = when {
            item.files.hls != null -> HlsMediaSource.Factory(cacheDataSourceFactory)
            else -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
        }.createMediaSource(mp4VideoUri, null, null)

        player?.let {
            it.prepare(videoSource)
            it.seekTo(1)
            it.repeatMode = if (item.repeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }

    override fun setSaved(saved: Boolean) {
    }

    override fun showFullscreen(video: Video) {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }


    override fun showSmallScreen() {
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun pauseVideo() {
        player?.playWhenReady = false
    }

    override fun startVideo() {
        player?.playWhenReady = true
    }

    override fun stopVideo() {
        player?.playWhenReady = false
        player?.release()
    }

    override fun getVideoId(): String = arguments!!.getString(VIDEO_ID)

    override fun getOwnerId(): String = arguments!!.getString(OWNER_ID)

    override fun getVideoState() = player?.playWhenReady

    override fun getVideoPosition() = player?.currentPosition

    override fun showRecommendations() {
    }

    override fun showLoadError() {
        Snackbar.make(video_layout, getString(R.string.error_loading), Snackbar.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun enterPipMode() {
        activity?.enterPictureInPictureMode(
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
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity?.isInPictureInPictureMode ?: false

    override fun hideUi() {
        video_info_recycler?.visibility = View.GONE

        video_exo_player.hideController()
    }

    override fun showUi() {
        video_info_recycler?.visibility = View.VISIBLE

        video_exo_player.showController()
    }

    override fun showProgress() {
        video_loading?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        video_loading?.visibility = View.GONE
    }

    override fun showPlayer() {
        video_exo_player.visibility = View.VISIBLE
        video_exo_player.showController()
    }

    override fun hidePlayer() {
        video_exo_player.visibility = View.GONE
    }

    override fun stopAudioFocusListener() {
        audioFocusListener.player = null
    }

    override fun startAudioFocusListener() {
        audioFocusListener.player = player
    }

    override fun setPlayerFullscreen() {
        appbar.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        appbar.requestLayout()
    }

    override fun setPlayerNormal() {
        appbar.layoutParams.height =
                resources.getDimensionPixelSize(R.dimen.exo_player_height)
        appbar.requestLayout()
    }

    override fun setLiked() {
    }

    override fun setUnliked() {
    }

    override fun showShareDialog() {
    }

    override fun hideShareDialog() {
    }

    override fun showSendDialog() {
    }

    override fun hideSendDialog() {
    }

    override fun setAdded() {
    }

    override fun setDeleted() {
    }

    override fun showOwnerUser(owner: Owner) {
    }

    override fun showOwnerGroup(owner: Owner) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }
}