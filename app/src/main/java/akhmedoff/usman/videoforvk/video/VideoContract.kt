package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Group
import akhmedoff.usman.data.model.Owner
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.base.BaseContract

interface VideoContract {

    interface View : BaseContract.View {
        var videoPresenter: VideoPresenter

        fun showGroupOwnerInfo(group: Group)

        fun showUserOwnerInfo(user: User)

        fun showVideo(item: Video)

        fun setSaved(saved: Boolean)

        fun showFullscreen(video: Video)

        fun showSmallScreen()

        fun pauseVideo()

        fun resumeVideo(state: Boolean, position: Long)

        fun startVideo()

        fun stopVideo()

        fun getVideoId(): String

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

    interface Presenter : BaseContract.Presenter<View> {
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