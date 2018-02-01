package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.ResponseCatalog
import akhmedoff.usman.videoforvk.model.VideoCatalog
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CatalogDeserializer : JsonDeserializer<ResponseCatalog> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ResponseCatalog {

        val jsonObject = json.asJsonObject["response"].asJsonObject

        val catalogsJsonArray = jsonObject["items"].asJsonArray

        val catalogs = mutableListOf<Catalog>()

        catalogsJsonArray.forEach { catalogElement ->
            val catalogJson = catalogElement.asJsonObject
            val videos = mutableListOf<VideoCatalog>()

            val videosJsonArray = catalogJson["items"].asJsonArray

            videosJsonArray.forEach { videoElement ->
                val videoJson = videoElement.asJsonObject

                val item = VideoCatalog()

                item.id = videoJson["id"].asJsonPrimitive.asInt
                item.ownerId = videoJson["owner_id"].asInt
                item.title = stringToUrlFormat(videoJson["title"].toString())!!
                videoJson["duration"]?.let { item.duration = it.asInt }
                videoJson["description"]?.let { item.description = it.toString() }
                videoJson["date"]?.let { item.date = it.asLong }
                item.comments = videoJson["comments"].asInt
                item.views = videoJson["views"].asInt
                item.accessKey = videoJson["access_key"].toString()
                item.photo130 = stringToUrlFormat(videoJson["photo_130"].toString())!!
                item.photo320 = stringToUrlFormat(videoJson["photo_320"].toString())!!
                videoJson["photo_640"]?.let { item.photo640 = stringToUrlFormat(it.toString()) }
                videoJson["photo_800"]?.let { item.photo800 = stringToUrlFormat(it.toString()) }
                item.platform = videoJson["platform"]?.toString()
                item.canAdd = videoJson["can_add"].asBoolean
                videoJson["type"]?.let { item.type = it.toString() }

                videos.add(item)
            }

            val name = catalogJson["name"].toString()
            val id = catalogJson["id"].toString()
            val view = catalogJson["view"].toString()
            val canHide = catalogJson["can_hide"].asBoolean
            val type = catalogJson["type"]?.toString()

            catalogs.add(Catalog(videos, name, id, view, canHide, type))
        }

        return ResponseCatalog(catalogs, jsonObject["next"].toString())
    }
}