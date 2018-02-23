package akhmedoff.usman.videoforvk.album

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import akhmedoff.usman.videoforvk.utils.vkApi
import akhmedoff.usman.videoforvk.video.VideoActivity
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : BaseActivity<AlbumContract.View, AlbumContract.Presenter>(),
    AlbumContract.View {

    companion object {
        private const val ALBUM_ID = "album_id"
        private const val ALBUM_OWNER_ID = "album_owner_id"
        private const val ALBUM_NAME = "album_name"

        fun getActivity(item: CatalogItem, context: Context): Intent {
            val intent = Intent(context, AlbumActivity::class.java)


            intent.putExtra(ALBUM_ID, item.id.toString())
            intent.putExtra(ALBUM_OWNER_ID, item.ownerId.toString())
            intent.putExtra(ALBUM_NAME, item.title)

            return intent
        }
    }

    private val adapter: AlbumRecyclerAdapter by lazy {
        val adapter = AlbumRecyclerAdapter { presenter.clickVideo(it) }

        adapter.setHasStableIds(true)
        return@lazy adapter
    }

    override lateinit var albumPresenter: AlbumContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        albumPresenter = AlbumPresenter(VideoRepository(vkApi))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        album_videos_recycler.layoutManager = layoutManager
        album_videos_recycler.adapter = adapter
    }

    override fun getAlbumId() = intent?.getStringExtra(ALBUM_ID)

    override fun getAlbumOwnerId() = intent?.getStringExtra(ALBUM_OWNER_ID)

    override fun showVideos(items: PagedList<Video>) = adapter.setList(items)

    override fun showAlbumTitle(title: String) {
        toolbar.title = title
    }

    override fun showAlbumImage(poster: String) {
        Picasso
            .with(this)
            .load(poster)
            .into(app_bar_album_poster_image)
    }

    override fun showVideo(video: Video) = startActivity(VideoActivity.getActivity(video, this))

    override fun getAlbumTitle(): String? {
        return intent?.getStringExtra(ALBUM_NAME)
    }

    override fun setAdded(isAdded: Boolean) {
    }

    override fun initPresenter() = albumPresenter

}
