package akhmedoff.usman.videoforvk.home

import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import android.arch.lifecycle.Observer

class HomePresenter(private val videoRepository: VideoRepository) : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun onPresenterCreated() {
        super.onPresenterCreated()
        videoRepository.getVideos().observe(view, Observer { response ->
            when {
                response != null
                        && response.isSuccessfull
                        && response.response != null
                        && response.response.count > 0 ->
                    view.showList(response.response.items!!)
                else -> {

                }
            }
        })
    }

    override fun clickItem(id: Int) {
    }
}