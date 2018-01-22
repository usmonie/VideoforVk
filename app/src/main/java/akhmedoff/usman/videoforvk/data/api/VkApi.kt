package akhmedoff.usman.videoforvk.data.api

import akhmedoff.usman.videoforvk.model.*
import android.arch.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface VkApi {
    companion object {
        val API_VERSION = "5.71"
    }

    @GET("users.get")
    fun getUser(
        @Query("user_ids") users_id: String? = null,
        @Query("fields") fields: String = "nickname,photo_100,screen_name,photo_max,photo_max_orig,photo_id,has_photo,is_friend,friend_status,online,status,is_favorite",
        @Query("access_token") token: String,
        @Query("v") v: String = API_VERSION
    ): LiveData<Response<User>>

    @GET("video.get")
    fun getVideos(
        @Query("owner_id") ownerId: Int?,
        @Query("videos") videos: String?,
        @Query("album_id") albumId: String?,
        @Query("count") count: Int,
        @Query("offset") offset: Long,
        @Query("access_token") token: String,
        @Query("extended") extended: Boolean = true,
        @Query("v") v: String = API_VERSION
    ): LiveData<Response<ResponseVideo>>

    @GET("video.getCatalog")
    fun getCatalog(
        @Query("count") count: Int = 10,
        @Query("items_count") itemsCount: Int = 5,
        @Query("from") from: String? = null,
        @Query("filters") filters: String = "other",
        @Query("access_token") token: String,
        @Query("v") v: String = API_VERSION
    ): LiveData<Response<ResponseCatalog>>

    @GET
    fun auth(
        @Url url: String,
        @Query("grant_type") grantType: String = "password",
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("scope") scope: String,
        @Query("v") v: String = API_VERSION
    ): LiveData<Response<Auth>>
}