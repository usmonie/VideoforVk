package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.model.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class VideoDeserializer : JsonDeserializer<ResponseVideo> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ResponseVideo {

        val jsonObject = json.asJsonObject["response"].asJsonObject

        val itemsJson = jsonObject["items"].asJsonArray

        val items = mutableListOf<Video>()

        itemsJson.forEach {
            val itemJson = it.asJsonObject

            val fileJson = itemJson["files"].asJsonObject
            val files = Files(fileJson["mp4_240"]?.asString,
                    fileJson["mp4_360"]?.asString,
                    fileJson["mp4_480"]?.asString,
                    fileJson["mp4_720"]?.asString,
                    fileJson["mp4_1080"]?.asString,
                    fileJson["external"]?.asString)

            val likesJson = itemJson["likes"].asJsonObject
            val likes = Likes(likesJson["user_likes"].asBoolean,
                    likesJson["count"].asInt)

            val repostsJson = itemJson["reposts"].asJsonObject
            val reposts = Reposts(repostsJson["count"].asInt,
                    repostsJson["user_reposted"].asBoolean)
            val item = Video()

            item.id = itemJson["id"].asJsonPrimitive.asInt
            item.ownerId = itemJson["owner_id"].asInt
            item.title = itemJson["title"].asString
            item.duration = itemJson["duration"].asInt
            item.description = itemJson["description"].asString
            item.date = itemJson["date"].asInt
            item.comments = itemJson["comments"].asInt
            item.views = itemJson["views"].asInt
            item.width = itemJson["width"]?.asInt
            item.height = itemJson["height"]?.asInt
            itemJson["photo_130"].apply { item.photo130 = it.toString() }
            itemJson["photo_320"].apply { item.photo320 = it.toString() }
            itemJson["photo_800"].apply { item.photo800 = it.toString() }
            item.addingDate = itemJson["adding_date"].asInt
            itemJson["first_frame_320"].apply { item.firstFrame320 = it.toString() }
            itemJson["first_frame_160"].apply { item.firstFrame160 = it.toString() }
            itemJson["first_frame_130"].apply { item.firstFrame130 = it.toString() }
            itemJson["first_frame_800"].apply { item.firstFrame800 = it.toString() }
            item.files = files
            item.player = itemJson["player"].asString
            item.canAdd = itemJson["can_add"].asBoolean
            item.canComment = itemJson["can_comment"].asBoolean
            item.canRepost = itemJson["can_repost"].asBoolean
            item.likes = likes
            item.reposts = reposts
            item.repeat = itemJson["repeat"].asBoolean

            items.add(item)
        }

        val profilesJson = jsonObject["profiles"].asJsonArray

        val profiles = mutableListOf<User>()

        profilesJson.forEach {
            val profileJson = it.asJsonObject

            profiles.add(User(profileJson["id"].asLong,
                    profileJson["first_name"].asString,
                    profileJson["last_name"].asString))
        }

        val groupsJson = jsonObject["groups"].asJsonArray

        val groups = mutableListOf<Group>()

        groupsJson.forEach {
            val groupJson = it.asJsonObject

            groups.add(Group(groupJson["id"].asLong,
                    groupJson["name"].asString,
                    groupJson["screen_name"].asString,
                    groupJson["is_closed"].asBoolean,
                    groupJson["type"].asString,
                    groupJson["is_admin"].asBoolean,
                    groupJson["is_member"].asBoolean,
                    groupJson["photo_50"].asString,
                    groupJson["photo_100"].asString,
                    groupJson["photo_200"].asString))
        }

        return ResponseVideo(jsonObject["count"].asInt, items, profiles, groups)
    }
}