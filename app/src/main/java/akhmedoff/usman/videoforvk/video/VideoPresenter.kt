package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class VideoPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<VideoContract.View>(), VideoContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        view?.let {
            loadVideo(it.getVideoId())
        }
    }

    override fun loadVideo(id: String) {
        view?.let {
            videoRepository
                .getVideo(id)
                .observe(it, Observer { response ->
                    when {
                        response?.response != null -> {
                            when {
                                response.response.groups != null -> it.showGroupOwnerInfo(
                                    response.response.groups[0]
                                )
                                response.response.profiles != null -> it.showUserOwnerInfo(
                                    response.response.profiles[0]
                                )
                            }
                            it.showVideo(response.response.items[0])
                        }
                    }
                })
        }

    }

    override fun clickFullscreen() {
    }

    override fun clickSmallScreen() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        view?.pauseVideo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        view?.stopVideo()
    }
}