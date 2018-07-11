package akhmedoff.usman.thevt.ui.view

import akhmedoff.usman.thevt.ui.catalog.CatalogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentsViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {

    private val catalogs = mutableListOf<Pair<Fragment, String>>()

    fun addFragment(pageCategory: String, pageTitle: String) =
            catalogs.add(Pair(CatalogFragment.createFragment(pageCategory), pageTitle))

    fun addFragment(page: Fragment, pageTitle: String) =
            catalogs.add(Pair(page, pageTitle))

    override fun getItem(position: Int) = catalogs[position].first

    override fun getCount() = catalogs.size

    override fun getPageTitle(position: Int) = catalogs[position].second
}