package akhmedoff.usman.videoforvk.utils.deserializers

import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.CatalogItemType
import akhmedoff.usman.videoforvk.model.ResponseCatalog
import akhmedoff.usman.videoforvk.utils.stringToUrlFormat
import android.util.Log
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

        Log.d("DESERIALIZER", "started")
        val jsonObject = json.asJsonObject["response"]?.asJsonObject
        val catalogs = mutableListOf<Catalog>()

        jsonObject?.let {
            val catalogsJsonArray = jsonObject["items"]?.asJsonArray

            catalogsJsonArray?.forEach { catalogElement ->
                val catalogJson = catalogElement.asJsonObject
                val videos = mutableListOf<CatalogItem>()

                val videosJsonArray = catalogJson["items"].asJsonArray

                videosJsonArray.forEach { videoElement ->
                    val videoJson = videoElement.asJsonObject

                    val item = CatalogItem()
                    with(item) {
                        id = videoJson["id"].asJsonPrimitive.asInt
                        ownerId = videoJson["owner_id"].asInt
                        title = videoJson["title"].asString

                        videoJson["duration"]?.let { duration = it.asInt }
                        videoJson["description"]?.let { description = it.asString }
                        videoJson["date"]?.let { date = it.asLong }
                        videoJson["comments"]?.let { comments = it.asInt }
                        videoJson["views"]?.let { views = it.asInt }
                        videoJson["access_key"]?.let { accessKey = it.asString }
                        videoJson["photo_130"]?.let { photo130 = it.asString }
                        videoJson["photo_320"]?.let { photo320 = it.asString }
                        videoJson["photo_640"]?.let { photo640 = it.asString }
                        videoJson["photo_800"]?.let { photo800 = it.asString }
                        videoJson["platform"]?.let { platform = it.asString }
                        canAdd = videoJson["can_add"]?.asBoolean ?: false
                        videoJson["type"]?.let {
                            when (it.asString) {
                                "video" -> type = CatalogItemType.VIDEO
                                "album" -> type = CatalogItemType.ALBUM
                            }
                        }
                    }
                    videos.add(item)
                }

                val name =
                    stringToUrlFormat(catalogJson["name"]?.toString())


                var id: String? = null
                catalogJson["id"]?.let { id = it.toString() }

                val view = catalogJson["view"]?.toString()
                val canHide = catalogJson["can_hide"]?.asBoolean
                val type = catalogJson["type"]?.toString()

                catalogs.add(Catalog(videos, name, id, view, canHide, type))
            }
        }

        return ResponseCatalog(catalogs, jsonObject?.get("next")?.toString())
    }
}