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
import java.util.concurrent.CountDownLatch

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
        val countDownLatch = CountDownLatch(3)
        view?.showUi(false)
        view?.let { view ->
            view.showLoading(countDownLatch.count > 0)
            albumRepository
                    .getAlbums(view.getUserId())
                    .observe(view, Observer { pagedList ->
                        countDownLatch.countDown()
                        when {
                            pagedList != null && pagedList.size > 0 -> {
                                view.showAlbums(pagedList)
                                view.showLoading(countDownLatch.count > 0)
                            }
                        }
                    })
            videoRepository.getFaveVideos().observe(view, Observer { pagedList ->
                countDownLatch.countDown()
                when {
                    pagedList != null && pagedList.size > 0 -> {
                        view.showFaveVideos(pagedList)
                        view.showLoading(countDownLatch.count > 0)
                    }
                }
            })
            videoRepository
                    .getVideos(view.getUserId()?.toInt())
                    .observe(view, Observer { pagedList ->
                        countDownLatch.countDown()
                        when {
                            pagedList != null && pagedList.size > 0 -> {
                                view.showVideos(pagedList)
                                view.showLoading(countDownLatch.count > 0)
                            }
                        }
                    })


            view.showLoading(countDownLatch.count > 0)
            view.showUi(countDownLatch.count > 0)
        }

    }
}