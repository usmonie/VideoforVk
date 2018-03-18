package akhmedoff.usman.data.db

import akhmedoff.usman.data.model.CatalogItemType
import android.arch.persistence.room.TypeConverter


class TypeConverters {

    @TypeConverter
    fun toStatus(type: String): CatalogItemType {
        return when (type) {
            "VIDEO" -> CatalogItemType.VIDEO
            "ALBUM" -> CatalogItemType.ALBUM
            else -> throw IllegalArgumentException("Could not recognize status")
        }
    }

    @TypeConverter
    fun toInteger(type: CatalogItemType) = type.name
}