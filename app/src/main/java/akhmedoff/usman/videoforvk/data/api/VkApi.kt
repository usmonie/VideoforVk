package akhmedoff.usman.videoforvk.data.api

import akhmedoff.usman.videoforvk.model.Response
import akhmedoff.usman.videoforvk.model.ResponseVideo
import akhmedoff.usman.videoforvk.model.User
import android.arch.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {
    @GET("users.get")
    fun getUser(@Query("user_ids") users_id: String? = null,
                @Query("fields") fields: String = "nickname,photo_100,screen_name,photo_max,photo_max_orig,photo_id,has_photo,is_friend,friend_status,online,status,is_favorite",
                @Query("access_token") token: String,
                @Query("v") v: String = "5.69"): LiveData<Response<User>>

    @GET("video.get")
    fun getVideos(@Query("owner_id") ownerId: Int? = null,
                  @Query("videos") videos: String? = null,
                  @Query("album_id") albumId: String? = null,
                  @Query("count") count: Int = 15,
                  @Query("offset") offset: Long = 0,
                  @Query("access_token") token: String,
                  @Query("extended") extended: Boolean = true,
                  @Query("v") v: String = "5.69"): LiveData<Response<ResponseVideo>>

    @GET("video.getCatalog")
    fun getCatalog(@Query("count") count: Int = 10,
                   @Query("items_count") itemsCount: Int = 10,
                   @Query("from") from: String? = null,
                   @Query("extended") extended: Boolean = true,
                   @Query("filters") filters: String,
                   @Query("access_token") token: String,
                   @Query("v") v: String = "5.69")

}