package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.home.HomeFragment
import akhmedoff.usman.videoforvk.looking.LookingFragment
import akhmedoff.usman.videoforvk.main.MainContract.Presenter
import akhmedoff.usman.videoforvk.main.MainContract.View
import akhmedoff.usman.videoforvk.profile.ProfileFragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<View, Presenter>(), View {

    override lateinit var mainPresenter: Presenter

    private lateinit var homeFragment: HomeFragment
    private lateinit var lookingFragment: LookingFragment
    private lateinit var profileFragment: ProfileFragment

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            mainPresenter.forwardTo(item.itemId)
            true
        }

    private val onNavigationItemReselectedListener =
        BottomNavigationView.OnNavigationItemReselectedListener {}

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter = MainPresenter(getUserRepository(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = when {
            supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(
                HomeFragment.FRAGMENT_TAG
            ) as HomeFragment
            else -> HomeFragment()
        }
        profileFragment = when {
            supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(
                ProfileFragment.FRAGMENT_TAG
            ) as ProfileFragment
            else -> ProfileFragment()
        }
        lookingFragment = when {
            supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(
                LookingFragment.FRAGMENT_TAG
            ) as LookingFragment
            else -> LookingFragment()
        }

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener)
    }

    override fun showHome() {
        if (supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) != null) {
            showFragment(homeFragment)
        } else {
            addFragment(homeFragment, HomeFragment.FRAGMENT_TAG)
        }
    }

    override fun showProfile() {
        if (supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) != null) {
            showFragment(profileFragment)
        } else {
            addFragment(profileFragment, ProfileFragment.FRAGMENT_TAG)
        }
    }

    override fun showLooking() {
        when {
            supportFragmentManager.findFragmentByTag(LookingFragment.FRAGMENT_TAG) != null -> showFragment(
                lookingFragment
            )
            else -> addFragment(lookingFragment, LookingFragment.FRAGMENT_TAG)
        }
    }

    override fun hidePrevious() {
        hideFragment(
            when {
                homeFragment.isVisible -> homeFragment
                profileFragment.isVisible -> profileFragment
                lookingFragment.isVisible -> lookingFragment
                else -> homeFragment
            }
        )
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .show(fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun showSettings() {
    }


    override fun initPresenter() = mainPresenter
}
