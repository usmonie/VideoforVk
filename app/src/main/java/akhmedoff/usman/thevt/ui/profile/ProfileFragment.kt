package akhmedoff.usman.thevt.ui.profile

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getAlbumRepository
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.Router
import akhmedoff.usman.thevt.ui.album.AlbumFragment
import akhmedoff.usman.thevt.ui.albums.AlbumsFragment
import akhmedoff.usman.thevt.ui.favourites.FavouritesFragment
import akhmedoff.usman.thevt.ui.video.VideoActivity
import akhmedoff.usman.thevt.ui.view.MarginItemDecorator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import kotlinx.android.synthetic.main.fragment_profile.*

private const val USER_ID = "user_id"
private const val IS_USER = "is_user"

class ProfileFragment : Fragment(), ProfileContract.View {

    companion object {
        const val FRAGMENT_TAG = "profile_fragment_tag"
        const val RETAINED_KEY = "retained"

        fun createFragment(userId: String?) = ProfileFragment().apply {
            val bundle = Bundle()
            bundle.putString(USER_ID, userId)
            bundle.putBoolean(IS_USER, false)
            arguments = bundle
        }
    }

    override lateinit var presenter: ProfileContract.Presenter

    private val recyclerAdapter: ProfileRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ProfileRecyclerAdapter({ video, view -> showVideo(video, view) },
                { album, view -> showAlbum(album, view) },
                { view -> showAlbumsPage(getUserId(), view) },
                { showFavouritesPage() })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProfilePresenter(this,
                getUserRepository(context!!),
                getVideoRepository(context!!),
                getAlbumRepository(context!!))
        presenter.view = this

        if (savedInstanceState == null || !savedInstanceState.containsKey(RETAINED_KEY))
            presenter.onCreated()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.onViewCreated()

        profile_recycler.adapter = recyclerAdapter
        profile_recycler.itemAnimator = DefaultItemAnimator()
        profile_recycler.addItemDecoration(MarginItemDecorator(1, resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)))
        swipe_update.setOnRefreshListener { presenter.refresh() }

        main.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onStart() {
        super.onStart()
        presenter.view = this
    }

    override fun showUserName(name: String) {
        collapsingToolbar.title = name
        user_name.text = name
    }

    override fun showUserPhoto(photoUrl: String) {}

    override fun setIsUser(isUser: Boolean) {

    }

    override fun showUi(isVisible: Boolean) {
        profile_recycler.isVisible = isVisible
    }

    override fun getUserId() = arguments?.getString(USER_ID)

    override fun showLoadingError() {
    }

    override fun showStartPositionVideos() = profile_recycler.smoothScrollToPosition(0)

    override fun showLoading(isLoading: Boolean) {
        swipe_update.isRefreshing = isLoading
    }

    override fun showUserStatus(status: String) {
    }

    override fun showAlbums(albums: PagedList<Album>) {
        recyclerAdapter.albums = albums
        profile_recycler.smoothScrollToPosition(0)
    }

    override fun showVideos(videos: PagedList<Video>) {
        recyclerAdapter.submitList(videos)
        profile_recycler.smoothScrollToPosition(0)
    }

    override fun showFaveVideos(videos: PagedList<Video>) {
        recyclerAdapter.faveVideos = videos
        profile_recycler.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }

    private fun showVideo(video: Video, view: View) {
        val intent = VideoActivity.getInstance(video,
                ViewCompat.getTransitionName(view), context!!)

        Router.startActivityWithTransition(activity!!, intent, view)
    }

    private fun showAlbum(album: Album, view: View) {
        val fragment = AlbumFragment.getFragment(album, view.transitionName)

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(
                    fragmentManager,
                    this,
                    fragment,
                    true,
                    VideoActivity.FRAGMENT_TAG,
                    view
            )
        }
    }

    private fun showAlbumsPage(id: String?, view: View) {
        val fragment = AlbumsFragment.getFragment(id, view.transitionName)

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(
                    fragmentManager,
                    this,
                    fragment,
                    true,
                    VideoActivity.FRAGMENT_TAG,
                    view
            )
        }
    }

    private fun showFavouritesPage() {
        val fragment = FavouritesFragment()

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(fragmentManager, fragment, true, null)
        }
    }

    override fun getIsUser() = arguments?.getBoolean(IS_USER) ?: true
}