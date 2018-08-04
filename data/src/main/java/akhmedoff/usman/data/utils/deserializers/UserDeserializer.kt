package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.User
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {

    override fun deserialize(
            json: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): User {
        Log.d("deserialize user", "start")
        val responseJson = json.asJsonObject
        val user = User()
        responseJson["id"]?.let { user.id = it.asJsonPrimitive.asLong }
        responseJson["first_name"]?.let { user.firstName = it.asString }
        responseJson["last_name"]?.let { user.lastName = it.asString }

        user.name = "${user.firstName} ${user.lastName}"

        responseJson["nickname"]?.let { user.nickname = it.asString }
        responseJson["screen_name"]?.let { user.screenName = it.asString }
        responseJson["photo_100"]?.let { user.photo100 = it.asString }
        responseJson["photo_max"]?.let { user.photoMax = it.asString }
        responseJson["photo_max_orig"]?.let { user.photoMaxOrig = it.asString }
        responseJson["photo_id"]?.let { user.photoId = it.asString }

        user.isFriend = when (responseJson.asJsonObject["is_friend"]?.asInt) {
            null -> false
            0 -> false
            else -> true
        }

        responseJson["friend_status"]?.let { user.friendStatus = it.asInt }

        Log.d("deserialize user", "end")

        return user
    }
}