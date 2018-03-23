package akhmedoff.usman.videoforvk.search

import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.video.VideoFragment
import akhmedoff.usman.videoforvk.view.VideosRecyclerAdapter
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(), SearchContract.View {
    override lateinit var searchPresenter: SearchContract.Presenter

    private val adapter: VideosRecyclerAdapter by lazy {
        VideosRecyclerAdapter({ searchPresenter.onClick(item = it) }, R.layout.search_videos)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchPresenter =
                SearchPresenter(this, getVideoRepository(context!!))


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        activity?.onBackPressed()
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

    override fun showFoundVideos(videos: PagedList<Video>) = adapter.submitList(videos)

    override fun expandFilters() {
    }

    override fun collapseFilters() {
    }

    override fun showError(errorMessage: String) {
    }

    override fun showVideo(item: Video) {
        val fragment = VideoFragment.getInstance(item)

        activity?.supportFragmentManager?.let {
            Router.replaceFragment(
                it,
                fragment,
                true,
                VideoFragment.FRAGMENT_TAG
            )
        }
    }
}