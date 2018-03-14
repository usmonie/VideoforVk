package akhmedoff.usman.videoforvk.home


import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.search.SearchActivity
import akhmedoff.usman.videoforvk.view.FragmentsViewPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_toolbar.*

class HomeFragment : Fragment(), HomeContract.View {

    companion object {
        const val FRAGMENT_TAG = "home_fragment_tag"
        const val CURRENT_TAB_KEY = "current_tab"
        const val RETAINED_KEY = "retained"
    }

    override var presenter: HomeContract.Presenter = HomePresenter()

    private lateinit var catalogsPagerAdapter: FragmentsViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.view = this
        catalogsPagerAdapter = FragmentsViewPagerAdapter(childFragmentManager)

        if (savedInstanceState == null || !savedInstanceState.containsKey(RETAINED_KEY)) {
            presenter.onCreated()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager.adapter = catalogsPagerAdapter
        view_pager.offscreenPageLimit = 3
        tabs.setupWithViewPager(view_pager)

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_TAB_KEY)) {
            view_pager.setCurrentItem(savedInstanceState.getInt(CURRENT_TAB_KEY), false)
        }

        search_box_collapsed.setOnClickListener { presenter.searchClicked() }
    }

    override fun getResourcesString(id: Int) = context?.getString(id) ?: ""

    override fun initPage(pageCategory: String, pageTitle: String) {
        catalogsPagerAdapter.addFragment(pageCategory, pageTitle)
        catalogsPagerAdapter.notifyDataSetChanged()
    }

    override fun startSearch() = startActivity(Intent(context, SearchActivity::class.java))

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_TAB_KEY, view_pager.currentItem)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }
}
