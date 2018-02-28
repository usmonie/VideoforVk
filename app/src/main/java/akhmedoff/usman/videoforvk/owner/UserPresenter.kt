package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPresenter(
    private val userRepository: UserRepository,
    videoRepository: VideoRepository
) : OwnerPresenter(videoRepository) {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        view?.let { view ->
            userRepository.getUsers(view.getOwnerId())
                /*   .observe(view, Observer {
                       it?.let {
                           if (it.isNotEmpty()) {
                               view.showOwnerInfo(it[0])
                               loadVideos(it[0].id.toString())
                               loadAlbums(it[0].id.toString())
                           }
                       }
                   })*/

                .enqueue(object : Callback<ApiResponse<List<User>>> {
                    override fun onFailure(call: Call<ApiResponse<List<User>>>?, t: Throwable?) {
                        Log.d("owner failure", t.toString())
                    }

                    override fun onResponse(
                        call: Call<ApiResponse<List<User>>>?,
                        response: Response<ApiResponse<List<User>>>?
                    ) {

                        response?.body()?.response?.let {
                            if (it.isNotEmpty()) {
                                view.showOwnerInfo(it[0])
                                loadVideos(it[0].id.toString())
                                loadAlbums(it[0].id.toString())
                            }
                        }
                    }
                })
        }
    }
}