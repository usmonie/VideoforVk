package akhmedoff.usman.videoforvk.profile

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.search.SearchActivity
import akhmedoff.usman.videoforvk.videos.VideosFragment
import akhmedoff.usman.videoforvk.view.FragmentsViewPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.search_toolbar.*

class ProfileFragment : Fragment(), ProfileContract.View {

    companion object {
        const val FRAGMENT_TAG = "profile_fragment_tag"

        private const val USER_ID = "user_id"

        fun createFragment(userId: String?) = ProfileFragment().apply {
            val bundle = Bundle()
            bundle.putString(USER_ID, userId)

            arguments = bundle
        }
    }

    override lateinit var presenter: ProfileContract.Presenter

    private val pagesPagerAdapter by lazy {
        FragmentsViewPagerAdapter(childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProfilePresenter(this, getUserRepository(context!!))
        presenter.view = this
        presenter.onCreated()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
        search_box_collapsed.setOnClickListener { presenter.onSearchClicked() }

        view_pager.adapter = pagesPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    override fun showUserName(name: String) {
        user_name.text = name
    }

    override fun showUserPhoto(photoUrl: String) =
        Picasso.with(context).load(photoUrl).into(user_avatar)

    override fun setIsUser(isUser: Boolean) {

    }

    override fun showPages(ownerId: String) {
        pagesPagerAdapter.addFragment(
            VideosFragment.createFragment(ownerId),
            resources.getString(R.string.videos_title)
        )

        pagesPagerAdapter.notifyDataSetChanged()
    }

    override fun getUserId(): String? = arguments?.getString(USER_ID)

    override fun showTabs() {
        tabs.visibility = VISIBLE
        view_pager.visibility = VISIBLE
    }

    override fun hideTabs() {
        tabs.visibility = GONE
        view_pager.visibility = GONE
    }

    override fun showError(message: String) {
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun startSearch() = startActivity(Intent(context, SearchActivity::class.java))
}