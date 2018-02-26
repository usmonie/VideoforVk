package akhmedoff.usman.data.utils.typeadapters

import akhmedoff.usman.data.model.User
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class UserTypeAdapter : TypeAdapter<List<User>>() {
    override fun write(out: JsonWriter?, value: List<User>?) {
    }

    override fun read(input: JsonReader?): List<User> {
        val users = mutableListOf<User>()

        input?.let {
            it.beginObject()
            while (it.hasNext()) {
                val user = User()

                when (it.nextName()) {
                    "id" -> user.id = it.nextLong()

                    "first_name" -> user.firstName = it.nextString()

                    "last_name" -> user.lastName = it.nextString()

                    "screen_name" -> user.screenName = it.nextString()

                    "photo_100" -> user.photo100 = it.nextString()
                    "photo_max" -> user.photoMax = it.nextString()
                    "photo_max_orig" -> user.photoMaxOrig = it.nextString()
                    "photo_id" -> user.photoId = it.nextString()
                    "is_friend" -> user.isFriend = when (it.nextInt()) {
                        0 -> false
                        else -> true
                    }
                    "friend_status" -> user.friendStatus = it.nextInt()

                }
            }
        }

        return users
    }
}