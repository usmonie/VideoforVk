package akhmedoff.usman.videoforvk.video

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.model.Quality.*
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.data.utils.gson
import akhmedoff.usman.videoforvk.R
import android.arch.lifecycle.Observer
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoPresenter(
    override var view: VideoContract.View?,
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository
) : VideoContract.Presenter {
    override fun addToAlbums(albumsIds: List<Int>) {
    }

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

    override fun onClick(itemView: Int) {
        when (itemView) {
            R.id.like_button ->
                if (video.likes?.userLikes == false)
                    likeCurrentVideo()
                else unlikeCurrentVideo()

            R.id.share_button -> shareCurrentVideo()
            R.id.add_button -> {
                view?.showAddDialog()

            }
        }
    }

    private fun addCurrentVideo() {

    }

    private fun shareCurrentVideo() {
        view?.let { view ->
            view.showShareDialog(
                view.getString(
                    R.string.shared_with_vt,
                    video.title,
                    "https://vk.com/video?z=video${video.ownerId}_${video.id}"
                )
            )
        }
    }

    private fun likeCurrentVideo() {
        likeVideo(video.ownerId.toString(), video.id.toString(), null, null)
    }

    private fun likeVideo(
        ownerId: String?,
        itemId: String,
        captchaSid: String?,
        captchaCode: String?
    ) {
        videoRepository
            .likeVideo(ownerId, itemId, captchaSid, captchaCode)
            .enqueue(object : Callback<ApiResponse<Likes>> {
                override fun onFailure(call: Call<ApiResponse<Likes>>?, t: Throwable?) {
                    video.likes?.let { view?.setUnliked(it) }
                }

                override fun onResponse(
                    call: Call<ApiResponse<Likes>>?,
                    response: Response<ApiResponse<Likes>>?
                ) {
                    response?.body()?.let {
                        video.likes?.userLikes = true
                        video.likes?.let { view?.setLiked(it) }

                    }
                    response?.errorBody()?.let {
                        errorConvert(it)
                        video.likes?.let { view?.setUnliked(it) }
                    }

                }

            })
    }

    private fun unlikeCurrentVideo() {
        unlikeVideo(video.ownerId.toString(), video.id.toString(), null, null)
    }

    private fun unlikeVideo(
        ownerId: String?,
        itemId: String,
        captchaSid: String?,
        captchaCode: String?
    ) {
        videoRepository
            .unlikeVideo(ownerId, itemId, captchaSid, captchaCode)
            .enqueue(object : Callback<ApiResponse<Likes>> {
                override fun onFailure(call: Call<ApiResponse<Likes>>?, t: Throwable?) {
                    video.likes?.let { view?.setLiked(it) }
                }

                override fun onResponse(
                    call: Call<ApiResponse<Likes>>?,
                    response: Response<ApiResponse<Likes>>?
                ) {
                    response?.body()?.let {
                        video.likes?.userLikes = false
                        video.likes?.let { view?.setUnliked(it) }
                    }
                    response?.errorBody()?.let {
                        errorConvert(it)
                        video.likes?.let { view?.setLiked(it) }
                    }

                }

            })
    }

    override fun changeQuality() {
        view?.saveVideoPosition(view?.getVideoPosition() ?: 0)

        view?.let { view ->
            if (video.files.size - 1 > view.getCurrentQuality()) {
                changeQuality(view.getCurrentQuality() + 1)
            } else {
                changeQuality(video.files.size - view.getCurrentQuality())
            }
        }
    }

    private fun changeQuality(index: Int) {
        view?.setQuality(video.files[index])

        view?.saveCurrentQuality(index)

        view?.setVideoPosition(view?.loadVideoPosition() ?: 0)
    }

    private fun errorConvert(response: ResponseBody) {
        val auth = gson.fromJson<Auth>(response.string(), Auth::class.java)

        when (auth.error) {
            Error.NEED_CAPTCHA -> {
                view?.saveCaptchaSid(auth.captchaSid!!)
                view?.showCaptcha(auth.captchaImg!!)
            }

            else -> {
            }
        }
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
                                view?.let { it.setVideoPosition(it.loadVideoPosition()) }
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
        view?.showVideo(video)
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
                    view?.showFullscreen()
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
        view?.enterPipMode(video)
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

    override fun enterCaptcha(captchaCode: String) {
        view?.let {
            if (captchaCode.trim().isEmpty()) return

            videoRepository.likeVideo(
                video.ownerId.toString(),
                video.id.toString(),
                it.loadCaptchaSid(),
                captchaCode
            )
        }
    }

    override fun onBackListener() {
        if (view?.loadIsFullscreen() == true) {
            clickFullscreen()
        } else {
            view?.back()
        }
    }
}