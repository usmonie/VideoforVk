package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router.hideFragment
import akhmedoff.usman.videoforvk.Router.replaceFragment
import akhmedoff.usman.videoforvk.home.HomeFragment
import akhmedoff.usman.videoforvk.looking.LookingFragment
import akhmedoff.usman.videoforvk.main.MainContract.Presenter
import akhmedoff.usman.videoforvk.profile.ProfileFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View,
        FragmentManager.OnBackStackChangedListener {
    companion object {
        const val CURRENT_FRAGMENT_TAG = "current_fragment"
    }

    override lateinit var mainPresenter: Presenter

    private lateinit var homeFragment: HomeFragment
    private lateinit var lookingFragment: LookingFragment
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

            else -> ProfileFragment()
        }
        lookingFragment = when {
            supportFragmentManager.findFragmentByTag(LookingFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(LookingFragment.FRAGMENT_TAG) as LookingFragment

            else -> LookingFragment()
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

    override fun showProfile() =
            replaceFragment(
                    supportFragmentManager,
                    fragment = profileFragment,
                    fragmentTag = ProfileFragment.FRAGMENT_TAG
            )

    override fun showLooking() = replaceFragment(
            supportFragmentManager,
            fragment = lookingFragment,
            fragmentTag = LookingFragment.FRAGMENT_TAG
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
                        lookingFragment.isVisible -> lookingFragment
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
        when {
            supportFragmentManager.backStackEntryCount > 0 ->
                navigation.visibility = View.GONE
            else -> navigation.visibility = View.VISIBLE
        }
    }
}