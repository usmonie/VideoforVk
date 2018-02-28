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
        val jsonObject = json.asJsonObject["response"].asJsonObject
        val album = Album()

        album.id = jsonObject["id"]?.asJsonPrimitive?.asInt!!
        album.ownerId = jsonObject["owner_id"]?.asJsonPrimitive?.asInt!!
        album.title = jsonObject["title"]?.asJsonPrimitive?.asString!!
        album.count = jsonObject["count"]?.asJsonPrimitive?.asInt!!
        album.photo320 = jsonObject["photo_320"]?.asJsonPrimitive?.asString!!

        return album
    }
}