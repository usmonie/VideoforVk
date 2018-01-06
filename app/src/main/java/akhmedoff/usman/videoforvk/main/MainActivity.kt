package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.home.HomeFragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {
    override lateinit var mainPresenter: MainContract.Presenter

    private lateinit var homeFragment: HomeFragment

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener {
        mainPresenter.navigate(it.itemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter = MainPresenter()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, homeFragment)
                .commitAllowingStateLoss()

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    override fun showHome() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, homeFragment)
                .commitAllowingStateLoss()
    }

    override fun showProfile() {
        navigation.selectedItemId = R.id.navigation_profile
    }

    override fun showFavourites() {
        navigation.selectedItemId = R.id.navigation_favourite
    }

    override fun initPresenter(): MainContract.Presenter = mainPresenter
}
