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

        val id = jsonObject["id"]?.asJsonPrimitive?.asString!!
        val ownerId = jsonObject["owner_id"]?.asJsonPrimitive?.asString!!
        val title = jsonObject["title"]?.asJsonPrimitive?.asString!!
        val count = jsonObject["count"]?.asJsonPrimitive?.asInt!!
        val photo320 = jsonObject["photo_320"]?.asJsonPrimitive?.asString!!
        val photo160 = jsonObject["photo_160"]?.asJsonPrimitive?.asString!!

        return Album(id, ownerId, title, count, photo320, photo160)
    }
}