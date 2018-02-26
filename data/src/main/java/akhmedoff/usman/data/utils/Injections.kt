package akhmedoff.usman.data.utils

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.model.*
import akhmedoff.usman.data.utils.deserializers.*
import akhmedoff.usman.data.utils.typeadapters.UserTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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
        val typeUsers = object : TypeToken<List<User>>() {}.type
        //TODO (type adapter https://stackoverflow.com/questions/43455825/retrofit-2-gson-and-custom-deserializer)
        registerTypeAdapter(
            typeUsers,
            UserTypeAdapter()
        )
        registerTypeAdapter(
            CheckTokenResponse::class.java,
            CheckTokenDeserializer()
        )

        registerTypeAdapter(
            getClassFromGeneric<List<Group>>(),
            GroupDeserializer()
        )

        registerTypeAdapter(
            getClassFromGeneric<List<Album>>(),
            AlbumsDeserializer()
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

/*val database: AppDatabase by lazy {

}*/

private inline fun <reified T> getClassFromGeneric() = T::class.java
