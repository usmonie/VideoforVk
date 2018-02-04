package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.data.local.UserSettings
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import akhmedoff.usman.videoforvk.utils.vkApi
import akhmedoff.usman.videoforvk.video.VideoActivity
import akhmedoff.usman.videoforvk.view.OnClickListener
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    override lateinit var mainPresenter: MainContract.Presenter

    private val adapter: MainRecyclerAdapter by lazy {
        val adapter = MainRecyclerAdapter(object :
            OnClickListener<VideoCatalog> {
            override fun onClick(item: VideoCatalog) {
                presenter.clickVideo(item)
            }
        })

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPresenter =
                MainPresenter(
                    VideoRepository(
                        UserSettings.getUserSettings(context),
                        vkApi
                    )
                )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        home_recycler.layoutManager = layoutManager
        home_recycler.adapter = adapter
        update_home_layout.setOnRefreshListener { mainPresenter.refresh() }
    }

    override fun showProfile() {
    }

    override fun showList(videos: PagedList<Catalog>) = adapter.setList(videos)

    override fun showVideo(video: VideoCatalog) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra(
            VideoActivity.VIDEO_ID,
            video.ownerId.toString() + "_" + video.id.toString()
        )

        startActivity(intent)
    }

    override fun showCatalog(catalog: Catalog) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCatalogs() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     *
     * This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * [.onPrepareOptionsMenu].
     *
     *
     * The default implementation populates the menu with standard system
     * menu items.  These are placed in the [Menu.CATEGORY_SYSTEM] group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     *
     * You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     *
     * When you add items to the menu, you can implement the Activity's
     * [.onOptionsItemSelected] method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     *
     * @see .onPrepareOptionsMenu
     *
     * @see .onOptionsItemSelected
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun showLoading() {
        update_home_layout.isRefreshing = true
    }

    override fun hideLoading() {
        update_home_layout.isRefreshing = false
    }

    override fun showErrorLoading() {
        Snackbar.make(activity_main, getText(R.string.error_loading), Snackbar.LENGTH_LONG).show()
    }

    override fun initPresenter(): MainContract.Presenter = mainPresenter
}
