package akhmedoff.usman.videoforvk.utils.deserializers

import akhmedoff.usman.videoforvk.model.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class VideoDeserializer : JsonDeserializer<ResponseVideo> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ResponseVideo {
        val jsonObject = json.asJsonObject["response"].asJsonObject

        val itemsJson = jsonObject["items"].asJsonArray

        val items = mutableListOf<Video>()

        itemsJson.forEach {
            val itemJson = it.asJsonObject

            val fileJson = itemJson["files"].asJsonObject
            var external = fileJson["external"]?.asString

            var mp4240 = fileJson["mp4_240"]?.asString
            val mp4360 = fileJson["mp4_360"]?.asString
            val mp4480 = fileJson["mp4_480"]?.asString
            val mp4720 = fileJson["mp4_720"]?.asString
            val mp41080 = fileJson["mp4_1080"]?.asString
            val hls = fileJson["hls"]?.asString

            if (external != null && (external.endsWith(".mp4") || external.contains("vk.com"))) {
                mp4240 = external
                external = null
            }

            val files = Files()

            files.mp4240 = mp4240
            files.mp4360 = mp4360
            files.mp4480 = mp4480
            files.mp4720 = mp4720
            files.mp41080 = mp41080
            files.hls = hls
            files.external = external

            val likesJson = itemJson["likes"].asJsonObject
            val likes = Likes(
                likesJson["user_likes"].asJsonPrimitive.asBoolean,
                likesJson["count"].asJsonPrimitive.asInt
            )

            val repostsJson = itemJson["reposts"].asJsonObject
            val reposts = Reposts(
                repostsJson["count"].asJsonPrimitive.asInt,
                repostsJson["user_reposted"].asJsonPrimitive.asBoolean
            )
            val item = Video()

            item.id = itemJson["id"].asJsonPrimitive.asInt
            item.ownerId = itemJson["owner_id"].asJsonPrimitive.asInt
            item.title = itemJson["title"].asString
            item.duration = itemJson["duration"].asJsonPrimitive.asInt
            item.description = itemJson["description"].asString
            item.date = itemJson["date"].asJsonPrimitive.asLong
            item.comments = itemJson["comments"].asJsonPrimitive.asInt
            item.views = itemJson["views"].asJsonPrimitive.asInt
            item.width = itemJson["width"]?.asJsonPrimitive?.asInt
            item.height = itemJson["height"]?.asJsonPrimitive?.asInt

            itemJson["photo_130"]?.let {
                item.photo130 = it.asString
            }
            itemJson["photo_320"]?.let {
                item.photo320 = it.asString
            }
            itemJson["photo_800"]?.let {
                item.photo800 = it.asString
            }
            itemJson["adding_date"]?.let { item.addingDate = it.asJsonPrimitive.asLong }
            itemJson["first_frame_320"]?.let {
                item.firstFrame320 = it.asString
            }
            itemJson["first_frame_160"]?.let {
                item.firstFrame160 = it.asString
            }
            itemJson["first_frame_130"]?.let {
                item.firstFrame130 = it.asString
            }
            itemJson["first_frame_800"]?.let {
                item.firstFrame800 = it.asString
            }
            item.files = files
            item.player = itemJson["player"].asString

            item.canAdd = when (itemJson["can_add"]?.asInt) {
                null -> false
                0 -> false
                else -> true
            }

            item.canRepost = when (itemJson["can_repost"]?.asInt) {
                null -> false
                0 -> false
                else -> true
            }

            item.canComment = when (itemJson["can_comment"]?.asInt) {
                null -> false
                0 -> false
                else -> true
            }

            item.repeat = when (itemJson["repeat"]?.asInt) {
                null -> false
                0 -> false
                else -> true
            }
            item.likes = likes
            item.reposts = reposts

            items.add(item)
        }

        val profilesJson = jsonObject["profiles"]?.asJsonArray

        val profiles = mutableListOf<User>()

        profilesJson?.forEach {
            val profileJson = it.asJsonObject

            val user = User()
            user.id = profileJson["id"].asJsonPrimitive.asLong
            user.firstName = profileJson["first_name"].asString
            user.lastName = profileJson["last_name"].asString

            profiles.add(user)
        }

        val groupsJson = jsonObject["groups"]?.asJsonArray

        val groups = mutableListOf<Group>()

        groupsJson?.forEach {
            val groupJson = it.asJsonObject

            groups.add(
                Group(
                    groupJson["id"].asJsonPrimitive.asLong,
                    groupJson["name"].asString,
                    groupJson["screen_name"].asString,
                    groupJson["is_closed"].asJsonPrimitive.asBoolean,
                    groupJson["type"].asString,
                    groupJson["is_admin"].asBoolean,
                    groupJson["is_member"].asBoolean,
                    groupJson["photo_50"].asString,
                    groupJson["photo_100"].asString,
                    groupJson["photo_200"].asString
                )
            )
        }

        return ResponseVideo(jsonObject["count"].asJsonPrimitive.asInt, items, profiles, groups)
    }
}