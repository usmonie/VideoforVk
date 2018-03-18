package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.ResponseVideo
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
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
        view?.let { view ->
            loadVideo(view.getVideoId())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        view?.startAudioFocusListener()
    }

    override fun loadVideo(id: String) {
        view?.let { view ->
            view.showProgress()
            videoRepository
                .getVideo(id)
                .enqueue(object : Callback<ResponseVideo> {
                    override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                        view.showLoadError()
                        view.hideProgress()
                    }

                    override fun onResponse(
                        call: Call<ResponseVideo>?,
                        response: Response<ResponseVideo>?
                    ) {
                        response?.body()?.let { responseVideo ->
                            when {
                                responseVideo.items.isNotEmpty() -> {
                                    video = responseVideo.items[0]
                                    view.showVideo(video)
                                }

                                else -> view.showLoadError()
                            }

                            when {
                                responseVideo.groups != null && responseVideo.groups!!.isNotEmpty() -> {
                                    view.showGroupOwnerInfo(responseVideo.groups!![0])
                                    view.hideProgress()
                                    view.showUi()
                                    view.showPlayer()
                                    responseVideo.groups?.forEach {
                                        videoRepository.saveOwner(it)
                                    }
                                    videoRepository.saveOwnerId(responseVideo.groups!![0].id)

                                }
                                responseVideo.profiles != null && responseVideo.profiles!!.isNotEmpty() -> {
                                    responseVideo.profiles?.forEach {
                                        videoRepository.saveOwner(it)
                                    }
                                    loadUser(responseVideo.profiles!![0])
                                }
                            }

                        }
                    }

                })
        }
    }

    private fun loadUser(user: User) {
        view?.let { view ->
            userRepository
                .getUsers(user.id.toString())
                /*.observe(view, Observer {
                    it?.let { users ->
                        if (users.isNotEmpty()) {
                            view.showUserOwnerInfo(users[0])
                            view.hideProgress()
                            view.showUi()
                            view.showPlayer()
                        }
                    }
                })*/
                .enqueue(object : Callback<ApiResponse<List<User>>> {
                    override fun onFailure(
                        call: Call<ApiResponse<List<User>>>?,
                        t: Throwable?
                    ) {
                        Log.e("error", t?.message)

                        view.hideProgress()
                        view.showLoadError()
                    }

                    override fun onResponse(
                        call: Call<ApiResponse<List<User>>>?,
                        response: Response<ApiResponse<List<User>>>?
                    ) {
                        view.hideProgress()
                        response?.body()?.response?.get(0)?.let { user ->
                            view.showUserOwnerInfo(user)
                            view.hideProgress()
                            view.showUi()
                            view.showPlayer()
                            videoRepository.saveOwnerId(user.id)
                        }
                    }
                })
        }

    }

    override fun clickFullscreen() {
        view?.let { view ->
            isFullscreen = when (isFullscreen) {
                true -> {
                    view.showSmallScreen()
                    view.setPlayerNormal()
                    false
                }
                false -> {
                    view.showFullscreen(video)
                    view.setPlayerFullscreen()
                    true
                }
            }
        }

    }

    override fun changedPipMode() {
        view?.let { view ->
            if (view.isPipMode()) {
                view.setPlayerFullscreen()
                view.hideUi()
            } else {
                view.setPlayerNormal()
                view.showUi()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        view?.let { view ->
            view.pauseVideo()
            view.getVideoState()?.let { isStartedVideo -> isStarted = isStartedVideo }
            view.getVideoPosition()?.let { videoPosition -> position = videoPosition }
            view.stopAudioFocusListener()
        }
    }

    override fun onPresenterDestroy() {
        view?.stopVideo()
        super.onPresenterDestroy()
    }

    override fun error(error: Error, message: String) {
    }

    override fun pipToggleButton() {
        view?.hideUi()
        view?.enterPipMode()
    }

    override fun liked() {
    }

    override fun share() {
    }

    override fun send() {
    }

    override fun ownerClicked() {
        view?.let { view ->
            videoRepository
                .getOwner()
                .observe(view, Observer { owner -> owner?.let { view.showOwnerGroup(it) } })
        }

    }
}