package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class UserPresenter(
    private val userRepository: UserRepository,
    videoRepository: VideoRepository
) : OwnerPresenter(videoRepository) {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        view?.let { view ->
            userRepository.getUsers(view.getOwnerId())
                .observe(view, Observer {
                    it?.let {
                        if (it.isNotEmpty()) {
                            view.showOwnerInfo(it[0])
                            loadVideos(it[0].id.toString())
                            loadAlbums(it[0].id.toString())
                        }
                    }
                })

            /*.enqueue(object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>?, t: Throwable?) {

                }

                override fun onResponse(
                    call: Call<List<User>>?,
                    response: Response<List<User>>?
                ) {

                    response?.body()?.let {
                        if (it.isNotEmpty()) {
                            view.showOwnerInfo(it[0])
                            loadVideos(it[0].id.toString())
                            loadAlbums(it[0].id.toString())
                        }
                    }
                }
            })*/
        }
    }
}