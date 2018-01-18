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
                item.title = videoJson["title"].asString
                item.duration = videoJson["duration"].asInt
                item.description = videoJson["description"].asString
                item.date = videoJson["date"].asInt
                item.comments = videoJson["comments"].asInt
                item.views = videoJson["views"].asInt
                item.accessKey = videoJson["access_key"].asString
                videoJson["photo_130"].apply { item.photo130 = videoJson["photo_130"].toString() }
                videoJson["photo_320"].apply { item.photo320 = videoJson["photo_320"].toString() }
                videoJson["photo_640"]?.apply { item.photo640 = videoJson["photo_640"].toString() }
                videoJson["photo_800"]?.apply { item.photo800 = videoJson["photo_800"].toString() }
                item.platform = videoJson["platform"]?.asString
                item.canAdd = videoJson["can_add"].asBoolean
                item.type = videoJson["type"].asString

                videos.add(item)
            }

            val name = catalogJson["name"].asString
            val id = catalogJson["id"].asInt
            val view = catalogJson["view"].asString
            val canHide = catalogJson["can_hide"].asBoolean
            val type = catalogJson["type"].asString

            catalogs.add(Catalog(videos, name, id, view, canHide, type))
        }

        return ResponseCatalog(catalogs, jsonObject["next"].asString)
    }
}