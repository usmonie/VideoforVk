package akhmedoff.usman.data.utils.typeadapters

import akhmedoff.usman.data.model.User
import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class UserTypeAdapter : TypeAdapter<User>() {
    override fun write(out: JsonWriter?, value: User?) {
    }

    override fun read(input: JsonReader): User {
        val user = User()
        Log.d("deserialize user", "start")

        loop@ while (input.hasNext()) {
            val token = input.peek()

            if (token == JsonToken.BEGIN_OBJECT) {
                input.beginObject()

                when (input.nextName()) {
                    "id" -> user.id = input.nextLong()

                    "first_name" -> user.firstName = input.nextString()

                    "last_name" -> user.lastName = input.nextString()

                    "screen_name" -> user.screenName = input.nextString()

                    "photo_100" -> user.photo100 = input.nextString()
                    "photo_max" -> user.photoMax = input.nextString()
                    "photo_max_orig" -> user.photoMaxOrig = input.nextString()
                    "photo_id" -> user.photoId = input.nextString()
                    "is_friend" -> user.isFriend = when (input.nextInt()) {
                        0 -> false
                        else -> true
                    }
                    "friend_status" -> user.friendStatus = input.nextInt()

                }
                input.endObject()
            } else if (token == JsonToken.END_OBJECT) {
                break@loop
            }

        }

        return user
    }
}