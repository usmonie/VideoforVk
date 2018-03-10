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
) :
    ProfileContract.Presenter {

    override fun onCreated() {
        view?.showPages(userRepository.getCurrentUser().toString())
    }

    override fun onViewCreated() {
        view?.let { view ->
            view.showLoading(true)
            view.hideTabs()

            userRepository
                .getUsers(view.getUserId())
                .enqueue(object : Callback<ApiResponse<List<User>>> {
                    override fun onFailure(
                        call: Call<ApiResponse<List<User>>>?,
                        t: Throwable?
                    ) {
                        view.showLoading(false)
                        view.hideTabs()
                        t?.message?.let {
                            view.showError(it)
                            Log.d(javaClass.simpleName, it)
                        }
                    }

                    override fun onResponse(
                        call: Call<ApiResponse<List<User>>>?,
                        response: Response<ApiResponse<List<User>>>?
                    ) {
                        view.showLoading(false)

                        val get = response?.body()?.response?.get(0)
                        if (get != null) get.let { user ->
                            view.showUserName("${user.firstName} ${user.lastName}")
                            view.showUserPhoto(user.photoMaxOrig)
                            view.showTabs()
                        } else {
                            view.hideTabs()
                            response?.message()?.let { view.showError(it) }
                        }
                    }
                })
        }
    }

    override fun onSearchClicked() {
        view?.startSearch()
    }

}