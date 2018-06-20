package akhmedoff.usman.videoforvk.ui.video

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.model.Quality.EXTERNAL
import akhmedoff.usman.data.repository.AlbumRepository
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
        private val userRepository: UserRepository,
        private val albumRepository: AlbumRepository
) : VideoContract.Presenter {

    private lateinit var video: Video

    override fun onStart() {
        view?.showUi(false)
        view?.let { view ->
            loadVideo("${view.getOwnerId()}_${view.getVideoId()}")
        }
    }

    override fun onResume() {
        view?.setVideoPosition(view?.loadVideoPosition() ?: 0)
    }

    override fun onPause() {
        view?.pauseVideo()
        view?.saveVideoPosition(view?.getVideoPosition() ?: 0)
    }

    override fun addToAlbums(albumsIds: MutableList<Album>) {
        val ids = mutableListOf<Int>()
        albumsIds.forEach {
            ids.add(it.id)
        }

        videoRepository
                .addToAlbum(
                        albumIds = ids,
                        ownerId = video.ownerId.toString(),
                        videoId = video.id.toString())
                .enqueue(object : Callback<ApiResponse<Int>> {
                    override fun onFailure(call: Call<ApiResponse<Int>>?,
                                           t: Throwable?) {
                    }

                    override fun onResponse(
                            call: Call<ApiResponse<Int>>?,
                            response: Response<ApiResponse<Int>>?
                    ) {
                        view?.hideAddDialog()
                    }
                })
    }

    private fun getAlbumsByVideo(videoId: String, ownerId: String) =
            albumRepository
                    .getAlbumsByVideo(videoId = videoId, ownerId = ownerId)
                    .enqueue(object : Callback<ApiResponse<List<Int>>> {
                        override fun onResponse(
                                call: Call<ApiResponse<List<Int>>>?,
                                response: Response<ApiResponse<List<Int>>>?
                        ) {
                            response?.body()?.response?.let {
                                view?.showSelectedAlbums(it)
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<List<Int>>>?,
                                               t: Throwable?) {
                        }

                    })


    override fun onClick(itemView: Int) {
        when (itemView) {
            R.id.like_button ->
                if (video.likes?.userLikes == false)
                    likeCurrentVideo()
                else unlikeCurrentVideo()

            R.id.share_button -> shareCurrentVideo()

            R.id.add_to_videos -> addToMyVideos()

            R.id.add_to_album -> {
                view?.showAddDialog()
                loadAlbums()
            }
            R.id.delete_from_videos -> deleteVideo()
        }
    }

    private fun deleteVideo() = videoRepository
            .deleteVideo(ownerId = video.ownerId.toString(), videoId = video.id.toString())
            .enqueue(object : Callback<ApiResponse<Int>?> {
                override fun onFailure(call: Call<ApiResponse<Int>?>?,
                                       t: Throwable?) {

                }

                override fun onResponse(call: Call<ApiResponse<Int>?>?,
                                        response: Response<ApiResponse<Int>?>?) {
                    if (response?.body()?.response == 1) {
                        view?.setDeleted()
                    }
                }
            })

    private fun addToMyVideos() = videoRepository
            .addToUser(
                    ownerId = video.ownerId.toString(),
                    videoId = video.id.toString())
            .enqueue(object : Callback<ApiResponse<Int>> {
                override fun onFailure(call: Call<ApiResponse<Int>>?,
                                       t: Throwable?) {
                }

                override fun onResponse(
                        call: Call<ApiResponse<Int>>?,
                        response: Response<ApiResponse<Int>>?
                ) {
                    view?.hideAddDialog()
                    view?.setAdded()
                }
            })

    private fun loadAlbums() =
            view?.let { view ->
                albumRepository
                        .getAlbums()
                        .observe(view, Observer { pagedList ->
                            if (pagedList != null && pagedList.isNotEmpty()) {
                                view.showAlbums(pagedList)
                                view.showAlbumsLoading(false)
                                getAlbumsByVideo(video.id.toString(),
                                        video.ownerId.toString())
                            }
                        })
            }

    private fun shareCurrentVideo() =
            view?.let { view ->
                view.showShareDialog(
                        video.title,
                        view.getString(
                                R.string.shared_with_vt,
                                video.title,
                                "https://vk.com/video?z=video${video.ownerId}_${video.id}"
                        ))
            }

    private fun likeCurrentVideo() =
            likeVideo(video.ownerId.toString(),
                    video.id.toString(),
                    null,
                    null
            )

    private fun likeVideo(
            ownerId: String?,
            itemId: String,
            captchaSid: String?,
            captchaCode: String?
    ) = videoRepository
            .likeVideo(ownerId, itemId, captchaSid, captchaCode)
            .enqueue(object : Callback<ApiResponse<Likes>> {
                override fun onFailure(call: Call<ApiResponse<Likes>>?, t: Throwable?) {
                    video.likes?.let { view?.setLiked(it) }
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
                        video.likes?.let { view?.setLiked(it) }
                    }

                }

            })

    private fun unlikeCurrentVideo() = unlikeVideo(video.ownerId.toString(),
            video.id.toString(), null, null)

    private fun unlikeVideo(
            ownerId: String?,
            itemId: String,
            captchaSid: String?,
            captchaCode: String?
    ) = videoRepository
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
                        video.likes?.let { view?.setLiked(it) }
                    }
                    response?.errorBody()?.let {
                        errorConvert(it)
                        video.likes?.let { view?.setLiked(it) }
                    }
                }
            })

    private fun isVideoLiked(userId: String? = null,
                             ownerId: String? = null,
                             itemId: String) =
            videoRepository.isLiked(userId, "video", ownerId, itemId).enqueue(object : Callback<ApiResponse<Liked>?> {

                override fun onFailure(call: Call<ApiResponse<Liked>?>?, t: Throwable?) {
                    view?.setLiked(Likes())
                }

                override fun onResponse(call: Call<ApiResponse<Liked>?>?, response: Response<ApiResponse<Liked>?>?) {
                    response?.body()?.response?.let {
                        view?.setLiked(likes = Likes().apply {
                            userLikes = it.liked == 1
                        })
                    }
                }
            })

    override fun changeQuality() {
        view?.saveVideoPosition(view?.getVideoPosition() ?: 0)

        view?.let { view ->
            changeQuality(
                    if (video.files.size - 1 > view.getCurrentQuality()) {
                        view.getCurrentQuality() + 1
                    } else {
                        video.files.size - view.getCurrentQuality()
                    }
            )

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
        view?.showProgress(true)
        videoRepository
                .getVideo(id)
                .enqueue(object : Callback<ResponseVideo> {
                    override fun onFailure(call: Call<ResponseVideo>?, t: Throwable?) {
                        view?.showProgress(false)
                        view?.showUi(false)
                        view?.showPlayer(false)
                        view?.showLoadError(true)
                    }

                    override fun onResponse(
                            call: Call<ResponseVideo>?,
                            response: Response<ResponseVideo>?
                    ) {
                        view?.showLoadError(false)
                        response?.body()?.let { responseVideo ->
                            when {
                                responseVideo.items.isNotEmpty() -> {
                                    video = responseVideo.items[0]
                                    showVideo(video)
                                    view?.showUi(true)
                                    view?.showPlayer(true)
                                }

                                else -> view?.showLoadError(true)
                            }

                            when {
                                responseVideo.groups != null
                                        && responseVideo.groups!!.isNotEmpty() -> {

                                    view?.showOwnerInfo(responseVideo.groups!![0])
                                    view?.showProgress(false)

                                    view?.let {
                                        it.setVideoPosition(it.loadVideoPosition())
                                    }

                                    responseVideo.groups?.forEach {
                                        videoRepository.saveOwner(it)
                                    }
                                    videoRepository
                                            .saveOwnerId(responseVideo.groups!![0].id)

                                }
                                responseVideo.profiles != null
                                        && responseVideo.profiles!!.isNotEmpty() -> {
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
                EXTERNAL -> view?.setExternalUi(videoUrl)
                else -> {
                    view?.setQuality(videoUrl)
                    view?.saveCurrentQuality(index)
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

                    view?.showProgress(false)
                }

                override fun onResponse(
                        call: Call<ApiResponse<List<User>>>?,
                        response: Response<ApiResponse<List<User>>>?
                ) {
                    view?.showProgress(false)
                    response?.body()?.response?.get(0)?.let { user ->
                        view?.showOwnerInfo(user)
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
        view?.showUi(
                if (view?.isPipMode() == true) {
                    view?.setPlayerFullscreen()
                    false
                } else {
                    view?.setPlayerNormal()
                    true
                }
        )
    }

    override fun onStop() {
        view?.pauseVideo()

        view?.getVideoState()?.let { isStartedVideo ->
            view?.saveVideoState(isStartedVideo)
        }
        view?.getVideoPosition()?.let { videoPosition ->
            view?.saveVideoPosition(videoPosition)
        }
    }

    override fun onDestroyView() {
        view?.stopVideo()
        view = null
    }

    override fun error(error: Error, message: String) {
    }

    override fun pipToggleButton() {
        view?.showUi(false)
        view?.enterPipMode(video)
    }

    override fun ownerClicked() {
        view?.let { view ->
            videoRepository.getOwner()
                    .observe(view, Observer { owner ->
                        owner?.let {
                            view.showOwnerGroup(it)
                        }
                    })
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

    override fun openBrowser() {
        view?.let { view ->
            view.showVideoInBrowser(
                    "https://vk.com/video?z=video${view.getOwnerId()}" +
                            "_${view.getVideoId()}"
            )
        }
    }

    override fun getVideo(): Video = video
}