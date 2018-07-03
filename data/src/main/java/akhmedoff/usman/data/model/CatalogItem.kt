package akhmedoff.usman.data.model

import akhmedoff.usman.data.db.SimpleTypeConverters
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.TypeConverters

@Entity(
    tableName = "catalog_items",
    primaryKeys = ["id", "ownerId"],
    indices = [(Index(value = ["id", "ownerId"], unique = true))]
)
class CatalogItem : Item() {
    var accessKey: String? = null
    var platform: String? = null
    var catalogId: String? = null

    @TypeConverters(SimpleTypeConverters::class)
    var type: CatalogItemType? = null

    override fun equals(other: Any?) = when {
        other !is CatalogItem -> false
        id == other.id && ownerId == other.ownerId -> true
        else -> false
    }

    override fun hashCode(): Int {
        var result = accessKey?.hashCode() ?: 0
        result = 31 * result + (platform?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        return result
    }

}