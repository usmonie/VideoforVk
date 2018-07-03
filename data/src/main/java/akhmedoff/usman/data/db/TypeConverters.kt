package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.data.model.CatalogItemType
import akhmedoff.usman.data.model.VideoUrl
import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class SimpleTypeConverters {
    private val gson = Gson()

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

    @TypeConverter
    fun toUsers(data: String): List<String> {
        return data.split(", ")
    }

    @TypeConverter
    fun fromUserIds(data: List<String>): String {
        return data.joinToString()
    }

    @TypeConverter
    fun stringToCatalogItemList(data: String?): List<CatalogItem> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<CatalogItem>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromCatalogItemsToString(catalogItems: List<CatalogItem>) = gson.toJson(catalogItems)
}