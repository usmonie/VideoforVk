package akhmedoff.usman.videoforvk.utils.deserializers

import akhmedoff.usman.videoforvk.model.User
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): User {
        val jsonObject = json!!.asJsonObject

        return User(
            jsonObject["id"].asLong,
            jsonObject["first_name"].asString,
            jsonObject["last_name"].asString,
            jsonObject["nickname"].asString,
            jsonObject["screen_name"].asString,
            jsonObject["photo_100"].asString,
            jsonObject["photo_max"].asString,
            jsonObject["photo_max_orig"].asString,
            jsonObject["photo_id"].asString,
            jsonObject["has_photo"].asBoolean,
            jsonObject["is_friend"].asBoolean,
            jsonObject["friend_status"].asBoolean,
            jsonObject["online"].asBoolean,
            jsonObject["status"].asString,
            jsonObject["is_favorite"].asBoolean
        )
    }
}