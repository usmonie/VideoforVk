package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.Album
import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AlbumsDeserializer : JsonDeserializer<List<Album>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): List<Album> {
        Log.d("deserialize albums", "start")

        val jsonArray = json.asJsonObject["response"].asJsonArray

        val albums = mutableListOf<Album>()
        jsonArray.forEach {
            val id = it.asJsonObject["id"]!!.asJsonPrimitive.asString
            val ownerId = it.asJsonObject["owner_id"]!!.asJsonPrimitive.asString
            val title = it.asJsonObject["title"]!!.asJsonPrimitive.asString
            val count = it.asJsonObject["count"]!!.asJsonPrimitive.asInt
            val photo320 = it.asJsonObject["photo_320"]!!.asJsonPrimitive.asString
            val photo160 = it.asJsonObject["photo_160"]!!.asJsonPrimitive.asString
            albums.add(Album(id, ownerId, title, count, photo320, photo160))
        }

        return albums
    }
}