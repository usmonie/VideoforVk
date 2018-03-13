package akhmedoff.usman.videoforvk.videonew

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.Video
import android.arch.lifecycle.LifecycleOwner

interface VideoContract {

    interface View : LifecycleOwner {
        var presenter: Presenter

        fun showOwnerInfo(owner: Owner)

        fun showVideo(item: Video)

        fun setSaved(saved: Boolean)

        fun showFullscreen(video: Video)

        fun showSmallScreen()

        fun pauseVideo()

        fun startVideo()

        fun stopVideo()

        fun getVideoId(): String

        fun getOwnerId(): String

        fun getVideoState(): Boolean?

        fun getVideoPosition(): Long?

        fun showRecommendations()

        fun showLoadError()

        fun enterPipMode()

        fun exitPipMode()

        fun isPipMode(): Boolean

        fun hideUi()

        fun showUi()

        fun stopAudioFocusListener()

        fun startAudioFocusListener()

        fun showProgress()

        fun hideProgress()

        fun showPlayer()

        fun hidePlayer()

        fun setPlayerFullscreen()

        fun setPlayerNormal()

        fun setLiked()

        fun setUnliked()

        fun showShareDialog()

        fun hideShareDialog()

        fun showSendDialog()

        fun hideSendDialog()

        fun setAdded()

        fun setDeleted()

        fun showOwnerUser(owner: Owner)

        fun showOwnerGroup(owner: Owner)
    }

    interface Presenter {

        var view: View?

        fun onClick(id: Int)

        fun onCreate()

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

        fun liked()

        fun share()

        fun send()

        fun ownerClicked()
    }
}