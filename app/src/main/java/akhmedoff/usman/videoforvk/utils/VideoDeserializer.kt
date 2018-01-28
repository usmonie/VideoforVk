package akhmedoff.usman.videoforvk.utils

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
            var external = stringToUrlFormat(fileJson["external"]?.toString())

            var mp4240 = stringToUrlFormat(fileJson["mp4_240"]?.toString())
            val mp4360 = stringToUrlFormat(fileJson["mp4_360"]?.toString())
            val mp4480 = stringToUrlFormat(fileJson["mp4_480"]?.toString())
            val mp4720 = stringToUrlFormat(fileJson["mp4_720"]?.toString())
            val mp41080 = stringToUrlFormat(fileJson["mp4_1080"]?.toString())
            val hls = stringToUrlFormat(fileJson["hls"]?.toString())

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
            item.title = stringToUrlFormat(itemJson["title"].toString())!!
            item.duration = itemJson["duration"].asJsonPrimitive.asInt
            item.description = stringToUrlFormat(itemJson["description"].toString())
            item.date = itemJson["date"].asJsonPrimitive.asLong
            item.comments = itemJson["comments"].asJsonPrimitive.asInt
            item.views = itemJson["views"].asJsonPrimitive.asInt
            item.width = itemJson["width"]?.asJsonPrimitive?.asInt
            item.height = itemJson["height"]?.asJsonPrimitive?.asInt
            itemJson["photo_130"]?.let { item.photo130 = it.toString() }
            itemJson["photo_320"]?.let { item.photo320 = it.toString() }
            itemJson["photo_800"]?.let { item.photo800 = it.toString() }
            itemJson["adding_date"]?.let { item.addingDate = it.asJsonPrimitive.asLong }
            itemJson["first_frame_320"]?.let { item.firstFrame320 = it.toString() }
            itemJson["first_frame_160"]?.let { item.firstFrame160 = it.toString() }
            itemJson["first_frame_130"]?.let { item.firstFrame130 = it.toString() }
            itemJson["first_frame_800"]?.let { item.firstFrame800 = it.toString() }
            item.files = files
            item.player = itemJson["player"].toString()
            item.canAdd = itemJson["can_add"].asJsonPrimitive.asBoolean
            item.canComment = itemJson["can_comment"].asJsonPrimitive.asBoolean
            item.canRepost = itemJson["can_repost"].asJsonPrimitive.asBoolean
            item.likes = likes
            item.reposts = reposts
            item.repeat = itemJson["repeat"].asJsonPrimitive.asBoolean

            items.add(item)
        }

        val profilesJson = jsonObject["profiles"].asJsonArray

        val profiles = mutableListOf<User>()

        profilesJson.forEach {
            val profileJson = it.asJsonObject

            profiles.add(
                User(
                    profileJson["id"].asJsonPrimitive.asLong,
                    profileJson["first_name"].toString(),
                    profileJson["last_name"].toString()
                )
            )
        }

        val groupsJson = jsonObject["groups"].asJsonArray

        val groups = mutableListOf<Group>()

        groupsJson.forEach {
            val groupJson = it.asJsonObject

            groups.add(
                Group(
                    groupJson["id"].asJsonPrimitive.asLong,
                    stringToUrlFormat(groupJson["name"].toString())!!,
                    groupJson["screen_name"].toString(),
                    groupJson["is_closed"].asJsonPrimitive.asBoolean,
                    groupJson["type"].toString(),
                    groupJson["is_admin"].asJsonPrimitive.asBoolean,
                    groupJson["is_member"].asJsonPrimitive.asBoolean,
                    stringToUrlFormat(groupJson["photo_50"].toString())!!,
                    stringToUrlFormat(groupJson["photo_100"].toString())!!,
                    stringToUrlFormat(groupJson["photo_200"].toString())!!
                )
            )
        }

        return ResponseVideo(jsonObject["count"].asJsonPrimitive.asInt, items, profiles, groups)
    }
}