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

    private val homeFragment by lazy { HomeFragment() }
    private val lookingFragment by lazy { LookingFragment() }
    private val profileFragment by lazy { ProfileFragment.createFragment(null) }

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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, homeFragment, HomeFragment.FRAGMENT_TAG)
                .hide(homeFragment)
                .add(R.id.container, lookingFragment, LookingFragment.FRAGMENT_TAG)
                .hide(lookingFragment)
                .add(R.id.container, profileFragment, ProfileFragment.FRAGMENT_TAG)
                .hide(profileFragment)
                .commit()
        }

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener)
    }

    override fun showHome() {
        supportFragmentManager.beginTransaction()
            .show(homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun showProfile() {
        supportFragmentManager.beginTransaction()
            .show(profileFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun showLooking() {
        supportFragmentManager.beginTransaction()
            .show(lookingFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun hidePrevious() {
        val fragment: Fragment =
            when {
                !homeFragment.isHidden -> homeFragment
                !profileFragment.isHidden -> profileFragment
                else -> lookingFragment
            }

        supportFragmentManager
            .beginTransaction()
            .hide(fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commitNow()
    }

    override fun showSettings() {
    }

    override fun initPresenter() = mainPresenter
}
