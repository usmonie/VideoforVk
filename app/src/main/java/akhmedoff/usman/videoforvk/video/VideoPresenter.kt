package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.Quality.*
import akhmedoff.usman.data.model.ResponseVideo
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoPresenter(
    override var view: VideoContract.View?,
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository
) : VideoContract.Presenter {

    private lateinit var video: Video

    override fun onCreate() {
    }

    override fun onStart() {
        view?.hideUi()
        view?.let { view ->
            loadVideo("${view.getOwnerId()}_${view.getVideoId()}")
        }
    }

    override fun onResume() {
        view?.startAudioFocusListener()
        view?.setVideoPosition(view?.loadVideoPosition() ?: 0)
    }

    override fun onPause() {
        view?.stopAudioFocusListener()
        view?.pauseVideo()
        view?.saveVideoPosition(view?.getVideoPosition() ?: 0)
    }

    override fun onClick(id: Int) {
    }

    override fun changeQuality() {
        view?.saveVideoPosition(view?.getVideoPosition() ?: 0)

        view?.let {
            if (video.files.size - 1 > it.getCurrentQuality()) {
                changeQuality(it.getCurrentQuality() + 1)
            } else {
                changeQuality(video.files.size - it.getCurrentQuality())
            }

        }


    }

    private fun changeQuality(index: Int) {
        view?.setQuality(video.files[index])

        view?.saveCurrentQuality(index)

        view?.setVideoPosition(view?.loadVideoPosition() ?: 0)
    }

    override fun loadVideo(id: String) {
        view?.showProgress()
        videoRepository
            .getVideo(id)
            .enqueue(object : Callback<ResponseVideo> {
                override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                    view?.showLoadError()
                    view?.hideProgress()
                }

                override fun onResponse(
                    call: Call<ResponseVideo>?,
                    response: Response<ResponseVideo>?
                ) {
                    response?.body()?.let { responseVideo ->
                        when {
                            responseVideo.items.isNotEmpty() -> {
                                video = responseVideo.items[0]
                                showVideo(video)
                            }

                            else -> view?.showLoadError()
                        }

                        when {
                            responseVideo.groups != null && responseVideo.groups!!.isNotEmpty() -> {
                                view?.showOwnerInfo(responseVideo.groups!![0])
                                view?.hideProgress()
                                view?.showUi()
                                view?.showPlayer()
                                responseVideo.groups?.forEach {
                                    videoRepository.saveOwner(it)
                                }
                                view?.let { it.setVideoPosition(it.loadVideoPosition()) }
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

    private fun showVideo(video: Video) {
        video.files.forEachIndexed { index, videoUrl ->
            when (videoUrl.quality) {
                HLS -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                FULLHD -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                HD -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                qHD -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                P360 -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                P240 -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                LOW -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
                }
                EXTERNAL -> {
                    view?.setExternalUi(videoUrl)
                }
            }
        }
    }

    private fun loadUser(user: User) = userRepository
        .getUsers(user.id.toString())
        .enqueue(object : Callback<ApiResponse<List<User>>> {
            override fun onFailure(
                call: Call<ApiResponse<List<User>>>?,
                t: Throwable?
            ) {
                Log.e("error", t?.message)

                view?.hideProgress()
                view?.showLoadError()
            }

            override fun onResponse(
                call: Call<ApiResponse<List<User>>>?,
                response: Response<ApiResponse<List<User>>>?
            ) {
                view?.hideProgress()
                response?.body()?.response?.get(0)?.let { user ->
                    view?.showOwnerInfo(user)
                    view?.hideProgress()
                    view?.showUi()
                    view?.showPlayer()
                    videoRepository.saveOwnerId(user.id)
                }
            }
        })

    override fun clickFullscreen() {
        view?.saveIsFullscreen(
            when (view?.loadIsFullscreen() == true) {
                true -> {
                    view?.showSmallScreen()
                    view?.setPlayerNormal()
                    false
                }
                false -> {
                    view?.showFullscreen(video)
                    view?.setPlayerFullscreen()
                    true
                }
            }
        )

    }

    override fun changedPipMode() {
        if (view?.isPipMode() == true) {
            view?.setPlayerFullscreen()
            view?.hideUi()
        } else {
            view?.setPlayerNormal()
            view?.showUi()
        }
    }

    override fun onStop() {
        view?.pauseVideo()
        view?.getVideoState()?.let { isStartedVideo -> view?.saveVideoState(isStartedVideo) }
        view?.getVideoPosition()?.let { videoPosition -> view?.saveVideoPosition(videoPosition) }
        view?.stopAudioFocusListener()
    }

    override fun onDestroyView() {
        view?.stopAudioFocusListener()
        view?.stopVideo()
        view = null
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
            videoRepository.getOwner()
                .observe(view, Observer { owner -> owner?.let { view.showOwnerGroup(it) } })
        }

    }
}