package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.model.*
import akhmedoff.usman.videoforvk.utils.deserializers.*
import akhmedoff.usman.videoforvk.utils.interceptors.AutentificationInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val okHttp: OkHttpClient by lazy {
    val okHttpBuilder = OkHttpClient.Builder()
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    okHttpBuilder.addInterceptor(logging)
    okHttpBuilder.addInterceptor(AutentificationInterceptor())


    return@lazy okHttpBuilder.build()
}
val retrofit: Retrofit by lazy {


    return@lazy Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttp)
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .baseUrl("https://api.vk.com/method/")
        .build()
}

val vkApi: VkApi by lazy { retrofit.create(VkApi::class.java) }

val gson: Gson by lazy {
    val gsonBuilder = GsonBuilder().apply {
        registerTypeAdapter(
            User::class.java,
            UserDeserializer()
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
    }

    return@lazy gsonBuilder.create()
}