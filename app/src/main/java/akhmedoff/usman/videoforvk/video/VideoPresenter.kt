package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class VideoPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<VideoContract.View>(), VideoContract.Presenter {

    override fun loadVideo(id: String) {
        view?.let {
            videoRepository
                .getVideo(id)
                .observe(view!!, Observer {
                    when {
                        it?.response != null -> {
                            view!!.showVideo(it.response.items[0])
                            when {
                                it.response.groups != null && it.response.groups.isNotEmpty() -> view!!.showGroupOwnerInfo(
                                    it.response.groups[0]
                                )
                                it.response.profiles != null && it.response.profiles.isNotEmpty() -> view!!.showUserOwnerInfo(
                                    it.response.profiles[0]
                                )
                            }
                        }
                    }
                })
        }
    }

    override fun clickFullscreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clickSmallScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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