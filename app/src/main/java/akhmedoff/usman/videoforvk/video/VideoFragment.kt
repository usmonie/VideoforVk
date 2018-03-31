package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.model.Quality.*
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.App
import akhmedoff.usman.videoforvk.CaptchaDialog
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.player.AudioFocusListener
import akhmedoff.usman.videoforvk.player.SimpleControlDispatcher
import akhmedoff.usman.videoforvk.view.MarginItemDecorator
import android.app.PictureInPictureParams
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
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
import kotlinx.android.synthetic.main.popup_add_video_dialog.view.*
import kotlinx.android.synthetic.main.video_info_item.*
import java.io.File

class VideoFragment : Fragment(), VideoContract.View {

    companion object {
        const val FRAGMENT_TAG = "video_fragment_tag"

        private const val TRANSITION_NAME_KEY = "transition_name"
        private const val VIDEO_ID_KEY = "video_id"
        private const val OWNER_ID_KEY = "owner_id"
        private const val VIDEO_STATE_KEY = "video_state"
        private const val VIDEO_POSITION_KEY = "video_position"
        private const val IS_FULLSCREEN_KEY = "is_fullscreen"
        private const val VIDEO_QUALITY_KEY = "video_quality"
        private const val VIDEO_QUALITIES_KEY = "video_qualities"

        private const val CAPTCHA_SID = "captcha_sid"

        fun getInstance(item: Item, transitionName: String): Fragment {
            val fragment = VideoFragment()
            val bundle = Bundle()

            bundle.putString(TRANSITION_NAME_KEY, transitionName)
            bundle.putString(VIDEO_ID_KEY, item.id.toString())
            bundle.putString(OWNER_ID_KEY, item.ownerId.toString())
            fragment.arguments = bundle

            return fragment
        }
    }

    override lateinit var presenter: VideoContract.Presenter

    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory

    private lateinit var simpleControlDispatcher: SimpleControlDispatcher

    private var player: SimpleExoPlayer? = null

    private lateinit var videoInfoAdapter: VideoInfoRecyclerAdapter

    private var mediaSource: MediaSource? = null

    private var addPopupWindow: PopupWindow? = null

    private lateinit var albumsAdapter: AlbumsRecyclerAdapter

    private val captchaDialog: CaptchaDialog by lazy {
        CaptchaDialog(context!!) {
            presenter.enterCaptcha(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = VideoPresenter(
            this,
            getVideoRepository(context!!),
            getUserRepository(context!!)
        )
        if (savedInstanceState != null) presenter.view = this

        albumsAdapter = AlbumsRecyclerAdapter()
        videoInfoAdapter = VideoInfoRecyclerAdapter {
            onRecyclerItemClicked(it)
        }

        val bandwidthMeter = DefaultBandwidthMeter()
        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        )

        val dataSourceFactory = DefaultDataSourceFactory(
            App.context,
            Util.getUserAgent(App.context, "yourApplicationName"), bandwidthMeter
        )

        val file = File("${context!!.filesDir.parent}/cache")
        cacheDataSourceFactory =
                CacheDataSourceFactory(SimpleCache(file, NoOpCacheEvictor()), dataSourceFactory)

        val audioManager: AudioManager =
            context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioFocusListener = AudioFocusListener {
            if (isResumed)
                player?.playWhenReady = it

            if (isPipMode())
                video_exo_player?.hideController()
        }

        simpleControlDispatcher = SimpleControlDispatcher(audioFocusListener, audioManager) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it)
                )
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val popupView = inflater.inflate(R.layout.popup_add_video_dialog, null)
        addPopupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popupView.albums_recycler.adapter = albumsAdapter

        popupView.ok_add_popup.setOnClickListener { presenter.addToAlbums(albumsAdapter.selectedIds) }
        popupView.cancel_add_popup.setOnClickListener { addPopupWindow?.dismiss() }

        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            pip_toggle.visibility = View.VISIBLE

        appbar.transitionName = arguments?.getString(TRANSITION_NAME_KEY)

        fullscreen_toggle.setOnClickListener { presenter.clickFullscreen() }
        pip_toggle.setOnClickListener { presenter.pipToggleButton() }
        exo_quality_toggle.setOnClickListener { presenter.changeQuality() }

        exo_arrow_back.setOnClickListener {
            presenter.onBackListener()
        }

        video_exo_player.setControlDispatcher(simpleControlDispatcher)
        video_exo_player.player = player

        video_info_recycler.itemAnimator = DefaultItemAnimator()
        video_info_recycler.addItemDecoration(
            MarginItemDecorator(
                1,
                resources.getDimensionPixelSize(R.dimen.video_fragment_vertical_margin)
            )
        )

        video_info_recycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        video_info_recycler.adapter = videoInfoAdapter

        if (savedInstanceState == null)
            presenter.onStart()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState?.get(IS_FULLSCREEN_KEY) ?: false == true) {
            showFullscreen()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    private fun onRecyclerItemClicked(it: Int) {
        presenter.onClick(it)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) =
        presenter.changedPipMode()

    override fun showVideo(item: Video) {
        videoInfoAdapter.video = item
    }

    override fun showOwnerInfo(owner: Owner) {
        videoInfoAdapter.owner = owner
    }

    override fun setVideoSource(videoUrl: VideoUrl) {
        mediaSource = when (videoUrl.quality) {
            HLS -> HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(
                Uri.parse(
                    videoUrl.url
                )
            )
            else -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(
                    Uri.parse(
                        videoUrl.url
                    )
                )
        }
    }

    override fun setQuality(videoUrl: VideoUrl) {
        player?.prepare(
            when (videoUrl.quality) {
                HLS ->
                    HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(
                        Uri.parse(
                            videoUrl.url
                        )
                    )
                else -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(
                        Uri.parse(
                            videoUrl.url
                        )
                    )
            }
        )

        when (videoUrl.quality) {
            HLS -> setImageDrawable(R.drawable.ic_auto_quality_24dp)
            FULLHD -> setImageDrawable(R.drawable.ic_high_quality_24dp)
            HD -> setImageDrawable(R.drawable.ic_hd_24dp)
            qHD -> setImageDrawable(R.drawable.ic_hd_24dp)
            LOW -> setImageDrawable(R.drawable.ic_low_quality_24dp)
            P360 -> setImageDrawable(R.drawable.ic_low_quality_24dp)
            P240 -> setImageDrawable(R.drawable.ic_low_quality_24dp)
            else -> {
            }
        }
    }

    override fun setExternalUi(videoUrl: VideoUrl) {
        pip_toggle?.visibility = View.GONE
        exo_quality_toggle?.visibility = View.GONE
        simpleControlDispatcher.isExternal = true
        simpleControlDispatcher.url = videoUrl.url
    }

    override fun setSaved(saved: Boolean) {
    }

    private fun setImageDrawable(@DrawableRes id: Int) =
        context?.let {
            exo_quality_toggle.setImageDrawable(ContextCompat.getDrawable(it, id))
        }

    override fun showFullscreen() {
        video_layout.fitsSystemWindows = false
        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun getString(id: Int, vararg items: String): String = resources.getString(id, items)

    override fun showSmallScreen() {
        video_layout.fitsSystemWindows = true
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

    override fun showLoadError() {
        Snackbar.make(video_layout, getString(R.string.error_loading), Snackbar.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun enterPipMode(video: Video) {
        activity?.enterPictureInPictureMode(
            PictureInPictureParams.Builder()
                .setAspectRatio(Rational(video.width ?: 16, video.height ?: 9))
                .build()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun exitPipMode() {
        video_exo_player?.showController()
    }

    override fun isPipMode() =
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
    }

    override fun startAudioFocusListener() {
    }

    override fun setPlayerFullscreen() {
        appbar.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        appbar.requestLayout()
    }

    override fun setPlayerNormal() {
        appbar.layoutParams.height = resources.getDimensionPixelSize(R.dimen.exo_player_height)
        appbar.requestLayout()
    }

    override fun setLiked(likes: Likes) {
        videoInfoAdapter.notifyItemChanged(
            0,
            if (likes.userLikes) VideoInfoPayloads.LIKED else VideoInfoPayloads.DISLIKED
        )
    }

    override fun setUnliked(likes: Likes) {
        videoInfoAdapter.notifyItemChanged(
            0,
            if (likes.userLikes) VideoInfoPayloads.LIKED else VideoInfoPayloads.DISLIKED
        )
    }

    override fun showShareDialog(url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
    }

    override fun hideShareDialog() {
    }

    override fun showSendDialog() {
    }

    override fun hideSendDialog() {
    }

    override fun showAddDialog() {
        addPopupWindow?.showAsDropDown(add_button)
    }

    override fun hideAddDialog() {
    }

    override fun showAlbumsLoading() {
    }

    override fun hideAlbumsLoading() {
    }

    override fun setAlbums(albums: PagedList<Album>) {
    }

    override fun setAdded() {
        videoInfoAdapter.notifyItemChanged(
            0, VideoInfoPayloads.ADDED
        )
    }

    override fun setDeleted() {
        videoInfoAdapter.notifyItemChanged(
            0, VideoInfoPayloads.DELETED
        )
    }

    override fun showOwnerUser(owner: Owner) {
    }

    override fun showOwnerGroup(owner: Owner) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun getVideoId(): String = arguments!!.getString(VIDEO_ID_KEY)

    override fun getOwnerId(): String = arguments!!.getString(OWNER_ID_KEY)

    override fun getVideoState() = player?.playWhenReady

    override fun getVideoPosition() = player?.currentPosition

    override fun loadIsFullscreen() = arguments?.getBoolean(IS_FULLSCREEN_KEY) ?: false

    override fun loadVideoState() = arguments?.getBoolean(VIDEO_STATE_KEY) ?: false

    override fun loadVideoPosition() = arguments?.getLong(VIDEO_POSITION_KEY) ?: 1

    override fun getVideoQualities() =
        arguments?.getStringArrayList(VIDEO_QUALITIES_KEY) ?: emptyList<String>()

    override fun getCurrentQuality() = arguments?.getInt(VIDEO_QUALITY_KEY) ?: 0

    override fun saveVideoState(state: Boolean) {
        arguments?.putBoolean(VIDEO_STATE_KEY, state)
    }

    override fun saveVideoPosition(position: Long) {
        arguments?.putLong(VIDEO_POSITION_KEY, position)
    }

    override fun saveIsFullscreen(isFullscreen: Boolean) {
        arguments?.putBoolean(IS_FULLSCREEN_KEY, isFullscreen)
    }

    override fun setVideoPosition(position: Long) {
        player?.seekTo(position)
    }

    override fun saveVideoQualities(qualities: ArrayList<String>) {
        arguments?.putStringArrayList(VIDEO_QUALITIES_KEY, qualities)
    }

    override fun saveCurrentQuality(quality: Int) {
        arguments?.putInt(VIDEO_QUALITY_KEY, quality)
    }

    override fun showCaptcha(captchaImg: String) {
        captchaDialog.show()
        captchaDialog.loadCaptcha(captchaImg)
    }

    override fun saveCaptchaSid(sid: String) {
        arguments?.putString(CAPTCHA_SID, sid)
    }

    override fun loadCaptchaSid(): String = arguments?.getString(CAPTCHA_SID) ?: ""

    override fun back() {
        activity?.supportFragmentManager?.popBackStack()
    }
}