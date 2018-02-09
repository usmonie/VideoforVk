package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.App
import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.model.*
import android.os.Build
import android.os.Build.VERSION
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

val okHttp: OkHttpClient by lazy {
    val okHttpBuilder = OkHttpClient.Builder()
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    okHttpBuilder.addInterceptor(logging)

    okHttpBuilder.addInterceptor {
        val original = it.request()

        var str = ""
        var appbuild = 0
        try {
            val pkgInfo = App.context.packageManager.getPackageInfo(App.context.packageName, 0)
            str = pkgInfo.versionName
            appbuild = pkgInfo.versionCode
        } catch (e: Exception) {
        }

        val request = original.newBuilder()
            .header(
                "User-Agent",
                String.format(
                    Locale.US,
                    "VKAndroidApp/%s-%d (Android %s; SDK %d; %s; %s %s; %s)",
                    str,
                    Integer.valueOf(appbuild),
                    VERSION.RELEASE,
                    Integer.valueOf(VERSION.SDK_INT),
                    Build.CPU_ABI,
                    Build.MANUFACTURER,
                    Build.MODEL,
                    System.getProperty("user.language")
                )
            )
            .method(original.method(), original.body())
            .build()

        it.proceed(request)
    }
    okHttpBuilder.build()
}


val vkApi: VkApi by lazy {
    val gsonBuilder = GsonBuilder().apply {
        registerTypeAdapter(User::class.java, UserDeserializer())
        registerTypeAdapter(ResponseVideo::class.java, VideoDeserializer())
        registerTypeAdapter(ResponseCatalog::class.java, CatalogDeserializer())
        registerTypeAdapter(Auth::class.java, AuthDeserializer())
        registerTypeAdapter(Album::class.java, AlbumDeserializer())
    }

    val gson = gsonBuilder.create()

    return@lazy Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttp)
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .baseUrl("https://api.vk.com/method/")
        .build()
        .create(VkApi::class.java)
}