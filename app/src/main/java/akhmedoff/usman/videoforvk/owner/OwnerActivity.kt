package akhmedoff.usman.videoforvk.owner

import akhmedoff.usman.data.db.AppDatabase
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.repository.GroupRepository
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.data.utils.vkApi
import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.base.BaseActivity
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_owner.*


class OwnerActivity : BaseActivity<OwnerContract.View, OwnerContract.Presenter>(),
    OwnerContract.View {

    companion object {
        const val OWNER_TYPE = "owner_type"
        const val OWNER_USER = "owner_user"
        const val OWNER_GROUP = "owner_group"
        const val OWNER_ID = "owner_ID"

        fun getGroupActivity(owner: Owner, context: Context): Intent {
            val intent = Intent(context, OwnerActivity::class.java)

            intent.putExtra(OWNER_ID, owner.id.toString())
            intent.putExtra(OWNER_TYPE, OWNER_GROUP)
            return intent
        }

        fun getUserActivity(owner: Owner, context: Context): Intent {
            val intent = Intent(context, OwnerActivity::class.java)

            intent.putExtra(OWNER_ID, owner.id.toString())
            intent.putExtra(OWNER_TYPE, OWNER_USER)
            return intent
        }
    }

    override lateinit var ownerPresenter: OwnerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        ownerPresenter = when (getOwnerType()) {
            OWNER_GROUP -> GroupPresenter(
                GroupRepository(
                    vkApi,
                    UserSettings.getUserSettings(context)
                ), getVideoRepository(this, AppDatabase.getInstance(this).ownerDao())
            )
            OWNER_USER -> UserPresenter(
                UserRepository(UserSettings.getUserSettings(this), vkApi),
                getVideoRepository(this, AppDatabase.getInstance(this).ownerDao())
            )
            else -> throw Exception(getOwnerType())
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)

    }

    override fun getOwnerType(): String = intent.getStringExtra(OWNER_TYPE)

    override fun getOwnerId(): String = intent.getStringExtra(OWNER_ID)

    override fun showOwnerInfo(owner: User) {
        showImage(owner.photoMax)
        showTitle(owner.firstName + " " + owner.lastName)
    }

    override fun showOwnerInfo(owner: Group) {
        showImage(owner.photo200)
        showTitle(owner.name)
    }

    private fun showTitle(name: String) {
        collapsingToolbar.title = name
        toolbar.title = name
    }

    private fun showImage(photo: String) {
/*
        val imageBitmap = Picasso.with(this).load(photo).get()
        owner_photo.setImageBitmap(imageBitmap)

        Palette.from(imageBitmap).generate {
            it.darkVibrantSwatch?.rgb?.let { color -> toolbar.setBackgroundColor(color) }
            it.darkMutedSwatch?.titleTextColor?.let { color -> toolbar.setTitleTextColor(color) }
        }*/

        Picasso.with(this).load(photo).into(owner_photo)
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