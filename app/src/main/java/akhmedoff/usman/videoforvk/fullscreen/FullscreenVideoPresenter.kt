package akhmedoff.usman.videoforvk.fullscreen

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.ResponseVideo
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FullscreenVideoPresenter(
    private val videoRepository: VideoRepository
) :
    BasePresenter<FullscreenVideoContract.View>(), FullscreenVideoContract.Presenter {
    private var isFullscreen = false

    private var isStarted = false

    private var position = 0L

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view?.let {
            loadVideo(it.getVideoId())
        }
    }

    override fun loadVideo(id: String) {
        view?.let {
            videoRepository
                .getVideo(id)
                .enqueue(object : Callback<ResponseVideo> {
                    override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                        it.showLoadError()
                    }

                    override fun onResponse(
                        call: Call<ResponseVideo>?,
                        response: Response<ResponseVideo>?
                    ) {
                        response?.body()?.let { responseVideo ->
                            when {
                                responseVideo.items.isNotEmpty() ->
                                    it.showVideo(responseVideo.items[0])

                                else -> it.showLoadError()
                            }
                        }
                    }

                })
        }
    }

    override fun clickSmallScreen() {
        view?.showSmallScreen()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        view?.let {
            it.pauseVideo()
            it.getVideoState()?.let { isStartedVideo -> isStarted = isStartedVideo }
            it.getVideoPosition()?.let { videoPosition -> position = videoPosition }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        view?.stopVideo()
    }

    override fun error(error: Error, message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}