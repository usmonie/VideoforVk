package akhmedoff.usman.videoforvk.ui.profile

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.ui.albums.AlbumsFragment
import akhmedoff.usman.videoforvk.ui.search.SearchFragment
import akhmedoff.usman.videoforvk.ui.videos.VideosFragment
import akhmedoff.usman.videoforvk.ui.view.FragmentsViewPagerAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.search_toolbar.*

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

    private lateinit var pagesPagerAdapter: FragmentsViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProfilePresenter(this, getUserRepository(context!!))
        presenter.view = this

        pagesPagerAdapter = FragmentsViewPagerAdapter(childFragmentManager)

        if (savedInstanceState == null || !savedInstanceState.containsKey(RETAINED_KEY)) presenter.onCreated()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.offscreenPageLimit = 2
        view_pager.adapter = pagesPagerAdapter
        tabs.setupWithViewPager(view_pager)
        presenter.view = this
        presenter.onViewCreated()

        search_box_collapsed.setOnClickListener { presenter.onSearchClicked() }
    }

    override fun onStart() {
        super.onStart()
        presenter.view = this
    }

    override fun showUserName(name: String) {
        user_name?.text = name
    }

    override fun showUserPhoto(photoUrl: String) =
            Picasso.get().load(photoUrl).into(user_avatar)

    override fun setIsUser(isUser: Boolean) {

    }

    override fun showPages(ownerId: String) {
        pagesPagerAdapter.addFragment(
                VideosFragment.createFragment(ownerId),
                resources.getString(R.string.tab_videos_title)
        )

        pagesPagerAdapter.addFragment(
                AlbumsFragment.createFragment(ownerId),
                getString(R.string.tab_albums_title)
        )

        pagesPagerAdapter.notifyDataSetChanged()
    }

    override fun getUserId() = arguments?.getString(USER_ID)

    override fun showTabs(isShowing: Boolean) {
        tabs.isVisible = isShowing
        view_pager.isVisible = isShowing
    }

    override fun showError(message: String) {
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun startSearch() {
        val fragment = SearchFragment()

        activity?.supportFragmentManager?.let {
            Router.replaceFragment(
                    it,
                    this,
                    fragment,
                    true,
                    SearchFragment.FRAGMENT_TAG,
                    search_box_collapsed
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }

    override fun getIsUser() = arguments?.getBoolean(IS_USER) ?: true
}