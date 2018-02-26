package akhmedoff.usman.data.utils.interceptors

import akhmedoff.usman.data.api.VkApi
import akhmedoff.usman.data.local.UserSettings
import android.content.Context
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class AuthenticationInterceptor(val context: Context) : Interceptor {

    // @Throws(LogException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val originalHttpUrl = original.url()

        val httpBuilder = originalHttpUrl.newBuilder()
        val userSettings = UserSettings.getUserSettings(context)
        if (userSettings.isLogged) {
            httpBuilder.addQueryParameter(
                "access_token",
                userSettings.getToken()
            )
        }

        val url = httpBuilder.addQueryParameter("v", VkApi.API_VERSION).build()

        // Request customization: add request headers
        val requestBuilder = original.newBuilder().url(url)

        var str = ""
        var appbuild = 0
        try {
            val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            str = pkgInfo.versionName
            appbuild = pkgInfo.versionCode
        } catch (e: Exception) {
        }

        val request = requestBuilder
            .header(
                "User-Agent",
                String.format(
                    Locale.US,
                    "VKAndroidApp/%s-%d (Android %s; SDK %d; %s; %s %s; %s)",
                    str,
                    Integer.valueOf(appbuild),
                    Build.VERSION.RELEASE,
                    Integer.valueOf(Build.VERSION.SDK_INT),
                    Build.CPU_ABI,
                    Build.MANUFACTURER,
                    Build.MODEL,
                    System.getProperty("user.language")
                )
            )
            .method(original.method(), original.body())
            .build()

        /*response?.body()?.let {
            if (it.string().contains("\"error_code\": 5")) {
                requestBuilder.url("https://api.vk.com/method/secure.checkToken?token" + userSettings.getToken())
                val tokenResponse = chain.proceed(requestBuilder.build())

                tokenResponse?.body()?.let {
                    if (!it.string().contains("success = 1"))
                        throw LogException(response.message(), Error.INVALID_TOKEN)
                }
            }
        }*/
        return chain.proceed(request)
    }
}