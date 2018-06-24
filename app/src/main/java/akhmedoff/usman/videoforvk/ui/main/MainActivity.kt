package akhmedoff.usman.videoforvk.ui.main

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router.hideFragment
import akhmedoff.usman.videoforvk.Router.replaceFragment
import akhmedoff.usman.videoforvk.ui.explore.ExploreFragment
import akhmedoff.usman.videoforvk.ui.home.HomeFragment
import akhmedoff.usman.videoforvk.ui.main.MainContract.Presenter
import akhmedoff.usman.videoforvk.ui.profile.ProfileFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

private const val CURRENT_FRAGMENT_TAG = "current_fragment"

class MainActivity : AppCompatActivity(), MainContract.View,
        FragmentManager.OnBackStackChangedListener {

    override lateinit var mainPresenter: Presenter

    private lateinit var homeFragment: HomeFragment
    private lateinit var exploreFragment: ExploreFragment
    private lateinit var profileFragment: ProfileFragment

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter = MainPresenter(getUserRepository(this), this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter.view = this
        supportFragmentManager.addOnBackStackChangedListener(this)

        homeFragment = when {
            supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) as HomeFragment

            else -> HomeFragment()
        }
        profileFragment = when {
            supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) as ProfileFragment

            else -> ProfileFragment.createFragment(mainPresenter.getUserId())
        }
        exploreFragment = when {
            supportFragmentManager.findFragmentByTag(ExploreFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(ExploreFragment.FRAGMENT_TAG) as ExploreFragment

            else -> ExploreFragment()
        }

        navigation.setOnNavigationItemSelectedListener { item ->
            mainPresenter.forwardTo(item.itemId)
            true
        }
        navigation.setOnNavigationItemReselectedListener {}

        if (savedInstanceState == null) {
            mainPresenter.onCreate()
        } else if (savedInstanceState.containsKey(CURRENT_FRAGMENT_TAG)) {
            currentFragment = supportFragmentManager.findFragmentByTag(
                    savedInstanceState.getString(CURRENT_FRAGMENT_TAG)
            )
            mainPresenter.onRecreate()
        }
    }

    override fun showHome() = replaceFragment(
            supportFragmentManager,
            fragment = homeFragment,
            fragmentTag = HomeFragment.FRAGMENT_TAG
    )

    override fun showProfile() = replaceFragment(
            supportFragmentManager,
            fragment = profileFragment,
            fragmentTag = ProfileFragment.FRAGMENT_TAG
    )

    override fun showExplore() = replaceFragment(
            supportFragmentManager,
            fragment = exploreFragment,
            fragmentTag = ExploreFragment.FRAGMENT_TAG
    )

    override fun hidePrevious() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            hideFragment(
                    supportFragmentManager,
                    when {
                        homeFragment.isVisible -> homeFragment
                        profileFragment.isVisible -> profileFragment
                        exploreFragment.isVisible -> exploreFragment
                        else -> homeFragment
                    }
            )
        }
    }

    override fun showLastFragment() {
        currentFragment?.let {
            replaceFragment(supportFragmentManager, fragment = it, fragmentTag = it.tag)
        }
    }

    override fun showSettings() {
    }

    override fun onBackStackChanged() {
        navigation.isVisible = supportFragmentManager.backStackEntryCount <= 0
    }
}