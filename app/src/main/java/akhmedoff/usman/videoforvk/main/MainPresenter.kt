package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BasePresenter
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val userRepository: UserRepository) :
    BasePresenter<MainContract.View>(), MainContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (!userRepository.hasCurrentUser())
            loadUser()
        view?.showHome()
    }

    override fun forwardTo(id: Int) {
        view?.hidePrevious()
        when (id) {
            R.id.navigation_home -> {
                view?.showHome()
            }

            R.id.navigation_looking -> {
                view?.showLooking()
            }

            R.id.navigation_person -> {
                view?.showProfile()
            }
        }
    }

    private fun loadUser() {
        userRepository
            .getUsers()
            .enqueue(object : Callback<ApiResponse<List<User>>> {
                override fun onFailure(call: Call<ApiResponse<List<User>>>?, t: Throwable?) {
                }

                override fun onResponse(
                    call: Call<ApiResponse<List<User>>>?,
                    response: Response<ApiResponse<List<User>>>?
                ) {
                    response?.body()?.response?.get(0)?.let {
                        userRepository.saveCurrentUser(it.id)
                    }
                }

            })
    }
}