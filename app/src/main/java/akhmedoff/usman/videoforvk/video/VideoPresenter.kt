package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.content.res.Configuration

class VideoPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<VideoContract.View>(), VideoContract.Presenter {
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
                .observe(it, Observer { response ->
                    response?.response?.let { responseVideo ->
                        when {
                            responseVideo.groups != null && responseVideo.groups.isNotEmpty() -> it.showGroupOwnerInfo(
                                responseVideo.groups[0]
                            )
                            responseVideo.profiles != null && responseVideo.profiles.isNotEmpty() -> it.showUserOwnerInfo(
                                responseVideo.profiles[0]
                            )
                        }
                        when {
                            responseVideo.items.isNotEmpty() ->
                                it.showVideo(responseVideo.items[0])

                            else -> it.showLoadError()
                        }
                    }
                })
        }
    }

    override fun clickFullscreen() {
        isFullscreen = when (isFullscreen) {
            true -> {
                view?.showSmallScreen()
                false
            }
            false -> {
                view?.showFullscreen()
                true
            }
        }
    }

    override fun changedConfiguration(newConfig: Configuration) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && isFullscreen)
            view?.showFullscreen()
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
}