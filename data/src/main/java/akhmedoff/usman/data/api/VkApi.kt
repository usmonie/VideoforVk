package akhmedoff.usman.data.api

import akhmedoff.usman.data.model.*
import android.arch.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface VkApi {
    companion object {
        const val API_VERSION = "5.71"
    }

    @GET("users.get")
    fun getUsers(
        @Query("user_ids") users_id: String? = null,
        @Query("fields") fields: String = "nickname,photo_100,screen_name,photo_max,photo_max_orig,photo_id,has_photo,is_friend,friend_status,online,status,is_favorite"
    ): LiveData<Response<List<User>>>

    @GET("video.get")
    fun getVideos(
        @Query("owner_id") ownerId: String?,
        @Query("videos") videos: String?,
        @Query("album_id") albumId: String?,
        @Query("count") count: Int,
        @Query("offset") offset: Long,
        @Query("extended") extended: Boolean = true
    ): Call<ResponseVideo>

    @GET("video.getAlbumById")
    fun getAlbum(
        @Query("owner_id") ownerId: String?,
        @Query("album_id") albumId: String?
    ): LiveData<Response<Album>>

    @GET("video.getCatalog")
    fun getCatalog(
        @Query("count") count: Int,
        @Query("items_count") itemsCount: Int,
        @Query("from") from: String? = null,
        @Query("filters") filters: String = "other"
    ): Call<ResponseCatalog>

    @GET
    fun auth(
        @Url url: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("scope") scope: String,
        @Query("grant_type") grantType: String = "password",
        @Query("code") code: String? = null,
        @Query("captcha_sid") captchaSid: String? = null,
        @Query("captcha_key") captchaKey: String? = null,
        @Query("2fa_supported") supported: Int = 1
    ): Call<Auth>

    @GET("video.search")
    fun searchVideos(
        @Query("q") query: String,
        @Query("sort") sort: Int?,
        @Query("hd") hd: Int?,
        @Query("adult") adult: Int?,
        @Query("filters") filters: String?,
        @Query("search_own") searchOwn: Boolean?,
        @Query("offset") offset: Long?,
        @Query("longer") longer: Long?,
        @Query("shorter") shorter: Long?
    ): Call<ResponseVideo>

    @GET("video.add")
    fun addVideo(@Query("video_id") videoId: String, @Query("owner_id") ownerId: String)

}