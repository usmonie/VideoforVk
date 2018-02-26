package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.User
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<List<User>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<User> {
        Log.d("deserialize user", "start")
        val responseJson = json.asJsonObject["response"].asJsonArray
        val users = mutableListOf<User>()
        responseJson.forEach {
            val user = User()
            it.asJsonObject["id"]?.let { user.id = it.asJsonPrimitive.asLong }
            it.asJsonObject["first_name"]?.let { user.firstName = it.asString }
            it.asJsonObject["last_name"]?.let { user.lastName = it.asString }
            it.asJsonObject["nickname"]?.let { user.nickname = it.asString }
            it.asJsonObject["screen_name"]?.let { user.screenName = it.asString }
            it.asJsonObject["photo_100"]?.let { user.photo100 = it.asString }
            it.asJsonObject["photo_max"]?.let { user.photoMax = it.asString }
            it.asJsonObject["photo_max_orig"]?.let { user.photoMaxOrig = it.asString }
            it.asJsonObject["photo_id"]?.let { user.photoId = it.asString }

            user.isFriend = when (it.asJsonObject["is_friend"]?.asInt) {
                null -> false
                0 -> false
                else -> true
            }

            it.asJsonObject["friend_status"]?.let { user.friendStatus = it.asInt }
            users.add(user)
        }
        Log.d("deserialize user", "end")

        return users
    }
}