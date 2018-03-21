package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.*
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
            val item = Video()

            val itemJson = it.asJsonObject

            val videoUrls = mutableListOf<VideoUrl>()

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

            external?.let { videoUrls.add(VideoUrl(it, Quality.EXTERNAL)) }
            mp4240?.let { videoUrls.add(VideoUrl(it, Quality.P240)) }
            mp4360?.let { videoUrls.add(VideoUrl(it, Quality.P360)) }
            mp4480?.let { videoUrls.add(VideoUrl(it, Quality.qHD)) }
            mp4720?.let { videoUrls.add(VideoUrl(it, Quality.HD)) }
            mp41080?.let { videoUrls.add(VideoUrl(it, Quality.FULLHD)) }
            hls?.let { videoUrls.add(VideoUrl(it, Quality.HLS)) }

            val likesJson = itemJson["likes"]?.asJsonObject

            likesJson?.let {
                val likes = Likes()
                likes.userLikes = it["user_likes"].asJsonPrimitive.asBoolean
                likes.count = it["count"].asJsonPrimitive.asInt

                item.likes = likes
            }

            val repostsJson = itemJson["reposts"]?.asJsonObject
            repostsJson?.let {
                val reposts = Reposts()

                reposts.count = repostsJson["count"].asJsonPrimitive.asInt
                reposts.userReposted = repostsJson["user_reposted"].asJsonPrimitive.asBoolean

                item.reposts = reposts
            }

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
            item.files = videoUrls
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
        }

        return ResponseVideo(jsonObject["count"].asJsonPrimitive.asInt, items, profiles, groups)
    }
}