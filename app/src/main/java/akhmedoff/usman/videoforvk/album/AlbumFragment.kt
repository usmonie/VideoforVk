package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.Video
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment

class AlbumFragment : Fragment(), AlbumContract.View {

    companion object {
        private const val ALBUM_ID = "album_id"
        private const val ALBUM_OWNER_ID = "album_owner_id"
        private const val ALBUM_NAME = "album_name"

        fun getFragment(item: CatalogItem): AlbumFragment {
            val fragment = AlbumFragment()
            val arguments = Bundle()

            arguments.putString(ALBUM_ID, item.id.toString())
            arguments.putString(ALBUM_OWNER_ID, item.ownerId.toString())
            arguments.putString(ALBUM_NAME, item.title)

            fragment.arguments = arguments

            return fragment
        }
    }

    override lateinit var albumPresenter: AlbumContract.Presenter


    override fun showVideos(items: PagedList<Video>) {
    }

    override fun showAlbumTitle(title: String) {
    }

    override fun showAlbumImage(poster: String) {
    }

    override fun showVideo(video: Video) {
    }

    override fun setAdded(isAdded: Boolean) {
    }

    override fun getAlbumId(): String? {
        return arguments?.getString(ALBUM_ID)
    }

    override fun getAlbumOwnerId(): String? {
        return arguments?.getString(ALBUM_OWNER_ID)
    }

    override fun getAlbumTitle(): String? {
        return arguments?.getString(ALBUM_NAME)
    }
}