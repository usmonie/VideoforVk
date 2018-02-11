package akhmedoff.usman.videoforvk.utils.interceptors

import akhmedoff.usman.videoforvk.App
import akhmedoff.usman.videoforvk.App.Companion.context
import akhmedoff.usman.videoforvk.data.api.VkApi
import akhmedoff.usman.videoforvk.data.local.UserSettings
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class AutentificationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val originalHttpUrl = original.url()

        val httpBuilder = originalHttpUrl.newBuilder()
        val userSettings = UserSettings.getUserSettings(context)

        if (userSettings.isLogged) httpBuilder.addQueryParameter(
            "access_token",
            UserSettings.getUserSettings(context).getToken()
        )

        val url = httpBuilder.addQueryParameter("v", VkApi.API_VERSION).build()


        // Request customization: add request headers
        val requestBuilder = original.newBuilder()
            .url(url)

        var str = ""
        var appbuild = 0
        try {
            val pkgInfo = App.context.packageManager.getPackageInfo(App.context.packageName, 0)
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

        return chain.proceed(request)
    }
}