package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.model.Auth
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AuthDeserializer : JsonDeserializer<Auth> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Auth {
        val jsonObject = json.asJsonObject
        val auth = Auth()

        jsonObject["access_token"]?.let { auth.accessToken = it.asString }
        jsonObject["expires_in"]?.let { auth.expiresIn = it.asInt }
        jsonObject["user_id"]?.let { auth.userId = it.asInt }

        jsonObject["error"]?.let { auth.error = it.asString }
        jsonObject["error_description"]?.let {
            auth.errorDescription = it.asString
        }
        jsonObject["redirect_uri"]?.let { auth.redirectUri = it.asString }

        return auth
    }
}
