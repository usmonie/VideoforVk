package akhmedoff.usman.thevt.ui.profile

import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.repository.AlbumRepository
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Observer
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(
        override var view: ProfileContract.View? = null,
        private val userRepository: UserRepository,
        private val videoRepository: VideoRepository,
        private val albumRepository: AlbumRepository
) : ProfileContract.Presenter {

    override fun onCreated() {
    }

    override fun onDestroyed() {
        view = null
    }

    override fun onViewCreated() {
        userRepository
                .getUsers(view?.getUserId())
                .enqueue(object : Callback<ApiResponse<List<User>>> {
                    override fun onFailure(
                            call: Call<ApiResponse<List<User>>>?,
                            t: Throwable?
                    ) {
                        val name = userRepository.getUserName()

                        if (name != null) {
                            view?.showUserName(name)
                            userRepository.getUserPhotoUrl()?.let { view?.showUserPhoto(it) }

                        } else {
                            t?.message?.let {
                                view?.showLoadingError()
                                Log.e(javaClass.simpleName, it)
                            }
                        }
                    }

                    override fun onResponse(
                            call: Call<ApiResponse<List<User>>>?,
                            response: Response<ApiResponse<List<User>>>?
                    ) {
                        val get = response?.body()?.response?.get(0)
                        if (get != null) get.let { user ->
                            userRepository.saveUser(user)

                            view?.showUserName("${user.firstName} ${user.lastName}")

                            view?.showUserPhoto(user.photoMaxOrig)
                        } else {
                            val name = userRepository.getUserName()

                            if (name != null) {
                                view?.showUserName(name)
                                userRepository.getUserPhotoUrl()?.let {
                                    view?.showUserPhoto(it)
                                }
                            }
                        }
                    }
                })

        refresh()
    }

    override fun refresh() {
        var albumsLoading = true
        var videosLoading = true

        view?.let { view ->
            view.showLoading(albumsLoading && videosLoading)
            albumRepository
                    .getAlbums(view.getUserId())
                    .observe(view, Observer { pagedList ->
                        when {
                            pagedList != null && pagedList.size > 0 -> {
                                view.showAlbums(pagedList)
                                albumsLoading = false
                                view.showLoading(albumsLoading && videosLoading)
                            }
                            pagedList == null -> view.showLoadingError()
                        }
                    })
            videoRepository
                    .getVideos(view.getUserId()?.toInt())
                    .observe(view, Observer { pagedList ->
                        view.showLoading(false)
                        when {
                            pagedList != null && pagedList.size > 0 -> {
                                view.showVideos(pagedList)
                                videosLoading = false
                                view.showLoading(albumsLoading && videosLoading)
                            }

                            pagedList == null -> view.showLoadingError()
                        }
                    })
        }

    }
}