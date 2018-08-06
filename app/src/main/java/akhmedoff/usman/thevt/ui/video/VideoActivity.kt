package akhmedoff.usman.thevt.ui.video

import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.model.Quality.*
import akhmedoff.usman.data.utils.Utils
import akhmedoff.usman.data.utils.getAlbumRepository
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.thevt.CaptchaDialog
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.player.AudioFocusExoPlayerDecorator
import akhmedoff.usman.thevt.player.SimpleControlDispatcher
import akhmedoff.usman.thevt.services.download.ACTION_DOWNLOAD
import akhmedoff.usman.thevt.services.download.EXTRA_URL
import akhmedoff.usman.thevt.services.download.EXTRA_VIDEO_NAME
import akhmedoff.usman.thevt.services.download.VideoDownloadingService
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Rational
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.media.AudioAttributesCompat
import androidx.paging.PagedList
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.REPEAT_MODE_OFF
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
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
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.activity_video_info.*
import kotlinx.android.synthetic.main.activity_video_load_error.*
import kotlinx.android.synthetic.main.playback_exo_control_view.*
import java.io.File
import java.lang.Exception
import java.util.*

private const val TRANSITION_NAME_KEY = "transition_name"
private const val VIDEO_ID_KEY = "video_id"
private const val VIDEO_KEY = "video"
private const val OWNER_ID_KEY = "owner_id"
private const val VIDEO_STATE_KEY = "video_state"
private const val VIDEO_POSITION_KEY = "video_position"
private const val IS_FULLSCREEN_KEY = "is_fullscreen"
private const val VIDEO_QUALITY_KEY = "video_quality"
private const val VIDEO_QUALITIES_KEY = "video_qualities"

private const val CAPTCHA_SID = "captcha_sid"

class VideoActivity : AppCompatActivity(), VideoContract.View {

    companion object {
        const val FRAGMENT_TAG = "video_fragment_tag"

        fun getInstance(item: CatalogItem, transitionName: String?, context: Context): Intent {
            val intent = Intent(context, VideoActivity::class.java)

            intent.putExtra(TRANSITION_NAME_KEY, transitionName)
            intent.putExtra(VIDEO_ID_KEY, item.id.toString())
            intent.putExtra(OWNER_ID_KEY, item.ownerId.toString())

            return intent
        }

        fun getInstance(item: Video, transitionName: String?, context: Context): Intent {
            val intent = Intent(context, VideoActivity::class.java)

            intent.putExtra(TRANSITION_NAME_KEY, transitionName)
            intent.putExtra(VIDEO_ID_KEY, item.id.toString())
            intent.putExtra(OWNER_ID_KEY, item.ownerId.toString())

            return intent
        }
    }

    private val selectedAlbums = mutableListOf<Album>()

    private lateinit var simpleControlDispatcher: SimpleControlDispatcher

    override lateinit var presenter: VideoContract.Presenter

    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory

    private var simpleCache: Cache? = null

    private var player: ExoPlayer? = null

    private lateinit var popupAddMenu: PopupMenu

    private lateinit var popupDownloadQualityMenu: PopupMenu

    private val captchaDialog: CaptchaDialog by lazy(LazyThreadSafetyMode.NONE) {
        CaptchaDialog(this) {
            presenter.enterCaptcha(it)
        }
    }

    private val addVideoDialog: AddVideoDialog by lazy(LazyThreadSafetyMode.NONE) {
        AddVideoDialog(
                this,
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        presenter = VideoPresenter(
                this,
                getVideoRepository(this),
                getUserRepository(this),
                getAlbumRepository(this)
        )

        if (savedInstanceState?.containsKey(VIDEO_KEY) == true) {
            presenter.setVideo(savedInstanceState.getParcelable(VIDEO_KEY))
        } else {
            presenter.onStart()
            initPlayer()
        }

        card_view_video.transitionName = intent.getStringExtra(TRANSITION_NAME_KEY)
    }

    override fun onStart() {
        super.onStart()
        pip_toggle.isVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        video_info_stub?.isVisible = true
    }


    private fun setVideoClickListeners() {
        like_button.setOnClickListener {
            presenter.onClick(it.id)
        }

        share_button.setOnClickListener {
            presenter.onClick(it.id)
        }
/*

        send_button.setOnClickListener {
            presenter.onClick(it.id)
        }
*/

        add_button.setOnClickListener {
            popupAddMenu.show()
        }

        fullscreen_toggle.setOnClickListener { presenter.clickFullscreen() }
        pip_toggle.setOnClickListener { presenter.pipToggleButton() }
        qualities_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                presenter.changeQuality(p2)
            }
        }

        exo_arrow_back.setOnClickListener {
            presenter.onBackListener()
        }

        download_button.setOnClickListener {
            popupDownloadQualityMenu.show()
        }
    }

    private fun setErrorClickListeners() {
        error_button_reload.setOnClickListener { presenter.onStart() }

        error_button_show_browser.setOnClickListener { presenter.openBrowser() }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState?.get(IS_FULLSCREEN_KEY) ?: false == true) {
            showFullscreen()
        }

        if (savedInstanceState?.containsKey(VIDEO_KEY) == true) {
            presenter.setVideo(savedInstanceState.getParcelable(VIDEO_KEY))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()

        setVideoClickListeners()

        popupAddMenu = PopupMenu(this, add_button)
        popupAddMenu.inflate(R.menu.add_video_menu)
        popupAddMenu.setOnMenuItemClickListener {
            presenter.onClick(it.itemId)
            true
        }

        popupDownloadQualityMenu = PopupMenu(this, download_button, Gravity.BOTTOM)

        popupDownloadQualityMenu.inflate(R.menu.download_video_qualities)
    }

    override fun setVideoState(state: Boolean) {
        player?.playWhenReady = state
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putParcelable(VIDEO_KEY, presenter.getVideo())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onPictureInPictureModeChanged(
            isInPictureInPictureMode: Boolean,
            newConfig: Configuration?
    ) {
        if (!isInPictureInPictureMode)
            presenter.changedPipMode(false)
    }

    override fun initPlayer() {
        val bandwidthMeter = DefaultBandwidthMeter()

        val audioManager = getSystemService<AudioManager>()!!

        val audioAttributes = AudioAttributesCompat.Builder()
                .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributesCompat.USAGE_MEDIA)
                .build()
        player = AudioFocusExoPlayerDecorator(audioAttributes, audioManager,
                player = ExoPlayerFactory.newSimpleInstance(
                        DefaultRenderersFactory(this),
                        DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter)),
                        DefaultLoadControl()
                ))

        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter)
        val maxCacheFileSize: Long = 1024 * 1024 * 2048L
        val file = File(cacheDir, "video/${getVideoId() + getOwnerId()}")
        if (!SimpleCache.isCacheFolderLocked(file)) {
            simpleCache = SimpleCache(file, LeastRecentlyUsedCacheEvictor(maxCacheFileSize))
        } else {
            simpleCache?.release()
        }


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

        simpleControlDispatcher = SimpleControlDispatcher { url ->
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        }

        player?.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY)
                    video_exo_player.forceLayout()
            }
        })
        video_exo_player.setControlDispatcher(simpleControlDispatcher)
        video_exo_player.player = player
    }

    override fun showVideo(item: Video) {
        val artWork = when {
            item.photo800 != null -> item.photo800
            item.firstFrame800 != null -> item.firstFrame800
            item.photo640 != null -> item.photo640
            item.firstFrame320 != null -> item.firstFrame320
            item.photo320 != null -> item.photo320
            item.firstFrame160 != null -> item.firstFrame160
            item.photo130 != null -> item.photo130
            item.firstFrame130 != null -> item.firstFrame130
            else -> null
        }

        artWork?.let {
            Picasso.get().load(it).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    video_exo_player.defaultArtwork = bitmap
                }
            })
        }
        add_button.isVisible = item.canAdd
        add_button_desc.isVisible = item.canAdd

        title_text_view.text = item.title

        video_views.text = item.views?.let {
            resources.getQuantityString(
                    R.plurals.video_views,
                    it,
                    it.toString()
            )
        }

        video_desc.text = item.description

        setDrawable(like_button, when (item.likes?.userLikes) {
            true -> R.drawable.ic_favorite_fill_24dp
            else -> R.drawable.ic_favorite_border
        })
        val likes = item.likes?.likes ?: 0
        like_button_desc.text = Utils.format(likes) + " " + resources.getQuantityString(R.plurals.likes, likes)

        player?.repeatMode = if (item.repeat) REPEAT_MODE_ONE else REPEAT_MODE_OFF

        val qualitiesAdapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {


            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val textView = super.getView(position, convertView, parent)
                (textView as? TextView)?.setTextColor(ContextCompat.getColor(this@VideoActivity, android.R.color.white))
                return textView
            }

        }

        val files = item.files.asReversed()
        files.forEach {
            when (it.quality) {
                HLS -> qualitiesAdapter.add(getString(R.string.video_quality_title_hls))
                FULLHD -> {
                    qualitiesAdapter.add(getString(R.string.video_quality_title_1080p))
                    popupDownloadQualityMenu.menu.findItem(R.id.download_quality_1080p).isEnabled = true
                }
                HD -> {
                    qualitiesAdapter.add(getString(R.string.video_quality_title_720p))
                    popupDownloadQualityMenu.menu.findItem(R.id.download_quality_720p).isEnabled = true
                }
                qHD -> {
                    qualitiesAdapter.add(getString(R.string.video_quality_title_480p))
                    popupDownloadQualityMenu.menu.findItem(R.id.download_quality_480p).isEnabled = true
                }
                P360 -> {
                    qualitiesAdapter.add(getString(R.string.video_quality_title_360p))
                    popupDownloadQualityMenu.menu.findItem(R.id.download_quality_360p).isEnabled = true
                }
                P240 -> {
                    qualitiesAdapter.add(getString(R.string.video_quality_title_240p))
                    popupDownloadQualityMenu.menu.findItem(R.id.download_quality_240p).isEnabled = true
                }
                else -> {
                }
            }
        }
        qualities_spinner.adapter = qualitiesAdapter

        popupDownloadQualityMenu.setOnMenuItemClickListener {
            startService(item.title, when (it.itemId) {
                R.id.download_quality_360p -> files[1].url
                R.id.download_quality_480p -> files[2].url
                R.id.download_quality_720p -> files[3].url
                R.id.download_quality_1080p -> files[4].url
                else -> files[0].url
            })

            true
        }

    }

    private fun setDrawable(imageView: ImageView, @DrawableRes id: Int) =
            imageView.setImageDrawable(ContextCompat.getDrawable(this, id))

    override fun showOwnerInfo(owner: Owner) {
        owner_name.text = owner.name

        Picasso.get().load(owner.photo100).into(owner_avatar)
    }

    override fun setVideoSource(videoUrl: VideoUrl) {

    }

    override fun setQuality(videoUrl: VideoUrl) {
        player?.prepare(
                when (videoUrl.quality) {
                    HLS -> HlsMediaSource.Factory(cacheDataSourceFactory)
                    else -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
                }.createMediaSource(videoUrl.url.toUri())
        )
    }

    override fun setExternalUi(videoUrl: VideoUrl) {
        pip_toggle?.isVisible = false
        qualities_spinner?.isVisible = false
        simpleControlDispatcher.isExternal = true
        simpleControlDispatcher.url = videoUrl.url
        fullscreen_toggle.isVisible = false
        progress_layout.isVisible = false
        download_button.isVisible = false
        download_button_desc.isVisible = false
        player?.release()
    }

    override fun setSaved(saved: Boolean) {
    }

    override fun showFullscreen() {
        video_layout.fitsSystemWindows = false
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun showSmallScreen() {
        video_layout.fitsSystemWindows = true
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
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
        player = null
        video_exo_player.player = null
    }

    override fun showLoadError(isError: Boolean) {
        video_error_loading_stub?.isVisible = isError
        error_mode?.isVisible = isError
        if (isError) setErrorClickListeners()

        card_view_video.isVisible = !isError
        showProgress(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun enterPipMode(video: Video) {
        presenter.changedPipMode(true)
        enterPictureInPictureMode(PictureInPictureParams.Builder()
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
            && isInPictureInPictureMode

    override fun showUi(isShowing: Boolean) {
        nested_scroll_view?.isVisible = isShowing
        showProgress(false)
        showLoadError(false)
        if (isShowing) {
            video_exo_player.showController()
        } else {
            video_exo_player.hideController()
        }
    }

    override fun showProgress(isLoading: Boolean) {
        video_loading?.isVisible = isLoading
    }

    override fun showPlayer(isShowing: Boolean) {
        video_exo_player.isVisible = isShowing
        if (isShowing) video_exo_player.showController()
    }

    override fun setPlayerFullscreen() {
        card_view_video.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        card_view_video.radius = 0F
        val linearLayoutParams = card_view_video.layoutParams as LinearLayout.LayoutParams
        linearLayoutParams.setMargins(0)
        card_view_video.layoutParams = linearLayoutParams
        card_view_video.requestLayout()
        video_exo_player.requestLayout()
    }

    override fun setPlayerNormal() {
        card_view_video.layoutParams.height = resources.getDimensionPixelSize(R.dimen.exo_player_height)
        card_view_video.radius = resources.getDimensionPixelSize(R.dimen.video_card_corner_radius).toFloat()
        val linearLayoutParams = card_view_video.layoutParams as LinearLayout.LayoutParams
        linearLayoutParams.setMargins(resources.getDimensionPixelSize(R.dimen.catalog_videos_margin))
        card_view_video.layoutParams = linearLayoutParams
        card_view_video.requestLayout()
    }

    override fun setLiked(likes: Likes) {
        setDrawable(like_button,
                if (likes.userLikes) R.drawable.ic_favorite_fill_24dp
                else R.drawable.ic_favorite_border)
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

    override fun showSelectedAlbums(ids: List<Int>) = addVideoDialog.setSelectedAlbums(ids)

    override fun setAdded() {
        popupAddMenu.menu.clear()
        popupAddMenu.inflate(R.menu.delete_video_menu)
        setDrawable(add_button, R.drawable.ic_done_black_24dp)
    }

    override fun setDeleted() {
        popupAddMenu.menu.clear()
        popupAddMenu.inflate(R.menu.add_video_menu)
        setDrawable(add_button, R.drawable.ic_add)
    }

    override fun showOwnerUser(owner: Owner) {
    }

    override fun showOwnerGroup(owner: Owner) {
    }

    override fun showCaptcha(captchaImg: String) {
        captchaDialog.show()
        captchaDialog.loadCaptcha(captchaImg)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        player?.stop(true)
        presenter.onStart()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isPipMode() && player?.playWhenReady == true) {
            enterPipMode(presenter.getVideo())
        } else {
            presenter.onPause()
        }
    }

    override fun onPause() {
        super.onPause()

        if (isFinishing) {
            simpleCache?.release()
            presenter.onDestroyView()
        }
    }

    override fun getVideoId(): String = intent.getStringExtra(VIDEO_ID_KEY)
            ?: intent.getParcelableExtra<Video>(VIDEO_ID_KEY)?.id?.toString() ?: ""

    override fun getOwnerId(): String = intent.getStringExtra(OWNER_ID_KEY)
            ?: intent.extras.getParcelable<Video>(VIDEO_ID_KEY)?.ownerId?.toString() ?: ""

    override fun getVideoState() = player?.playWhenReady

    override fun getVideoPosition() = player?.currentPosition

    override fun loadIsFullscreen() = intent.getBooleanExtra(IS_FULLSCREEN_KEY, false)

    override fun loadVideoState() = intent.getBooleanExtra(VIDEO_STATE_KEY, false)

    override fun loadVideoPosition() = intent.getLongExtra(VIDEO_POSITION_KEY, 1)

    override fun getVideoQualities() = intent.getStringArrayListExtra(VIDEO_QUALITIES_KEY)
            ?: emptyList<String>()

    override fun getCurrentQuality() = intent.getIntExtra(VIDEO_QUALITY_KEY, 0)

    override fun saveVideoState(state: Boolean) {
        intent.putExtra(VIDEO_STATE_KEY, state)
    }

    override fun saveVideoPosition(position: Long) {
        intent.putExtra(VIDEO_POSITION_KEY, position)
    }

    override fun saveIsFullscreen(isFullscreen: Boolean) {
        intent.putExtra(IS_FULLSCREEN_KEY, isFullscreen)
    }

    override fun setVideoPosition(position: Long) {
        player?.seekTo(position)
    }


    override fun saveVideoQualities(qualities: ArrayList<String>) {
        intent.putStringArrayListExtra(VIDEO_QUALITIES_KEY, qualities)
    }

    override fun saveCurrentQuality(quality: Int) {
        intent.putExtra(VIDEO_QUALITY_KEY, quality)
    }

    override fun saveCaptchaSid(sid: String) {
        intent.putExtra(CAPTCHA_SID, sid)
    }

    override fun loadCaptchaSid(): String = intent.getStringExtra(CAPTCHA_SID) ?: ""

    override fun back() = super.onBackPressed()

    override fun onBackPressed() {
        presenter.onBackListener()
    }

    override fun getString(id: Int, vararg items: String): String = resources.getString(id, *items)

    override fun showVideoInBrowser(url: String) =
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))

    private fun startService(title: String, url: String) =
            ContextCompat.startForegroundService(this,
                    Intent(this, VideoDownloadingService::class.java).apply {
                        action = ACTION_DOWNLOAD
                        putExtra(EXTRA_VIDEO_NAME, title)
                        putExtra(EXTRA_URL, url)
                    }
            )

}