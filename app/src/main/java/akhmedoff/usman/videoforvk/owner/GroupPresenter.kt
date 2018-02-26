package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.model.Group
import akhmedoff.usman.data.repository.GroupRepository
import akhmedoff.usman.data.repository.VideoRepository
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupPresenter(
    private val groupRepository: GroupRepository,
    videoRepository: VideoRepository
) : OwnerPresenter(videoRepository) {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        view?.let { view ->
            groupRepository.getGroup(view.getOwnerId()).enqueue(object : Callback<List<Group>> {
                override fun onFailure(call: Call<List<Group>>?, t: Throwable?) {
                    Log.d("failure", t.toString())
                }

                override fun onResponse(
                    call: Call<List<Group>>?,
                    response: Response<List<Group>>?
                ) {

                    response?.body()?.let {
                        if (it.isNotEmpty()) {
                            Log.d("response", it[0].name)
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