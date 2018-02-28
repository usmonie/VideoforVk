package akhmedoff.usman.data.utils

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.db.OwnerDao
import akhmedoff.usman.data.local.UserSettings
import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.repository.GroupRepository
import akhmedoff.usman.data.repository.UserRepository
import akhmedoff.usman.data.repository.VideoRepository
import akhmedoff.usman.data.utils.deserializers.*
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var interceptor: Interceptor

private val okHttp: OkHttpClient by lazy {
    OkHttpClient.Builder().apply {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        addInterceptor(logging)
        addInterceptor(interceptor)
    }.build()

}

private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://api.vk.com/method/")
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
}

val vkApi: VkApi by lazy { retrofit.create(VkApi::class.java) }

val gson: Gson by lazy {
    GsonBuilder().apply {

        registerTypeAdapter(
            User::class.java,
            UserDeserializer()
        )
        registerTypeAdapter(
            CheckTokenResponse::class.java,
            CheckTokenDeserializer()
        )
        registerTypeAdapter(
            Group::class.java,
            GroupDeserializer()
        )
        registerTypeAdapter(
            ResponseVideo::class.java,
            VideoDeserializer()
        )
        registerTypeAdapter(
            ResponseCatalog::class.java,
            CatalogDeserializer()
        )
        registerTypeAdapter(
            Auth::class.java,
            AuthDeserializer()
        )

        registerTypeAdapter(
            Album::class.java,
            AlbumDeserializer()
        )
    }.create()
}

fun getVideoRepository(context: Context, ownerDao: OwnerDao) =
    VideoRepository(vkApi, UserSettings.getUserSettings(context), ownerDao)

fun getUserRepository(context: Context) =
    UserRepository(UserSettings.getUserSettings(context), vkApi)

fun getGroupRepository(context: Context) =
    GroupRepository(vkApi, UserSettings.getUserSettings(context))

