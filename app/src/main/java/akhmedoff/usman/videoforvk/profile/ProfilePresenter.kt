package akhmedoff.usman.videoforvk.profile

import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.repository.UserRepository
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(
        override var view: ProfileContract.View? = null,
        private val userRepository: UserRepository
) : ProfileContract.Presenter {

    override fun onCreated() {
        view?.showPages(userRepository.getCurrentUser() ?: "")
    }

    override fun onDestroyed() {
        view = null
    }

    override fun onViewCreated() {
        view?.showLoading(true)
        view?.hideTabs()

        userRepository
                .getUsers(view?.getUserId())
                .enqueue(object : Callback<ApiResponse<List<User>>> {
                    override fun onFailure(
                            call: Call<ApiResponse<List<User>>>?,
                            t: Throwable?
                    ) {
                        view?.showLoading(false)
                        val name = userRepository.getUserName()

                        if (name != null) {
                            view?.showUserName(name)
                            userRepository.getUserPhotoUrl()?.let { view?.showUserPhoto(it) }

                            view?.showTabs()
                        } else {
                            t?.message?.let {
                                view?.showError(it)
                                Log.e(javaClass.simpleName, it)
                            }
                            view?.hideTabs()
                        }
                    }

                    override fun onResponse(
                            call: Call<ApiResponse<List<User>>>?,
                            response: Response<ApiResponse<List<User>>>?
                    ) {
                        view?.showLoading(false)

                        val get = response?.body()?.response?.get(0)
                        if (get != null) get.let { user ->
                            userRepository.saveUser(user)

                            view?.showUserName("${user.firstName} ${user.lastName}")
                            view?.showUserPhoto(user.photoMaxOrig)
                            view?.showTabs()
                        } else {
                            val name = userRepository.getUserName()

                            if (name != null) {
                                view?.showUserName(name)
                                userRepository.getUserPhotoUrl()?.let { view?.showUserPhoto(it) }

                                view?.showTabs()
                            } else {
                                view?.hideTabs()
                            }
                        }
                    }
                })
    }

    override fun onSearchClicked() {
        view?.startSearch()
    }
}