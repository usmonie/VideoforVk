package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.model.VideoUrl
import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class TypeConverters {
    val gson = Gson()

    @TypeConverter
    fun toType(type: String) = when (type) {
        "VIDEO" -> CatalogItemType.VIDEO
        "ALBUM" -> CatalogItemType.ALBUM
        else -> throw IllegalArgumentException("Could not recognize status")
    }

    @TypeConverter
    fun toText(type: CatalogItemType) = type.name

    @TypeConverter
    fun toVideoUrls(data: String): List<VideoUrl> {
        val listType = object : TypeToken<List<VideoUrl>>() {}.type

        return gson.fromJson<List<VideoUrl>>(data, listType)
    }

    @TypeConverter
    fun fromVideoUrls(data: List<VideoUrl>): String {
        return gson.toJson(data)
    }
}