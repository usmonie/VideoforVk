package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router.hideFragment
import akhmedoff.usman.videoforvk.Router.replaceFragment
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.home.HomeFragment
import akhmedoff.usman.videoforvk.looking.LookingFragment
import akhmedoff.usman.videoforvk.main.MainContract.Presenter
import akhmedoff.usman.videoforvk.profile.ProfileFragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, Presenter>(), MainContract.View,
    FragmentManager.OnBackStackChangedListener {

    override lateinit var mainPresenter: Presenter

    private lateinit var homeFragment: HomeFragment
    private lateinit var lookingFragment: LookingFragment
    private lateinit var profileFragment: ProfileFragment

    private var currentFragment: Fragment? = null

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

        supportFragmentManager.addOnBackStackChangedListener(this)

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

        if (savedInstanceState == null) {
            mainPresenter.onCreate()
        }
    }

    override fun showHome() = replaceFragment(
        supportFragmentManager,
        homeFragment,
        fragmentTag = LookingFragment.FRAGMENT_TAG
    )

    override fun showProfile() =
        replaceFragment(
            supportFragmentManager,
            profileFragment,
            fragmentTag = LookingFragment.FRAGMENT_TAG
        )

    override fun showLooking() = replaceFragment(
        supportFragmentManager,
        lookingFragment,
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


    override fun showSettings() {
    }

    override fun onBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            navigation.visibility = View.GONE
        } else {
            navigation.visibility = View.VISIBLE
        }
    }

    override fun initPresenter() = mainPresenter
}
