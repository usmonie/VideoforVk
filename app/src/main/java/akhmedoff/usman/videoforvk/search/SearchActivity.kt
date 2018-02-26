package akhmedoff.usman.videoforvk.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.data.utils.vkApi
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.video.VideoActivity
import android.arch.paging.PagedList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity<SearchContract.View, SearchContract.Presenter>(),
    SearchContract.View {

    override lateinit var searchPresenter: SearchContract.Presenter

    private val adapter: SearchAdapter by lazy {
        SearchAdapter({ searchPresenter.onClick(item = it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        searchPresenter = SearchPresenter(VideoRepository(vkApi))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_recycler.adapter = adapter

        search_expanded_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchPresenter.search()
            }
        })

        search_expanded_back_button.setOnClickListener { searchPresenter.onBackClicked() }

        filter_fab.setOnClickListener { drawer_layout.openDrawer(Gravity.END) }

        hd_filter.setOnCheckedChangeListener { _, _ ->
            searchPresenter.search()
        }

        adult_filter.setOnCheckedChangeListener { _, _ ->
            searchPresenter.search()
        }

        sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchPresenter.search()
            }

        }
        duration_filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchPresenter.search()
            }

        }
    }

    override fun onBackClicked() {
        onBackPressed()
    }

    override fun getQueryText() = search_expanded_edit_text.text.toString()

    override fun getHdFilter() = when {
        hd_filter.isChecked -> 1
        else -> 0
    }

    override fun getAdultFilter() = when {
        adult_filter.isChecked -> 1
        else -> 0
    }

    override fun getSortFilter() = sort.selectedItemPosition

    override fun getLengthFilter() = duration_filter.selectedItem.toString()

    override fun searchOwn(): Boolean {
        return false
    }

    override fun getLonger(): Long {
        return 0
    }

    override fun getShorter(): Long {
        return 0
    }

    override fun showFoundVideos(videos: PagedList<Video>) = adapter.setList(videos)

    override fun expandFilters() {
    }

    override fun collapseFilters() {
    }

    override fun showError(errorMessage: String) {
    }

    override fun showVideo(item: Video) {
        startActivity(VideoActivity.getActivity(item, this))
    }

    override fun initPresenter() = searchPresenter
}