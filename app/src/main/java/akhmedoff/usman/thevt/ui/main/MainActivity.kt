package akhmedoff.usman.thevt.ui.main

import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.thevt.Router.replaceFragment
import akhmedoff.usman.thevt.ui.home.HomeFragment
import akhmedoff.usman.thevt.ui.main.MainContract.Presenter
import akhmedoff.usman.thevt.ui.profile.ProfileFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

private const val CURRENT_FRAGMENT_TAG = "current_fragment"

class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var mainPresenter: Presenter

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPresenter = MainPresenter(getUserRepository(this), this)

        homeFragment = when {
            supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) as HomeFragment

            else -> HomeFragment()
        }
        profileFragment = when {
            supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) != null -> supportFragmentManager.findFragmentByTag(ProfileFragment.FRAGMENT_TAG) as ProfileFragment

            else -> ProfileFragment.createFragment(mainPresenter.getUserId())
        }

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

    override fun showLastFragment() {
        currentFragment?.let {
            replaceFragment(supportFragmentManager, fragment = it, fragmentTag = it.tag)
        }
    }

    override fun showSettings() {
    }

}