package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.Group
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GroupDeserializer : JsonDeserializer<Group> {
    override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
    ): Group {
        val groups = mutableListOf<Group>()
        val groupJson = json.asJsonObject
        val group = Group()
        group.id = groupJson["id"].asJsonPrimitive.asLong
        group.name = groupJson["name"].asString
        group.screenName = groupJson["screen_name"].asString

        group.isClosed = when (groupJson["is_closed"]?.asInt) {
            null -> false
            0 -> false
            else -> true
        }
        group.type = groupJson["type"].asString

        group.isAdmin = when (groupJson["is_admin"]?.asInt) {
            null -> false
            0 -> false
            else -> true
        }

        group.isMember = when (groupJson["is_member"]?.asInt) {
            null -> false
            0 -> false
            else -> true
        }
        group.photo50 = groupJson["photo_50"].asString
        group.photo100 = groupJson["photo_100"].asString
        group.photo200 = groupJson["photo_200"].asString
        groups.add(group)
        return group
    }
}