package akhmedoff.usman.thevt.ui.main

import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.thevt.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(
        private val userRepository: UserRepository,
        override var view: MainContract.View?
) : MainContract.Presenter {

    override fun onCreate() {
        if (!userRepository.hasCurrentUser()) loadUser()
        view?.showHome()
    }

    override fun onRecreate() {
        view?.showLastFragment()
    }

    override fun forwardTo(id: Int) {
        when (id) {
            R.id.navigation_home -> view?.showHome()

            R.id.navigation_looking -> view?.showExplore()

            R.id.navigation_person -> view?.showProfile()
        }
    }

    private fun loadUser() = userRepository.getUsers()
            .enqueue(object : Callback<ApiResponse<List<User>>> {
                override fun onFailure(call: Call<ApiResponse<List<User>>>?, t: Throwable?) {
                }

                override fun onResponse(
                        call: Call<ApiResponse<List<User>>>?,
                        response: Response<ApiResponse<List<User>>>?
                ) {
                    response?.body()?.response?.get(0)?.let {
                        userRepository.saveUser(it)
                        userRepository.saveCurrentUser(it.id)
                    }
                }

            })

    override fun getUserId(): String {
        return userRepository.getCurrentUser() ?: ""
    }
}