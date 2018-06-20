package akhmedoff.usman.videoforvk.ui.video

import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.model.Quality.*
import akhmedoff.usman.data.utils.getAlbumRepository
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.App
import akhmedoff.usman.videoforvk.CaptchaDialog
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.player.AudioFocusListener
import akhmedoff.usman.videoforvk.player.SimpleControlDispatcher
import akhmedoff.usman.videoforvk.services.download.ACTION_DOWNLOAD
import akhmedoff.usman.videoforvk.services.download.EXTRA_URL
import akhmedoff.usman.videoforvk.services.download.EXTRA_VIDEO_NAME
import akhmedoff.usman.videoforvk.services.download.VideoDownloadingService
import android.app.PictureInPictureParams
import android.arch.paging.PagedList
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.text.format.DateUtils
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.systemService
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.playback_exo_control_view.*
import java.io.File
import java.util.*

private const val TRANSITION_NAME_KEY = "transition_name"
private const val VIDEO_ID_KEY = "video_id"
private const val OWNER_ID_KEY = "owner_id"
private const val VIDEO_STATE_KEY = "video_state"
private const val VIDEO_POSITION_KEY = "video_position"
private const val IS_FULLSCREEN_KEY = "is_fullscreen"
private const val VIDEO_QUALITY_KEY = "video_quality"
private const val VIDEO_QUALITIES_KEY = "video_qualities"

private const val CAPTCHA_SID = "captcha_sid"

class VideoFragment : Fragment(), VideoContract.View {

    companion object {
        const val FRAGMENT_TAG = "video_fragment_tag"

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

    private val selectedAlbums = mutableListOf<Album>()

    override lateinit var presenter: VideoContract.Presenter

    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory

    private lateinit var simpleControlDispatcher: SimpleControlDispatcher
    private lateinit var simpleCache: Cache

    private var player: SimpleExoPlayer? = null

    private lateinit var popupAddMenu: PopupMenu

    private lateinit var popupDownloadQualityMenu: PopupMenu

    private val addVideoDialog: AddVideoDialog by lazy {
        AddVideoDialog(
                context!!,
                { addVideoDialog.hide() },
                { album: Album, isChecked: Boolean ->
                    if (isChecked) selectedAlbums.add(album)
                    else selectedAlbums.remove(album)
                },
                {
                    addVideoDialog.hide()
                    presenter.addToAlbums(selectedAlbums)
                })
    }

    private val captchaDialog: CaptchaDialog by lazy {
        CaptchaDialog(context!!) {
            presenter.enterCaptcha(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_video, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = VideoPresenter(
                this,
                getVideoRepository(context!!),
                getUserRepository(context!!),
                getAlbumRepository(context!!)
        )
        if (savedInstanceState != null) presenter.view = this

        initPlayer()

        error_button_reload.setOnClickListener { presenter.onStart() }

        error_button_show_browser.setOnClickListener { presenter.openBrowser() }

        pip_toggle.isVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

        appbar.transitionName = arguments?.getString(TRANSITION_NAME_KEY)

        fullscreen_toggle.setOnClickListener { presenter.clickFullscreen() }
        pip_toggle.setOnClickListener { presenter.pipToggleButton() }
        exo_quality_toggle.setOnClickListener { presenter.changeQuality() }

        exo_arrow_back.setOnClickListener {
            presenter.onBackListener()
        }

        download_button.setOnClickListener {
            popupDownloadQualityMenu.show()
        }

        video_exo_player.setControlDispatcher(simpleControlDispatcher)
        video_exo_player.player = player

        presenter.onStart()

        popupAddMenu = PopupMenu(context!!, add_button).apply {
            inflate(R.menu.add_video_menu)
            setOnMenuItemClickListener {
                presenter.onClick(it.itemId)
                true
            }
        }


        like_button.setOnClickListener {
            presenter.onClick(it.id)
        }

        share_button.setOnClickListener {
            presenter.onClick(it.id)
        }

        send_button.setOnClickListener {
            presenter.onClick(it.id)
        }

        add_button.setOnClickListener {
            popupAddMenu.show()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState?.get(IS_FULLSCREEN_KEY) ?: false == true)
            showFullscreen()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleCache.release()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) =
            presenter.changedPipMode()

    private fun initPlayer() {
        val bandwidthMeter = DefaultBandwidthMeter()

        player = ExoPlayerFactory.newSimpleInstance(
                context,
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        )

        val dataSourceFactory = DefaultDataSourceFactory(
                App.context,
                Util.getUserAgent(App.context, "yourApplicationName"), bandwidthMeter
        )

        simpleCache = SimpleCache(
                File(context!!.cacheDir, "video"),
                LeastRecentlyUsedCacheEvictor(1024 * 1024 * 1024))

        val maxCacheFileSize: Long = 1024 * 1024 * 2048L
        val cacheFlags = CacheDataSource.FLAG_BLOCK_ON_CACHE or
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR

        cacheDataSourceFactory = CacheDataSourceFactory(
                simpleCache,
                dataSourceFactory,
                FileDataSourceFactory(),
                CacheDataSinkFactory(simpleCache, maxCacheFileSize),
                cacheFlags,
                null
        )

        val audioManager = context!!.systemService<AudioManager>()

        val audioFocusListener = AudioFocusListener { isOnFocus ->
            player?.playWhenReady = isResumed && isOnFocus

            if (isPipMode()) video_exo_player?.hideController()
        }

        simpleControlDispatcher =
                SimpleControlDispatcher(audioFocusListener, audioManager) { url ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
    }


    override fun showVideo(item: Video) {
        video_title.text = item.title

        video_date.text = DateUtils.getRelativeTimeSpanString(
                item.date * 1000,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS
        )

        video_views.text = item.views?.let {
            resources.getQuantityString(
                    R.plurals.video_views,
                    it,
                    it.toString()
            )
        }

        video_desc.text = item.description

        video_info_card.isVisible = true
        setDrawable(like_button, when (item.likes?.userLikes) {
            true -> R.drawable.ic_favorite_fill_24dp
            else -> R.drawable.ic_favorite_border
        })

        player?.repeatMode = if (item.repeat) REPEAT_MODE_ONE
        else REPEAT_MODE_OFF

        popupDownloadQualityMenu = PopupMenu(context!!, add_button).apply {
            inflate(R.menu.download_video_qualities)
            val files = item.files.asReversed()
            files.forEach {
                when (it.quality) {
                    FULLHD -> menu.findItem(R.id.download_quality_1080p).isEnabled = true
                    HD -> menu.findItem(R.id.download_quality_720p).isEnabled = true
                    qHD -> menu.findItem(R.id.download_quality_480p).isEnabled = true
                    P360 -> menu.findItem(R.id.download_quality_360p).isEnabled = true
                    P240 -> menu.findItem(R.id.download_quality_240p).isEnabled = true
                    else -> {
                    }
                }
            }


            setOnMenuItemClickListener {
                startService(item.title, when (it.itemId) {
                    R.id.download_quality_240p -> files[0].url
                    R.id.download_quality_360p -> files[1].url
                    R.id.download_quality_480p -> files[2].url
                    R.id.download_quality_720p -> files[3].url
                    R.id.download_quality_1080p -> files[4].url
                    else -> files[0].url
                })

                true
            }
        }
    }

    private fun setDrawable(imageView: ImageView, @DrawableRes id: Int) =
            imageView.setImageDrawable(ContextCompat.getDrawable(context!!, id))

    override fun showOwnerInfo(owner: Owner) {
        owner_name.text = owner.name

        Picasso
                .get()
                .load(owner.photo100)
                .into(owner_avatar)
    }

    override fun setVideoSource(videoUrl: VideoUrl) {

    }

    override fun setQuality(videoUrl: VideoUrl) {
        player?.prepare(
                when (videoUrl.quality) {
                    HLS ->
                        HlsMediaSource.Factory(cacheDataSourceFactory)
                                .createMediaSource(Uri.parse(videoUrl.url))
                    else -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
                            .createMediaSource(Uri.parse(videoUrl.url))
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
        pip_toggle?.isVisible = false
        exo_quality_toggle?.isVisible = false
        simpleControlDispatcher.isExternal = true
        simpleControlDispatcher.url = videoUrl.url
        fullscreen_toggle.isVisible = false
        progress_layout.isVisible = false
        download_button.isVisible = false
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

    override fun showLoadError(isError: Boolean) {
        error_mode.isVisible = isError
        appbar.isVisible = !isError
        nested_scroll_view.isVisible = !isError
        video_loading.isVisible = !isError
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun enterPipMode(video: Video) {
        activity?.enterPictureInPictureMode(
                PictureInPictureParams.Builder()
                        .setAspectRatio(Rational(video.width ?: 16,
                                video.height ?: 9))
                        .build()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun exitPipMode() {
        video_exo_player?.showController()
    }

    override fun isPipMode() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && activity?.isInPictureInPictureMode ?: false

    override fun showUi(isShowing: Boolean) {
        nested_scroll_view?.isVisible = isShowing
        showLoadError(false)
        if (isShowing)
            video_exo_player.showController()
    }

    override fun showProgress(isLoading: Boolean) {
        video_loading?.isVisible = isLoading
    }

    override fun showPlayer(isShowing: Boolean) {
        video_exo_player.isVisible = isShowing
        if (isShowing) video_exo_player.showController()
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
        setDrawable(like_button,
                if (likes.userLikes) R.drawable.ic_favorite_fill_24dp
                else R.drawable.ic_favorite_border)
    }

    override fun setUnliked(likes: Likes) {
    }

    override fun showShareDialog(videoName: String, url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                String.format(resources.getText(R.string.shared_with_vt).toString(), videoName, url))

        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
    }

    override fun hideShareDialog() {
    }

    override fun showSendDialog() {
    }

    override fun hideSendDialog() {
    }

    override fun showAddDialog() = addVideoDialog.show()

    override fun hideAddDialog() = addVideoDialog.hide()

    override fun showAlbumsLoading(isLoading: Boolean) =
            addVideoDialog.showLoading(isLoading)

    override fun showAlbums(albums: PagedList<Album>) {
        if (addVideoDialog.isShowing)
            addVideoDialog.showAlbums(albums)
    }

    override fun showSelectedAlbums(ids: List<Int>) =
            addVideoDialog.setSelectedAlbums(ids)

    override fun setAdded() {
        setDrawable(add_button, R.drawable.ic_done_black_24dp)
        popupAddMenu.menu.clear()
        popupAddMenu.inflate(R.menu.delete_video_menu)
    }

    override fun setDeleted() {
        setDrawable(add_button, R.drawable.ic_add)
        popupAddMenu.menu.clear()
        popupAddMenu.inflate(R.menu.add_video_menu)
    }

    override fun showOwnerUser(owner: Owner) {
    }

    override fun showOwnerGroup(owner: Owner) {
    }

    override fun showCaptcha(captchaImg: String) {
        captchaDialog.show()
        captchaDialog.loadCaptcha(captchaImg)
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

    override fun saveCaptchaSid(sid: String) {
        arguments?.putString(CAPTCHA_SID, sid)
    }

    override fun loadCaptchaSid(): String = arguments?.getString(CAPTCHA_SID) ?: ""

    override fun back() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun getString(id: Int, vararg items: String): String =
            resources.getString(id, *items)

    override fun showVideoInBrowser(url: String) =
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    private fun startService(title: String, url: String) {
        ContextCompat.startForegroundService(context!!,
                Intent(context, VideoDownloadingService::class.java).apply {
                    this.action = ACTION_DOWNLOAD
                    this.putExtra(EXTRA_VIDEO_NAME, title)
                    this.putExtra(EXTRA_URL, url)
                })
    }
}