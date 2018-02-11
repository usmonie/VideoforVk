package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Group
import akhmedoff.usman.videoforvk.model.User
import akhmedoff.usman.videoforvk.model.Video

interface VideoContract {

    interface View : BaseContract.View {
        var videoPresenter: VideoPresenter

        fun showGroupOwnerInfo(group: Group)

        fun showUserOwnerInfo(user: User)

        fun showVideo(item: Video)

        fun setSaved(saved: Boolean)

        fun showFullscreen()

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

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadVideo(id: String)

        fun clickFullscreen()

        fun error(error: Error, message: String)
    }
}