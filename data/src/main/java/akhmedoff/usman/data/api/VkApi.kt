package akhmedoff.usman.data.api

import akhmedoff.usman.data.model.*
import android.arch.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface VkApi {
    companion object {
        const val API_VERSION = "5.73"
    }

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

    @GET
    fun checkToken(@Url url: String): Call<CheckTokenResponse>

    @GET("groups.getById")
    fun getGroups(
        @Query("group_ids") groupIds: List<String>? = null,
        @Query("group_id") groupId: String? = null
    ): Call<ApiResponse<List<Group>>>

    @GET("groups.leave")
    fun leaveGroup(
        @Query("group_id") groupId: Long
    ): Call<ApiResponse<Boolean>>

    @GET("groups.join")
    fun joinGroup(
        @Query("group_id") groupId: Long
    ): Call<ApiResponse<Boolean>>

    @GET("users.get")
    fun getUsers(
        @Query("user_ids") users_id: List<String>? = null,
        @Query("fields") fields: String = "photo_100,screen_name,photo_max,photo_max_orig,photo_id,has_photo,is_friend,friend_status"
    ): Call<ApiResponse<List<User>>>

    @GET("video.get")
    fun getVideos(
        @Query("owner_id") ownerId: String?,
        @Query("videos") videos: String?,
        @Query("album_id") albumId: String?,
        @Query("likes") count: Int,
        @Query("offset") offset: Long,
        @Query("extended") extended: Boolean = true
    ): Call<ResponseVideo>

    @GET("video.getAlbumById")
    fun getAlbum(
        @Query("owner_id") ownerId: String?,
        @Query("album_id") albumId: String?
    ): LiveData<ApiResponse<Album>>

    @GET("video.getAlbums")
    fun getAlbums(
        @Query("owner_id") ownerId: String?,
        @Query("offset") offset: Long,
        @Query("likes") count: Long,
        @Query("extended") extended: Int = 1,
        @Query("need_system") need_system: Int = 0
    ): Call<ApiResponse<AlbumsResponse>>

    @GET("video.getCatalog")
    fun getCatalog(
        @Query("likes") count: Int,
        @Query("items_count") itemsCount: Int,
        @Query("from") from: String? = null,
        @Query("filters") filters: String
    ): Call<ResponseCatalog>

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

    @GET("video.addToAlbum")
    fun addVideoToAlbum(
        @Query("target_id") targetId: String? = null,
        @Query("album_id") albumId: String? = null,
        @Query("album_ids") albumIds: List<Int>? = null,
        @Query("owner_id") ownerId: String,
        @Query("video_id") videoId: String
    ): Call<ApiResponse<Int>>

    @GET("video.getAlbumsByVideo")
    fun getAlbumsByVideo(
        @Query("target_id") targetId: String? = null,
        @Query("owner_id") ownerId: String,
        @Query("video_id") videoId: String,
        @Query("extended") extended: Int = 0
    ): Call<ApiResponse<List<Int>>>

    @GET("video.getCatalogSection")
    fun getCatalogSection(
        @Query("section_id") section_id: String,
        @Query("from") from: String,
        @Query("likes") count: Int,
        @Query("extended") extended: Boolean = true
    ): Call<ResponseCatalog>

    @GET("likes.add")
    fun like(
        @Query("type") type: String,
        @Query("owner_id") ownerId: String?,
        @Query("item_id") itemId: String,
        @Query("captcha_sid") captchaSid: String?,
        @Query("captcha_key") captchaKey: String?
    ): Call<ApiResponse<Likes>>

    @GET("likes.delete")
    fun unlike(
        @Query("type") type: String,
        @Query("owner_id") ownerId: String?,
        @Query("item_id") itemId: String,
        @Query("captcha_sid") captchaSid: String?,
        @Query("captcha_key") captchaKey: String?
    ): Call<ApiResponse<Likes>>

}