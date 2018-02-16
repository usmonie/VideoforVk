package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.UserRepository
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.ResponseVideo
import akhmedoff.usman.videoforvk.model.User
import akhmedoff.usman.videoforvk.model.Video
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoPresenter(
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository
) :
    BasePresenter<VideoContract.View>(), VideoContract.Presenter {

    private var isFullscreen = false

    private var isStarted = false

    private var position = 0L
    private lateinit var video: Video

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view?.hideUi()
        view?.let {
            loadVideo(it.getVideoId())
        }
    }

    override fun loadVideo(id: String) {
        view?.let {
            it.showProgress()
            videoRepository
                .getVideo(id)
                .enqueue(object : Callback<ResponseVideo> {
                    override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                        it.showLoadError()
                        it.hideProgress()
                    }

                    override fun onResponse(
                        call: Call<ResponseVideo>?,
                        response: Response<ResponseVideo>?
                    ) {
                        response?.body()?.let { responseVideo ->
                            when {
                                responseVideo.items.isNotEmpty() -> {
                                    video = responseVideo.items[0]
                                    it.showVideo(video)
                                    it.hideProgress()
                                    it.showPlayer()
                                }

                                else -> it.showLoadError()
                            }

                            when {
                                responseVideo.groups != null && responseVideo.groups.isNotEmpty() -> {
                                    it.showGroupOwnerInfo(responseVideo.groups[0])
                                    it.hideProgress()
                                    it.showUi()
                                }
                                responseVideo.profiles != null && responseVideo.profiles.isNotEmpty() ->
                                    loadUser(responseVideo.profiles[0])
                            }

                        }
                    }

                })
        }
    }

    private fun loadUser(user: User) {
        view?.let {
            userRepository.getUser(user.id.toString()).enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    it.hideProgress()
                    Log.d("failure", t.toString())
                }

                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                    response?.body()?.let { user ->
                        it.showUserOwnerInfo(user)
                        it.hideProgress()
                        it.showUi()
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
                view?.showFullscreen(video)
                true
            }
        }
    }

    override fun changedPipMode() {
        view?.let { view ->
            when (view.isPipMode()) {
                true -> view.hideUi()
                false -> view.showUi()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        view?.let {
            it.pauseVideo()
            it.getVideoState()?.let { isStartedVideo -> isStarted = isStartedVideo }
            it.getVideoPosition()?.let { videoPosition -> position = videoPosition }
        }
    }

    override fun onPresenterDestroy() {
        view?.stopVideo()
        super.onPresenterDestroy()
    }

    override fun error(error: Error, message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pipToggleButton() {
        view?.hideUi()
        view?.enterPipMode()
    }
}