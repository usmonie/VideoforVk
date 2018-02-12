package akhmedoff.usman.videoforvk.fullscreen

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BaseContract
import akhmedoff.usman.videoforvk.model.Video

interface FullscreenVideoContract {
    interface View : BaseContract.View {
        var videoPresenter: Presenter

        fun showVideo(item: Video)

        fun setSaved(saved: Boolean)

        fun showSmallScreen()

        fun pauseVideo()

        fun resumeVideo(state: Boolean, position: Long)

        fun startVideo()

        fun stopVideo()

        fun getVideoId(): String

        fun getVideoState(): Boolean?

        fun getVideoPosition(): Long?

        fun showLoadError()

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadVideo(id: String)

        fun clickSmallScreen()

        fun error(error: Error, message: String)
    }
}