package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.model.ResponseVideo
import akhmedoff.usman.videoforvk.model.User
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val vkApi: VkApi by lazy {
    val gsonBuilder = GsonBuilder()

    val userDeserializer = UserDeserializer()
    val videoDeserializer = VideoDeserializer()
    gsonBuilder
            .registerTypeAdapter(User::class.java, userDeserializer)
    gsonBuilder
            .registerTypeAdapter(ResponseVideo::class.java, videoDeserializer)

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    val gson = gsonBuilder.create()

    Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl("https://api.vk.com/method/")
            .build()
            .create(VkApi::class.java)
}