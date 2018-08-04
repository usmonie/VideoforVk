package akhmedoff.usman.thevt.ui.video

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.thevt.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import kotlinx.android.synthetic.main.popup_add_video_dialog.*

class AddVideoDialog(
        context: Context,
        private val cancelListener: () -> Unit,
        private val selectedAlbumListener: (Album, Boolean) -> Unit,
        private val okAlbumListener: () -> Unit
) :
        Dialog(context, true, { cancelListener() }) {

    private val albumsAdapter: AlbumsRecyclerAdapter = AlbumsRecyclerAdapter { album: Album, isChecked: Boolean ->
        selectedAlbumListener(
                album,
                isChecked
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_add_video_dialog)
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )

        albums_recycler.adapter = albumsAdapter
        albums_recycler.itemAnimator = DefaultItemAnimator()
        cancel_add_popup.setOnClickListener { cancelListener() }
        ok_add_popup.setOnClickListener { okAlbumListener() }
    }

    fun showAlbums(albums: PagedList<Album>) = albumsAdapter.submitList(albums)

    fun showLoading(isLoading: Boolean) {
        albums_progress.isVisible = isLoading
        albums_recycler.isVisible = !isLoading
        ok_add_popup.isEnabled = !isLoading
    }

    fun setSelectedAlbums(selectedAlbumIds: List<Int>) {
        albumsAdapter.notifyItemRangeChanged(
                0,
                albumsAdapter.currentList?.size ?: 0,
                selectedAlbumIds
        )
    }
}