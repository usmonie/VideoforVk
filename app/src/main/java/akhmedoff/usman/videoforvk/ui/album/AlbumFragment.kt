package akhmedoff.usman.videoforvk.ui.album

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getAlbumRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.ui.video.VideoFragment
import akhmedoff.usman.videoforvk.ui.view.MarginItemDecorator
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_album.*

class AlbumFragment : Fragment(), AlbumContract.View {

    companion object {
        private const val ALBUM_ID = "album_id"
        private const val ALBUM_OWNER_ID = "album_owner_id"
        private const val ALBUM_NAME = "album_name"

        fun getFragment(item: CatalogItem) =
                getFragment(item.id.toString(), item.ownerId.toString(), item.title)

        fun getFragment(item: Album) =
                getFragment(item.id.toString(), item.ownerId.toString(), item.title)

        private fun getFragment(id: String,
                                ownerId: String,
                                title: String): AlbumFragment {
            val arguments = Bundle()

            arguments.putString(ALBUM_ID, id)
            arguments.putString(ALBUM_OWNER_ID, ownerId)
            arguments.putString(ALBUM_NAME, title)

            val fragment = AlbumFragment()
            fragment.arguments = arguments

            return fragment
        }
    }

    private val adapter: AlbumRecyclerAdapter by lazy {
        val adapter = AlbumRecyclerAdapter { video, view -> showVideo(video, view) }

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override lateinit var albumPresenter: AlbumContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumPresenter =
                AlbumPresenter(this, getAlbumRepository(context!!))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_album, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        album_videos_recycler.addItemDecoration(
                MarginItemDecorator(
                        1,
                        resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
                )
        )
        album_videos_recycler.adapter = adapter
        albumPresenter.onCreated()
    }

    override fun showVideos(items: PagedList<Video>) = adapter.submitList(items)

    override fun showAlbumTitle(title: String) {
        toolbar.title = title
    }

    override fun showAlbumImage(poster: String) = Picasso
            .get()
            .load(poster)
            .into(app_bar_album_poster_image)

    override fun showVideo(video: Video, view: View) {
        val fragment =
                VideoFragment.getInstance(video, ViewCompat.getTransitionName(view))

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(
                    fragmentManager,
                    this,
                    fragment,
                    true,
                    VideoFragment.FRAGMENT_TAG,
                    view
            )
        }
    }

    override fun setAdded(isAdded: Boolean) {
    }

    override fun getAlbumId(): String? = arguments?.getString(ALBUM_ID)

    override fun getAlbumOwnerId(): String? = arguments?.getString(ALBUM_OWNER_ID)

    override fun getAlbumTitle(): String? = arguments?.getString(ALBUM_NAME)
}