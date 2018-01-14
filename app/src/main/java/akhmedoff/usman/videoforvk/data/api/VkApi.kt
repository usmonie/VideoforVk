package akhmedoff.usman.videoforvk.data.api

import akhmedoff.usman.videoforvk.model.*
import android.arch.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

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
                  @Query("count") count: Int = 1,
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
                   @Query("v") v: String = "5.69"): LiveData<ResponseCatalog>

    @GET
    fun auth(@Url url: String,
             @Query("grant_type") grantType: String = "password",
             @Query("client_id") clientId: String,
             @Query("client_secret") clientSecret: String,
             @Query("username") username: String,
             @Query("password") password: String,
             @Query("scope") scope: String,
             @Query("v") v: String = "5.69"): LiveData<Response<Auth>>
}