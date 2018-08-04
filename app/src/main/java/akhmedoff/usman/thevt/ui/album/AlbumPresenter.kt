package akhmedoff.usman.thevt.ui.album

import akhmedoff.usman.data.Error
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.ApiResponse
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.AlbumRepository
import android.util.Log
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumPresenter(private var view: AlbumContract.View?, private val albumRepository: AlbumRepository) :
        AlbumContract.Presenter {

    override fun error(error: Error, message: String) {

    }

    override fun onCreated() {
        loadAlbum()
        loadAlbumVideos()
    }

    private fun loadAlbumVideos() {
        view?.let { view ->
            albumRepository.getVideos(
                    ownerId = view.getAlbumOwnerId()?.toInt(),
                    videos = null,
                    albumId = view.getAlbumId()?.toInt()
            ).observe(view, Observer { pagedList: PagedList<Video>? ->
                pagedList?.let { items -> view.showVideos(items) }
            })
        }
    }

    private fun loadAlbum() {
        view?.let { view ->
            albumRepository
                    .getAlbum(
                            ownerId = view.getAlbumOwnerId()?.toInt(),
                            albumId = view.getAlbumId()?.toInt()
                    ).enqueue(object : Callback<ApiResponse<Album>?> {
                        override fun onFailure(call: Call<ApiResponse<Album>?>?, t: Throwable?) {
                            Log.e("error", t?.message)
                        }

                        override fun onResponse(call: Call<ApiResponse<Album>?>?, response: Response<ApiResponse<Album>?>?) {
                            response?.body()?.response?.let { album ->
                                view.showAlbumImage(when {
                                    album.photo130 != null -> album.photo130!!
                                    album.photo320 != null -> album.photo320!!
                                    album.photo640 != null -> album.photo640!!
                                    album.photo800 != null -> album.photo800!!
                                    else -> ""
                                })
                                view.showAlbumTitle(view.getAlbumTitle() ?: album.title)
                            }
                        }
                    })
        }
    }

    override fun clickVideo(video: Video) {
    }

    override fun clickAdd() {
    }
}