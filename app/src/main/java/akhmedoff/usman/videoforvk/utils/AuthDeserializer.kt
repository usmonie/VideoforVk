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
        val jsonObject = json.asJsonObject!!
        val auth = Auth()

        jsonObject["access_token"]?.let { auth.accessToken = jsonObject["access_token"].asString }
        jsonObject["expires_in"]?.let { auth.expiresIn = jsonObject["expires_in"].asInt }
        jsonObject["user_id"]?.let { auth.userId = jsonObject["user_id"].asInt }

        jsonObject["error"]?.let { auth.error = jsonObject["error"].asString }
        jsonObject["error_description"]?.let {
            auth.errorDescription = jsonObject["error_description"].asString
        }
        jsonObject["redirect_uri"]?.let { auth.redirectUri = jsonObject["redirect_uri"].asString }

        return auth
    }
}
