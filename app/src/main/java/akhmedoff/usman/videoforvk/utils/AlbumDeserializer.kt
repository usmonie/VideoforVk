package akhmedoff.usman.videoforvk.utils

import akhmedoff.usman.videoforvk.model.Album
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AlbumDeserializer : JsonDeserializer<Album> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Album {
        val jsonObject = json.asJsonObject["response"].asJsonObject!!

        val id = stringToUrlFormat(jsonObject["id"]?.toString())!!
        val ownerId = stringToUrlFormat(jsonObject["owner_id"]?.toString())!!
        val title = stringToUrlFormat(jsonObject["title"]?.toString())!!
        val count = jsonObject["count"]?.asJsonPrimitive?.asInt!!
        val photo320 = stringToUrlFormat(jsonObject["photo_320"]?.toString())!!
        val photo160 = stringToUrlFormat(jsonObject["photo_160"]?.toString())!!

        return Album(id, ownerId, title, count, photo320, photo160)
    }
}