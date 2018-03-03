package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.home.HomeFragment
import akhmedoff.usman.videoforvk.main.MainContract.Presenter
import akhmedoff.usman.videoforvk.main.MainContract.View
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<View, Presenter>(), View {
    override lateinit var mainPresenter: Presenter

    private val homeFragment by lazy { HomeFragment() }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            mainPresenter.forwardTo(item.itemId)
            true
        }


    private val onNavigationItemReselectedListener =
        BottomNavigationView.OnNavigationItemReselectedListener {}

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter = MainPresenter()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener)
    }

    override fun showHome() {
        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
    }

    override fun showProfile() {
    }

    override fun showLooking() {
    }

    override fun showSettings() {
    }

    override fun initPresenter(): Presenter = mainPresenter
}
