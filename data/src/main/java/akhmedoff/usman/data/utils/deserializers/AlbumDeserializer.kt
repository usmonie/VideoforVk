package akhmedoff.usman.data.utils.deserializers

import akhmedoff.usman.data.model.Album
import android.util.Log
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
        Log.d("deserialize album", "start")
        val jsonObject = json.asJsonObject
        val album = Album()
        jsonObject["id"]?.asJsonPrimitive?.asInt?.let { album.id = it }
        jsonObject["owner_id"]?.asJsonPrimitive?.asInt?.let { album.ownerId = it }
        jsonObject["title"]?.asJsonPrimitive?.asString?.let { album.title = it }
        jsonObject["likes"]?.asJsonPrimitive?.asInt?.let { album.count = it }
        jsonObject["photo_320"]?.asJsonPrimitive?.asString?.let { album.photo320 = it }

        return album
    }
}