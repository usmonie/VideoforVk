package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Group
import akhmedoff.usman.data.model.User
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.repository.GroupRepository
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.data.utils.vkApi
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.graphics.Palette
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_owner.*


class OwnerActivity : BaseActivity<OwnerContract.View, OwnerContract.Presenter>(),
    OwnerContract.View {

    companion object {
        const val OWNER_TYPE = "owner_type"
        const val OWNER_USER = "owner_user"
        const val OWNER_GROUP = "owner_group"
        const val OWNER_ID = "owner_ID"

        fun getActivity(owner: Group, context: Context): Intent {
            val intent = Intent(context, OwnerActivity::class.java)

            intent.putExtra(OWNER_ID, owner.id.toString())
            intent.putExtra(OWNER_TYPE, OWNER_GROUP)
            return intent
        }

        fun getActivity(owner: User, context: Context): Intent {
            val intent = Intent(context, OwnerActivity::class.java)

            intent.putExtra(OWNER_ID, owner.id.toString())
            intent.putExtra(OWNER_TYPE, OWNER_USER)
            return intent
        }
    }

    override lateinit var ownerPresenter: OwnerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        ownerPresenter = when (getOwnerType()) {
            OWNER_GROUP -> GroupPresenter(GroupRepository(vkApi), VideoRepository(vkApi))
            OWNER_USER -> UserPresenter(
                UserRepository(UserSettings.getUserSettings(this), vkApi), VideoRepository(
                    vkApi
                )
            )
            else -> throw Exception(getOwnerType())
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)

    }

    override fun getOwnerType(): String = intent.getStringExtra(OWNER_TYPE)

    override fun getOwnerId(): String = intent.getStringExtra(OWNER_ID)

    override fun showOwnerInfo(owner: User) {

    }

    override fun showOwnerInfo(owner: Group) {

        showImage(owner.photo200)
    }

    private fun showImage(photo: String) {
        val imageBitmap = Picasso.with(this).load(photo).get()

        owner_photo.setImageBitmap(imageBitmap)

        Palette.from(imageBitmap).generate {
            it.darkVibrantSwatch?.rgb?.let { color -> toolbar.setBackgroundColor(color) }
            it.darkMutedSwatch?.titleTextColor?.let { color -> toolbar.setTitleTextColor(color) }
        }
    }

    override fun showVideos(items: PagedList<Video>) {
    }

    override fun showAlbums(items: PagedList<Album>) {
    }

    override fun showVideo(item: Video) {
    }

    override fun showAlbum(item: Album) {
    }

    override fun showFriends() {
    }

    override fun showFavourites() {
    }

    override fun initPresenter() = ownerPresenter
}