package akhmedoff.usman.videoforvk.ui.video

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.*
import android.arch.lifecycle.LifecycleOwner
import android.arch.paging.PagedList
import android.support.annotation.StringRes

interface VideoContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun showVideo(item: Video)

        fun getVideoId(): String

        fun pauseVideo()

        fun startVideo()

        fun stopVideo()

        fun showOwnerInfo(owner: Owner)

        fun getOwnerId(): String

        fun showFullscreen()

        fun showSmallScreen()

        fun enterPipMode(video: Video)

        fun exitPipMode()

        fun isPipMode(): Boolean

        fun showUi(isShowing: Boolean)

        fun showProgress(isLoading: Boolean)

        fun showPlayer(isShowing: Boolean)

        fun getVideoState(): Boolean?

        fun getVideoPosition(): Long?

        fun loadIsFullscreen(): Boolean

        fun loadVideoState(): Boolean

        fun loadVideoPosition(): Long

        fun setVideoPosition(position: Long)

        fun saveVideoState(state: Boolean)

        fun saveVideoPosition(position: Long)

        fun saveIsFullscreen(isFullscreen: Boolean)

        fun setPlayerFullscreen()

        fun setPlayerNormal()

        fun setLiked(likes: Likes)

        fun showShareDialog(videoName: String, url: String)

        fun hideShareDialog()

        fun showCaptcha(captchaImg: String)

        fun showSendDialog()

        fun hideSendDialog()

        fun showAddDialog()

        fun hideAddDialog()

        fun showAlbumsLoading(isLoading: Boolean)

        fun showAlbums(albums: PagedList<Album>)

        fun showSelectedAlbums(ids: List<Int>)

        fun setAdded()

        fun setSaved(saved: Boolean)

        fun setDeleted()

        fun showOwnerUser(owner: Owner)

        fun showOwnerGroup(owner: Owner)

        fun setVideoSource(videoUrl: VideoUrl)

        fun setExternalUi(videoUrl: VideoUrl)

        fun getVideoQualities(): List<String>

        fun getCurrentQuality(): Int

        fun saveVideoQualities(qualities: ArrayList<String>)

        fun saveCurrentQuality(quality: Int)

        fun setQuality(videoUrl: VideoUrl)

        fun saveCaptchaSid(sid: String)

        fun loadCaptchaSid(): String

        fun back()

        fun getString(@StringRes id: Int, vararg items: String): String

        fun showLoadError(isError: Boolean)

        fun showVideoInBrowser(url: String)
    }

    interface Presenter {

        var view: View?

        fun onClick(itemView: Int)

        fun onStart()

        fun onResume()

        fun onPause()

        fun onStop()

        fun onDestroyView()

        fun loadVideo(id: String)

        fun clickFullscreen()

        fun error(error: Error, message: String)

        fun pipToggleButton()

        fun changedPipMode()

        fun ownerClicked()

        fun changeQuality()

        fun enterCaptcha(captchaCode: String)

        fun onBackListener()

        fun addToAlbums(albumsIds: MutableList<Album>)

        fun openBrowser()

        fun getVideo(): Video

        fun setVideo(video: Video)
    }
}