package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.model.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class VideoDeserializer : JsonDeserializer<ResponseVideo> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ResponseVideo {

        val jsonObject = json?.asJsonObject?.get("response")?.asJsonObject!!

        val itemsJson = jsonObject.get("items").asJsonArray

        val items = mutableListOf<Item>()

        itemsJson.forEach {
            val itemJson = it.asJsonObject

            val fileJson = itemJson.get("files").asJsonObject
            val files = Files(fileJson.get("mp4_240")?.asString,
                    fileJson.get("mp4_360")?.asString,
                    fileJson.get("mp4_480")?.asString,
                    fileJson.get("mp4_720")?.asString,
                    fileJson.get("mp4_1080")?.asString,
                    fileJson.get("external")?.asString)

            val likesJson = itemJson.get("likes").asJsonObject
            val likes = Likes(likesJson.get("user_likes").asBoolean,
                    likesJson.get("count").asInt)

            val repostsJson = itemJson.get("reposts").asJsonObject
            val reposts = Reposts(repostsJson.get("count").asInt,
                    repostsJson.get("user_reposted").asBoolean)

            items.add(Item(jsonObject.get("id")?.asLong,
                    jsonObject.get("owner_id")?.asLong,
                    jsonObject.get("title")?.asString,
                    jsonObject.get("duration")?.asInt,
                    jsonObject.get("description")?.asString,
                    jsonObject.get("date")?.asLong,
                    jsonObject.get("comments")?.asInt,
                    jsonObject.get("views")?.asInt,
                    jsonObject.get("width")?.asInt,
                    jsonObject.get("height")?.asInt,
                    jsonObject.get("photo_130")?.asString,
                    jsonObject.get("photo_320")?.asString,
                    jsonObject.get("photo_800")?.asString,
                    jsonObject.get("adding_date")?.asInt,
                    jsonObject.get("first_frame_320")?.asString,
                    jsonObject.get("first_frame_160")?.asString,
                    jsonObject.get("first_frame_130")?.asString,
                    jsonObject.get("first_frame_800")?.asString,
                    files,
                    jsonObject.get("player")?.asString,
                    jsonObject.get("can_add")?.asBoolean,
                    jsonObject.get("can_comment")?.asBoolean,
                    jsonObject.get("can_repost")?.asBoolean,
                    likes,
                    reposts,
                    jsonObject.get("repeat")?.asBoolean))
        }

        val profilesJson = jsonObject.get("profiles").asJsonArray

        val profiles = mutableListOf<User>()

        profilesJson.forEach {
            val profileJson = it.asJsonObject

            profiles.add(User(profileJson.get("id").asLong,
                    profileJson.get("first_name").asString,
                    profileJson.get("last_name").asString))
        }

        val groupsJson = jsonObject.get("groups").asJsonArray

        val groups = mutableListOf<Group>()

        groupsJson.forEach {
            val groupJson = it.asJsonObject

            groups.add(Group(groupJson.get("id").asLong,
                    groupJson.get("name").asString,
                    groupJson.get("screen_name").asString,
                    groupJson.get("is_closed").asBoolean,
                    groupJson.get("type").asString,
                    groupJson.get("is_admin").asBoolean,
                    groupJson.get("is_member").asBoolean,
                    groupJson.get("photo_50").asString,
                    groupJson.get("photo_100").asString,
                    groupJson.get("photo_200").asString))
        }

        return ResponseVideo(jsonObject.get("count").asInt, items, profiles, groups)
    }
}