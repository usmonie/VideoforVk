package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.model.User
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {
        val jsonObject = json?.asJsonObject!!

        return User(jsonObject.get("id").asLong,
                jsonObject.get("first_name").asString,
                jsonObject.get("last_name").asString,
                jsonObject.get("nickname").asString,
                jsonObject.get("screen_name").asString,
                jsonObject.get("photo_100").asString,
                jsonObject.get("photo_max").asString,
                jsonObject.get("photo_max_orig").asString,
                jsonObject.get("photo_id").asString,
                jsonObject.get("has_photo").asBoolean,
                jsonObject.get("is_friend").asBoolean,
                jsonObject.get("friend_status").asBoolean,
                jsonObject.get("online").asBoolean,
                jsonObject.get("status").asString,
                jsonObject.get("is_favorite").asBoolean)
    }
}