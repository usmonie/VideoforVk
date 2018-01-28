package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.home.HomeFragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {
    companion object {
        const val CURRENT_FRAGMENT_KEY = "current_fragment"
    }

    override lateinit var mainPresenter: MainContract.Presenter

    private lateinit var fragment: Fragment

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener {
        mainPresenter.navigate(it.itemId)
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter = MainPresenter()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            mainPresenter.navigate(R.id.navigation_home)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragments = supportFragmentManager.fragments
            fragments.forEach {
                if (it.isVisible) {
                    presenter.updateCurrentFragment(it)
                }
            }
        }
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun showHome() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, HomeFragment(), CURRENT_FRAGMENT_KEY)
            .commitAllowingStateLoss()
    }

    override fun showProfile() {
        navigation.selectedItemId = R.id.navigation_profile
    }

    override fun showFavourites() {
        navigation.selectedItemId = R.id.navigation_favourite
    }

    override fun initPresenter(): MainContract.Presenter = mainPresenter

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }
}
